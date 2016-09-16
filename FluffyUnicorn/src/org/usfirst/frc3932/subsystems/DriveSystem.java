// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc3932.subsystems;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.RobotMap;
import org.usfirst.frc3932.commands.*;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotDrive;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveSystem extends Subsystem {

	private static final int feedBackPoorManPID = 20;
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS
	// 360 tics per rotation, 84 in per second, 4.5 revolutions per second for
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	private final CANTalon leftFront = RobotMap.driveSystemLeftFront;
	private final CANTalon leftRear = RobotMap.driveSystemLeftRear;
	private final CANTalon rightFront = RobotMap.driveSystemRightFront;
	private final CANTalon rightRear = RobotMap.driveSystemRightRear;
	private final RobotDrive tankDrive = RobotMap.driveSystemTankDrive;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	private static final double FAST_SCALE = 1;
	private static final double SLOW_SCALE = 0.5;
	private double scale = FAST_SCALE;

	private double initYaw = 0;
	private boolean isYawInitialized = false;

	public DriveSystem() {
		super();
		leftRear.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightRear.changeControlMode(CANTalon.TalonControlMode.Follower);
	}

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// Set the default command for a subsystem here.

		setDefaultCommand(new DriveTeleop());
		Robot.log("Start up Drive System");
		tankDrive.setSafetyEnabled(false);
	}

	public void drive() {

		double left = 0, right = 0;

		if (!goForward.isLeftTriggerPressed) {
			GenericHID leftStick = Robot.oi.driverLeft;
			GenericHID rightStick = Robot.oi.driverRight;

			left = leftStick.getY();
			right = rightStick.getY();

			initYaw = Robot.ahrs.getAngle();
			isYawInitialized = true;
			if (Math.abs(left) >.06 || Math.abs(right) > 0.06) {
				Robot.logf(
						"Drive Normal Yaw:%.2f LD:%.2f RD:%.2f " + "LP:%.0f RP:%.0f LS:%.0f RS:%.0f LC:%.2f RC:%.2f%n",
						initYaw, left, right, RobotMap.driveSystemLeftFront.getPosition(),
						RobotMap.driveSystemRightFront.getPosition(), RobotMap.driveSystemLeftFront.getSpeed(),
						RobotMap.driveSystemRightFront.getSpeed(), RobotMap.driveSystemLeftFront.getOutputCurrent(),
						RobotMap.driveSystemRightFront.getOutputCurrent());
			}

		}

		else { // Left Trigger is pressed, which means to drive straight, use a
				// poor mans PID to keep on course

			GenericHID leftStick = Robot.oi.driverRight;
			left = leftStick.getY();
			right = leftStick.getY();

			double currentYaw = Robot.ahrs.getAngle();
			double deltaYaw = currentYaw - initYaw;
			double factorYaw = deltaYaw / feedBackPoorManPID;

			// recalculate left or right wheel if angle changed

			if ((isYawInitialized) && (deltaYaw != 0)) {
				if (left < 0) {
					if (deltaYaw < 0) {

						// compensate for left drive
						left = left * (1 - factorYaw);
					} else {
						// compensate for right drive
						right = right * (1 + factorYaw);
					}
				} else {
					if (deltaYaw < 0) {

						// compensate for left drive
						left = left * (1 + factorYaw);
					} else {
						// compensate for right drive
						right = right * (1 - factorYaw);

					}
				}

				Robot.logf(
						"leftTrig initYaw:%.2f Yaw:%.2f LD:%.2f RD:%.2f "
								+ "LP:%.0f RP:%.0f LS:%.0f RS:%.0f LV:%.2f RV:%.2f%n",
						initYaw, currentYaw, left, right, RobotMap.driveSystemLeftFront.getPosition(),
						RobotMap.driveSystemRightFront.getPosition(), RobotMap.driveSystemLeftFront.getSpeed(),
						RobotMap.driveSystemRightFront.getSpeed(), RobotMap.driveSystemLeftFront.getBusVoltage(),
						RobotMap.driveSystemRightFront.getBusVoltage());
			}
		}

		drivePercent(left * scale, right * scale);
	}

	public void drivePercent(double leftPercent, double rightPercent) {
		driveLeftPercent(leftPercent);
		driveRightPercent(rightPercent); // Changed to - for mini
	}

	public void driveRightPercent(double rightPercent) {
		rightFront.set(rightPercent);
		rightRear.set(rightFront.getDeviceID());
	}

	public void driveLeftPercent(double leftPercent) {
		leftFront.set(leftPercent);
		leftRear.set(leftFront.getDeviceID());
	}

	public void setPercent(CANTalon masterTalon, CANTalon slaveTalon, double leftPercent) {
		masterTalon.set(leftPercent);
		slaveTalon.set(masterTalon.getDeviceID());
	}

	public void goFast() {
		scale = FAST_SCALE;
	}

	public void goSlow() {
		scale = SLOW_SCALE;
	}

	public void resetEncoders() { // Reset encoders take a few Milliseconds to
									// complete could be as long as 100 MS
		leftFront.setEncPosition(0);
		rightFront.setEncPosition(0);
	}
}
