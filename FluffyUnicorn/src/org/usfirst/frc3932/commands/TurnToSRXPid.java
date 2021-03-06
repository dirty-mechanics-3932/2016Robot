package org.usfirst.frc3932.commands;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.RobotMap;
import org.usfirst.frc3932.Robot.ROBOTTYPES;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.StatusFrameRate;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Date;

public class TurnToSRXPid extends Command {
	double m_degrees;
	double m_timeout;
	double m_distance = 0;
	boolean m_targeting = false;
	double startingYaw;
	int coast = 0;

	double rightPosInitial;
	double leftPosInitial;
	double lastYawError;
	
	boolean adjustA = false;
	boolean adjustB = false;

	Date m_init;
	int ignoreDataCount;

	enum SIDE {
		Right, Left
	};

	public TurnToSRXPid(double degrees, double timeout) {
		requires(Robot.driveSystem);
		m_degrees = degrees;
		m_timeout = timeout;
		m_distance = 0;
	}

	public TurnToSRXPid(double degrees, double timeout, boolean targeting) {
		requires(Robot.driveSystem);
		m_degrees = degrees;
		m_timeout = timeout;
		m_targeting = targeting;
		m_distance = 0;
	}

	public TurnToSRXPid(double degrees, double timeout, double distance) {
		requires(Robot.driveSystem);
		m_degrees = degrees;
		m_timeout = timeout;
		m_distance = distance;
	}

	public TurnToSRXPid(double degrees) { // Overloaded for 5 second time out
		this(degrees, 5);
	}

	public void initialize() {
		Robot.driveSystem.resetEncoders();
		m_init = new Date();
		startingYaw = Robot.ahrs.getYaw();
		double rotations = (m_degrees - startingYaw) * Robot.conf.rotateFactor;
		talonInit(RobotMap.driveSystemLeftFront, SIDE.Left);
		talonInit(RobotMap.driveSystemRightFront, SIDE.Right);
		rightPosInitial = RobotMap.driveSystemRightFront.getPosition();
		leftPosInitial = RobotMap.driveSystemLeftFront.getPosition();
		Robot.navXPin9TurningSRXPid.set(true);

		rightPosInitial = 0;
		leftPosInitial = 0;

		Robot.logf("+++++ Init TurnToSRXPID Degrees: %.2f Timeout:%.2f Mode:%s Yaw:%.2f Rotations:%.2f  +++++%n",
				m_degrees, m_timeout, RobotMap.driveSystemLeftFront.getControlMode().name(), startingYaw, rotations);
		Robot.logf("RP:%.2f LP:%.2f Config: %s%n", rightPosInitial, leftPosInitial, Robot.conf.currentConfig());
		Robot.logf("AHRS Version:%s Pin8 Right:%s Pin9 Left:%s%n", Robot.ahrs.getFirmwareVersion(),
				(Robot.pin8.get() ? "True" : "False"), (Robot.pin9.get() ? "True" : "False"));
		// if (!m_targeting) {
		setRotations(RobotMap.driveSystemRightFront,
				rightPosInitial + rotations + m_distance * Robot.conf.ticksPerFoot);
		setRotations(RobotMap.driveSystemLeftFront, leftPosInitial + rotations - m_distance * Robot.conf.ticksPerFoot);
		// }
		coast = 0;
		ignoreDataCount = 2; // Set to ignore the results for 2 passes
		lastYawError = rotations;
		if (Robot.conf.brakeMode) // Turn on break mode if parameter is set
			Robot.setBrakeMode(true);
	}

	public void execute() {
	}

	/* (non-Javadoc)
	 * @see edu.wpi.first.wpilibj.command.Command#isFinished()
	 */
	public boolean isFinished() {
		// Look to error in the PID's to see if we are done
		int rightErr = RobotMap.driveSystemRightFront.getClosedLoopError();
		int leftErr = RobotMap.driveSystemLeftFront.getClosedLoopError();
		double convTime = (new Date().getTime() - m_init.getTime());
		String rightS = miscData(RobotMap.driveSystemRightFront, SIDE.Right);
		String leftS = miscData(RobotMap.driveSystemLeftFront, SIDE.Left);
		double yaw = Robot.ahrs.getYaw();
		double yawError = (m_degrees - yaw);
		Robot.logf("Conv:%7.2f Yaw:%.2f YErr:%.2f Coast:%d %s %s%n", convTime, yaw, yawError, coast, rightS, leftS);
		// Ignore the first few updates after any change in the SetPoints
		if (ignoreDataCount > 0) {
			ignoreDataCount--;
			return false;
		}
		if (convTime > m_timeout * 1000) // If timeout stop immediately
			return true;
		if (Robot.robotType == ROBOTTYPES.MINI) {
			// If error is in range coast for a while
			if (Math.abs(leftErr) < Robot.conf.maxError && Math.abs(rightErr) < Robot.conf.maxError) {
				coast++;
			}
			// Pass thru zero stop
			if (lastYawError * yawError < 0)
				return true;
			lastYawError = yawError;
			// Stop when error is in range
			if (Math.abs((yawError)) < .25) // .25 mini 9/14
				return true;
			if (coast > 20) // if coasting complete stop
				return true;
			// If coasting i.e. PID has converged, re-adjust the setPoints based
			// upon yaw error
			if (coast > 2) {
				if (Math.abs((yawError)) < .25)
					return false;
				adjustSetPoint(-yawError);
				ignoreDataCount = 3;
				coast = 0;
			}
		} else {
			if (coast > 0) {
				coast--;
				if (coast == 0)
					return true;
				return false;
			}
			if (Math.abs(yawError) < 1.5) {
				return true;
			}
			// Pass thru zero stop
//			if (lastYawError * yawError < 0) {
//				RobotMap.driveSystemRightFront.changeControlMode(TalonControlMode.PercentVbus);
//				RobotMap.driveSystemLeftFront.changeControlMode(TalonControlMode.PercentVbus);
//				RobotMap.driveSystemRightFront.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
//				RobotMap.driveSystemLeftFront.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
//				if (coast == 0)
//					coast = 5;
//				return false;
//			}
			if (Math.abs(yawError) < 6 && !adjustA) {
				//adjustSetPoint(-yawError);
				adjustA = true;
			}
			if (Math.abs(yawError) < 3 && !adjustB) {
				//adjustSetPoint(-yawError);
				adjustB = true;
			}
			lastYawError = yawError;
//			if (Math.abs(rightErr) < 3 && Math.abs(leftErr) < 3) {
//				RobotMap.driveSystemRightFront.changeControlMode(TalonControlMode.PercentVbus);
//				RobotMap.driveSystemLeftFront.changeControlMode(TalonControlMode.PercentVbus);
//				RobotMap.driveSystemRightFront.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
//				RobotMap.driveSystemLeftFront.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
//				if (coast == 0)
//					coast = 5;
//				return false;
//			}
		}

		return false;
	}

