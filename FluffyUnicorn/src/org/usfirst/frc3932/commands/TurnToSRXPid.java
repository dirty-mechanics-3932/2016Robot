package org.usfirst.frc3932.commands;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.RobotMap;
import org.usfirst.frc3932.Robot.ROBOTTYPES;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
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
	double startingYaw;
	int coast = 0;
	public Config conf;

	double rightPosInitial;
	double leftPosInitial;
	double lastYawError;
	
	

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

	public TurnToSRXPid(double degrees, double timeout, double distance) {
		requires(Robot.driveSystem);
		m_degrees = degrees;
		m_timeout = timeout;
		m_distance = distance;
	}

	public void initialize() {
		Robot.driveSystem.resetEncoders();
		m_init = new Date();
		conf = new Config();
		startingYaw = Robot.ahrs.getYaw();
		double rotations = (m_degrees - startingYaw) * conf.rotateFactor;
		talonInit(RobotMap.driveSystemLeftFront, SIDE.Left);
		talonInit(RobotMap.driveSystemRightFront, SIDE.Right);
		rightPosInitial = RobotMap.driveSystemRightFront.getPosition();
		leftPosInitial = RobotMap.driveSystemLeftFront.getPosition();
		
		rightPosInitial = 0;
		leftPosInitial = 0;
		
		Robot.logf("+++++ Init TurnToSRXPID Degrees: %.2f Timeout:%.2f Mode:%s Yaw:%.2f Rotations:%.2f  +++++%n",
				m_degrees, m_timeout, RobotMap.driveSystemLeftFront.getControlMode().name(), startingYaw, rotations);
		Robot.logf("RP:%.2f LP:%.2f Config: %s%n", rightPosInitial, leftPosInitial, conf.currentConfig());
		setRotations(RobotMap.driveSystemRightFront, rightPosInitial + rotations + m_distance * Robot.TICKS_PER_FOOT);
		setRotations(RobotMap.driveSystemLeftFront, leftPosInitial + rotations - m_distance * Robot.TICKS_PER_FOOT);
		coast = 0;
		ignoreDataCount = 3; // Set to ignore the results for 3 passes
		lastYawError = rotations;
		if (conf.brakeMode) // Turn on break mode if parameter is set
			Robot.setBrakeMode(true);
	}

	public void execute() {
	}

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
		// If error is in range coast for a while
		if (Math.abs(leftErr) < conf.maxError && Math.abs(rightErr) < conf.maxError) {
			coast++;
		}
		// Pass thru zero stop
		if (lastYawError * yawError < 0)
			return true;
		lastYawError = yawError;
		// Stop when error is in range
		if (Math.abs((yawError)) < .25)
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
		if (conf.brakeMode)
			Robot.setBrakeMode(false);
	}

	public void interrupted() {
		end();
	}

	public void talonInit(CANTalon talon, SIDE side) {
		// choose the sensor and sensor direction
		talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);

		// Set Talon's for reverse sensing, needed to get the PID to converge
		// See what it is the for big robots and re-wire Mini-3930 or Mini-8700
		// 3930 Both Right and Left On confirmed by Keith 9/10/2016
		if (side == SIDE.Left && Robot.pin9.get()) // Needed for Mini-3930
		 	talon.reverseSensor(true);
		if (side == SIDE.Right && Robot.pin8.get()) // Needed for Mini-8700
			talon.reverseSensor(true);

		/* set the peak and nominal outputs, 12V means full */
		talon.configNominalOutputVoltage(conf.minVoltage, -conf.minVoltage);
		talon.configPeakOutputVoltage(+12f, -12f);

		/*
		 * set the allowable closed-loop error, Closed-Loop output will be
		 * neutral within this range. See Table in Section 17.2.1 for native
		 * units per rotation.
		 */
		talon.setAllowableClosedLoopErr(0); /* always zero?? */

		/* set closed loop gains in slot0 */
		talon.setProfile(0);
		talon.setP(conf.pid[0]);
		talon.setI(conf.pid[1]);
		talon.setD(conf.pid[2]);
		talon.setF(conf.f);
		talon.setIZone(conf.iZone);
		talon.setVoltageRampRate(conf.voltageRampRate);
	}

	public String miscData(CANTalon talon, SIDE side) { // Create Misc data for
														// a side
		return String.format("%s P:%.1f En:%d V:%.2f C:%.2f Err:%d", side.name(), talon.getPosition(),
				talon.getEncPosition(), talon.getOutputVoltage(), talon.getOutputCurrent(), talon.getClosedLoopError());
	}

	public void setRotations(CANTalon talon, double rotations) {
		talon.changeControlMode(TalonControlMode.Position);
		talon.set(rotations);
	}

	// Used to update the set points on the fly
	void adjustSetPoint(double yawError) {
		double rightPos = RobotMap.driveSystemRightFront.getPosition();
		double leftPos = RobotMap.driveSystemLeftFront.getPosition();
		double deltaTicks = -yawError * conf.rotateFactor;
		RobotMap.driveSystemRightFront.set(rightPos + deltaTicks);
		RobotMap.driveSystemLeftFront.set(leftPos + deltaTicks);
		Robot.logf("adjustSetPoint ticks:%.2f RP:%.0f LP:%.0f%n", deltaTicks, rightPos, leftPos);
	}

	public class Config {
		// Competition Robot -- default values
		public double pid[] = { 2.5, 0, 0 };
		public double f = 0;
		public int iZone = 0;
		public double rotateFactor = 35;
		public double maxError = 5;
		public double minVoltage = 4;
		public boolean brakeMode = true;
		public double voltageRampRate = 0;

		public Config() {
			if (Robot.robotType == ROBOTTYPES.MINI) {
				pid[0] = 6;
				pid[1] = 0;
				pid[2] = 60;
				f = 0;
				rotateFactor = 14.2; // Previous 14.8
				maxError = 1.5;
				minVoltage = 2;
				brakeMode = false;
				voltageRampRate = 0;
			}
		}

		// Create a string that shows the configuration data
		public String currentConfig() {
			return String.format(
					"P:%.2f I:%.2f D:%.2f F:%.2f rotF:%.2f maxErr:%.2f Min Volt:%.2f Brake:%s VRamp:%.2f Type:%s",
					pid[0], pid[1], pid[2], f, rotateFactor, maxError, minVoltage, (brakeMode ? "True" : "False"),
					voltageRampRate, Robot.robotType.name());
		}
	}
}