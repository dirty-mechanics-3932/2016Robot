// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc3932.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.command.Command;

import java.util.Date;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.RobotMap;
import org.usfirst.frc3932.commands.TurnToSRXPid.SIDE;

/**
 *
 */

public class ShooterSpeed extends Command {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR

	Preferences prefs;

	private CANTalon leftWheel = RobotMap.shooterWheelsLeftWheel;
	private CANTalon rightWheel = RobotMap.shooterWheelsRightWheel;
	private Double m_speed;
	private Double m_timeout;
	public double P = 0.08d; // was .1097 should have been .22
	public double I = 0; // was 0
	public double D = 0; // was 0
	public double F = 0.0265d; // was .22 should have been .1097
	private long initTime;
	private boolean goodPid = true;
	private double fRight = 0;
	private double fLeft = 0;
	private int count = 0;

	public ShooterSpeed(double speed, double timeout) {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		m_speed = speed;
		m_timeout = timeout;
		requires(Robot.shooterWheels);

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		prefs = Preferences.getInstance();
		m_speed = prefs.getDouble("Speed", m_speed);

		if (!goodPid) {
			P = prefs.getDouble("P", P);
			I = prefs.getDouble("I", I);
			D = prefs.getDouble("D", D);
			F = prefs.getDouble("F", F);
			// maxOutput = prefs.getDouble("maxO", maxOutput);
		} else {
			if (Robot.robotType == Robot.ROBOTTYPES.MINI) {
				P = .195;
				I = 0;
				D = 10;
				F = .03;
			}
		}

		if (Robot.robotType != Robot.ROBOTTYPES.MINI)
			talonInit(rightWheel, SIDE.Right, m_speed);
		talonInit(leftWheel, SIDE.Left, m_speed);
		fRight = F;
		fLeft = F;
		initTime = new Date().getTime();
		Robot.logf("Start Shooter Motor speed:%.2f timeout:%.2f%n", m_speed, m_timeout);
		count = 0;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		// setSpeed(leftWheel);
		count++;
		if (Robot.robotType == Robot.ROBOTTYPES.MINI) {
			// adjustF(leftWheel, SIDE.Left);
			Robot.logf("Shooter %s F:%.4f count:%d%n", miscData(leftWheel, SIDE.Left), fLeft, count);

		} else {
			adjustF(leftWheel, SIDE.Left);
			adjustF(rightWheel, SIDE.Right);
			Robot.logf("Shooter %s F:%.4f %s F:%.4f count:%d%n", miscData(rightWheel, SIDE.Right), fRight,
					miscData(leftWheel, SIDE.Left), fLeft, count);

		}
		double convTime = (new Date().getTime() - initTime);
		if (convTime > m_timeout * 1000)
			return true;
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		talonStop(rightWheel);
		talonStop(leftWheel);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.log("ShooterSpeedInterupted");
		end();
	}
	
	public boolean speedOk(double tolerance) {
		double leftDelta = Math.abs(leftWheel.getSpeed() - m_speed);
		double rightDelta = leftDelta; // Set for mini with only one wheel
		if(Robot.robotType != Robot.ROBOTTYPES.MINI)
			rightDelta= Math.abs(rightWheel.getSpeed() - m_speed);
		Robot.logf("shooter speedOk leftD:%.0f rightD:%.0f %n", leftDelta, rightDelta);
		return (rightDelta < tolerance) && (leftDelta < tolerance);
	}

	private void adjustF(CANTalon talon, SIDE side) {
		if (count < 100)
			return;
		double err = talon.getClosedLoopError();
		if (Math.abs(err) < 20)
			return;
		double lastF;
		lastF = (side == SIDE.Left) ? fLeft : fRight;
		double adjust = .001d;
		if (err < 0)
			adjust *= -1d;
		if (side == SIDE.Left) {
			lastF *= 1d + adjust;
			fLeft = lastF;
		} else {
			lastF *= 1d - adjust;
			fRight = lastF;
		}
		talon.setF(lastF);

		// if (err > 0)
		// lastF *= 1d + adjust;
		// else
		// lastF *= 1d - adjust;
		//
		// talon.setF(lastF);
		//
		// if (side == SIDE.Left)
		// fLeft = lastF;
		// else
		// fRight = lastF;

	}

	private void talonInit(CANTalon talon, SIDE side, double speed) {
		Robot.logf("+++++++++++++++++++++ Shooter Init %s P:%.4f I:%.4f D:%.4f F:%.4f Speed:%.0f%n", side.name(), P, I,
				D, F, speed);
		talon.setProfile(0);
		// choose the sensor and sensor direction
		talon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		/* set the peak and nominal outputs, 12V means full */
		talon.configNominalOutputVoltage(+0f, +0f);
		talon.configPeakOutputVoltage(+12f, -12f);

		talon.reverseSensor(false);

		talon.reverseOutput(false);

		/*
		 * set the allowable closed-loop error, Closed-Loop output will be
		 * neutral within this range. See Table in Section 17.2.1 for native
		 * units per rotation.
		 */

		/* set closed loop gains in slot0 */
		// talon.setProfile(0);
		talon.setVoltageRampRate(16);
		talon.setAllowableClosedLoopErr(0);
		talon.setP(P);
		talon.setI(I);
		talon.setD(D);
		talon.setF(F);
		talon.changeControlMode(TalonControlMode.Speed);
		talon.set(speed);
		talon.setIZone(0);
		talon.enableBrakeMode(true);
	}

	public void talonStop(CANTalon talon) {
		talon.set(0);
		talon.changeControlMode(TalonControlMode.PercentVbus);
		talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		// talon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
	}

	public void setSpeed(CANTalon talon) {
		talon.changeControlMode(TalonControlMode.Speed);
		talon.set(m_speed);
	}

	// Create Misc data for a side
	public String miscData(CANTalon talon, SIDE side) {
		return String.format("%s P:%.0f En:%d Sp:%.2f V:%.2f C:%.2f Err:%d", side.name(), talon.getPosition(),
				talon.getEncPosition(), talon.getSpeed(), talon.getOutputVoltage(), talon.getOutputCurrent(),
				talon.getClosedLoopError());
	}

}