	public void end() {
		double convTime = (new Date().getTime() - m_init.getTime());
		double yaw = Robot.ahrs.getYaw();
		Robot.logf("---- Complete TurnToSRXPid Duration: %.2f Req Yaw:%.2f Yaw:%.2f Yaw Err:%.2f ---%n", convTime,
				m_degrees, yaw, yaw - m_degrees);

		SmartDashboard.putNumber("Time to Turn", convTime);
		SmartDashboard.putNumber("Turn to Err", yaw - m_degrees);

		// Change the HW back to driving with the joy sticks
		RobotMap.driveSystemRightFront.changeControlMode(TalonControlMode.PercentVbus);
		RobotMap.driveSystemLeftFront.changeControlMode(TalonControlMode.PercentVbus);
		RobotMap.driveSystemRightFront.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		RobotMap.driveSystemLeftFront.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		Robot.driveSystem.drivePercent(0, 0);
		if (Robot.conf.brakeMode)
			Robot.setBrakeMode(false);
		Robot.navXPin9TurningSRXPid.set(false);
	}

	public void interrupted() {
		end();
	}

	public void talonInit(CANTalon talon, SIDE side) {
		// choose the sensor and sensor direction
		talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);

		// Set Talon's for reverse sensing, needed to get the PID to converge
		// See what it is the for big robots and re-wire Mini-3930 or Mini-8700
		// Pin 8 is for the Right Side and Pin 9 is for the Left Side
		// If Right Side does not converge then invert Pin 8.
		// If Left Side does not converge then invert Pin 9.
		// 1337 Pin 8 High, Pin 9 Low
		// 3930 Pin 8 High, Pin 9 High
		// 3932 Pin 8 Low, Pin 9 Low

		if (side == SIDE.Left && Robot.pin9.get())
			talon.reverseSensor(true);
		if (side == SIDE.Right && Robot.pin8.get())
			talon.reverseSensor(true);

		/* set the peak and nominal outputs, 12V means full */
		talon.configNominalOutputVoltage(Robot.conf.minVoltage, -Robot.conf.minVoltage);
		talon.configPeakOutputVoltage(+12f, -12f);

		/*
		 * set the allowable closed-loop error, Closed-Loop output will be
		 * neutral within this range. See Table in Section 17.2.1 for native
		 * units per rotation.
		 */
		talon.setAllowableClosedLoopErr(0); /* always zero?? */

		/* set closed loop gains in slot0 */
		talon.setProfile(0);
		talon.setP(Robot.conf.pid[0]);
		talon.setI(Robot.conf.pid[1]);
		talon.setD(Robot.conf.pid[2]);
		talon.setF(Robot.conf.f);
		talon.setIZone(Robot.conf.iZone);
		talon.setVoltageRampRate(Robot.conf.voltageRampRate);
	}

	// Create Misc data for a side
	public String miscData(CANTalon talon, SIDE side) {
		return String.format("%s P:%.1f En:%d V:%.2f C:%.2f Err:%d", side.name(), talon.getPosition(),
				talon.getEncPosition(), talon.getOutputVoltage(), talon.getOutputCurrent(), talon.getClosedLoopError());
	}

	public void setRotations(CANTalon talon, double rotations) {
		talon.changeControlMode(TalonControlMode.Position);
		talon.set(rotations);
	}

	// Used to update the set points on the fly
	public void adjustSetPoint(double yawError) {
		double rightPos = RobotMap.driveSystemRightFront.getPosition();
		double leftPos = RobotMap.driveSystemLeftFront.getPosition();
		double deltaTicks = -yawError * Robot.conf.rotateFactor;
		RobotMap.driveSystemRightFront.set(rightPos + deltaTicks);
		RobotMap.driveSystemLeftFront.set(leftPos + deltaTicks);
		Robot.logf("adjustSetPoint ticks:%.2f RP:%.0f LP:%.0f%n", deltaTicks, rightPos, leftPos);
	}
}