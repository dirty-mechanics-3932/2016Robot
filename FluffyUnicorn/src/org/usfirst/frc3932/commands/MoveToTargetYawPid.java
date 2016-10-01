//MoveToTarget.java// RobotBuilder Version: 2.0
//
//This file was generated by RobotBuilder. It contains sections of
//code that are automatically generated and assigned by robotbuilder.
//These sections will be updated in the future when you export to
//Java from RobotBuilder. Do not put any code or make any change in
//the blocks indicating autogenerated code or it will be lost on an
//update. Deleting the comments indicating the section will prevent
//it from being updated in the future.

package org.usfirst.frc3932.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import java.util.Date;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.RobotMap;
import org.usfirst.frc3932.Robot.ROBOTTYPES;

/**
 *
 */
public class MoveToTargetYawPid extends Command {

	private Robot.RunningAverage avgError;
	private DriveStraight driving;
	private TurnToOrig turnTo;

	private int Samples = 10; // 10 was good for practice robot

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// private double distance;
	private double m_timeout = 5;
	private boolean m_noDrive = false;

	private Date moveToInit;
	private static Vision vision;
	private double yaw;
	private double lidar;
	private long elaspedTime;
	private int waitCount = 0;
	private int retargets = 0;

	private enum State {
		INITIAL, TARGETING, MOVING, WAIT, FINALTARGETING, FINALCOAST, TURNAWAY
	};

	private String targetResult = "OK";
	private State state = State.INITIAL;
	private double magicDistance = 9.5; // Dave Keith 9/21
	// private DriveStraight Move;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public MoveToTargetYawPid(double timeout) {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		requires(Robot.driveSystem);
		m_timeout = timeout;
		m_noDrive = false; //

	}

	// Overload call when you do not want to drive forward / backward just
	// target
	public MoveToTargetYawPid(double timeout, boolean noDrive) {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		requires(Robot.driveSystem);
		m_timeout = timeout;
		m_noDrive = noDrive;

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		moveToInit = new Date();
		if (Robot.robotType == ROBOTTYPES.MINI) {
			magicDistance = 6;
		}
		avgError = new Robot.RunningAverage(Samples);
		vision = Robot.vision;
		Robot.logf("+++++ MoveToTarget Yaw Pid Started %s noDrive:%b +++++%n", visionParms(), m_noDrive);
		state = State.TARGETING;
		SmartDashboard.putString("Target Result", "Targeting");
		waitCount = 0;
		retargets = 0;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		elaspedTime = new Date().getTime() - moveToInit.getTime();
		boolean visionResult = vision.getTarget();
		// Return if timeout
		if (elaspedTime > m_timeout * 1000) {
			Robot.logf("***** MoveToTarget Timed Out after %.2f seconds%n", m_timeout);
			targetResult = "Time Out " + m_timeout + " seconds";
			return true;
		}
		// Verify Lidar is working
		lidar = Robot.rangefinder.getDistanceFeet();
		if (lidar < 2 || lidar > 30) {
			Robot.logf("***** MoveToTarget Will not target Lidar:%.2f *****%n", lidar);
			targetResult = "Lidar out of Range " + String.format("%.2f", lidar);
			return true;
		}
		// Verify that Vision is working
		if (vision.angle > 35 || vision.angle < -35) {
			targetResult = "Vision Angle out of Range " + String.format(".2f", vision.angle);
			Robot.logf("***** MoveToTarget Will not target Vision:%.2f *****%n", vision.angle);
			return true;
		}
		yaw = Robot.ahrs.getYaw();
		if (waitCount > 0) {
			waitCount--;
			Robot.logf("Wait:%d yaw:%.2f visionResult:%b %s %s %s%n", waitCount, yaw, visionResult, visionParms(), Robot.motorsPosition(),
					Robot.motorsCurrent());
			return false;
		}
		// Do an action based upon state
		if (state == State.TARGETING) {
			if (targeting()) {
				if (m_noDrive || Math.abs(lidar - magicDistance) < .25) {
					Robot.logf("Robot is at the correct distanace %.2f visionResult:%b action complete%n", lidar, visionResult);
					// Check the angle if OK do Final Coast
					if (Math.abs(vision.angle) < .8  && visionResult) {
						state = State.FINALCOAST;
						waitCount = 30;
						return false;
					} else {
						state = State.FINALTARGETING;
						waitCount = 30;
						return false;
					}
				}
				state = State.MOVING;
				driving = new DriveStraight(0, 0);
				driving.setup(lidar - magicDistance, .5);
				driving.initialize();
				return false;
			}
		}
		if (state == State.MOVING) {
			if (driving.isFinished()) {
				state = State.FINALTARGETING;
				waitCount = 30;
				driving.end();
				driving = null;
				return false;
			}
		}
		if (state == State.FINALTARGETING) {
			if (targeting()) {
				state = State.FINALCOAST;
				waitCount = 30;
				return false;
			}
		}

		if (state == State.FINALCOAST) {
			Robot.logf("Final Wait %s yaw:%.2f retargets:%d %s %n", visionParms(), yaw, retargets, Robot.motorDataAll());
			if (Math.abs(vision.angle) < 1d && visionResult) {
				targetResult = "Final Coast angle GOOD  " + String.format("%.2f", vision.angle);
				return true;
			} else {
				targetResult = "************ Final Coast angle BAD - Will Try again "
						+ String.format("%.2f", vision.angle);
				state = State.FINALTARGETING;
				waitCount = 30;
				retargets++;
				return false;
			}
		}

		if (state == State.TURNAWAY) {
			if (turnTo.isFinished()) {
				turnTo.end();
				turnTo = null;
				state = State.FINALTARGETING;
				return false;
			}
		}
		return false;
	}

	// Called once after isFinished returns true
	// Clean up anything that may be running
	protected void end() {
		Double shootTime = Robot.shootSpinup - ((new Date().getTime() - moveToInit.getTime()) / 1000d);
		shootTime = shootTime > 0 ? shootTime : 0;
		Robot.shootTime = shootTime;
		Robot.logf("------ MoveToTarget Yaw Pid Ended time:%d retargets:%d turnTo:%s driving:%s result: %s Remaing Shoot Time:%.2f%n",
				(new Date().getTime() - moveToInit.getTime()), retargets, (turnTo != null ? "Turning" : "Null"),
				(driving != null ? "Driving" : "Null"), targetResult, shootTime);
		SmartDashboard.putString("Target Result", targetResult);
		// Fix bug that allow turn or driving after targeting is complete
		if (turnTo != null) {
			turnTo.end();
			turnTo = null;
		}
		if (driving != null) {
			driving.end();
			driving = null;
		}
		Robot.driveSystem.drivePercent(0, 0);
		Robot.setBrakeMode(false);

	}

	protected void interrupted() {
		Robot.logf("????? MoveToTarget Interupted ?????%n");
		end();
	}

	// protected boolean turnAway(double angle) {
	// if (turnTo != null) {
	// Robot.logf("????? Error -- turnAway turning still active%n");
	// return false;
	// }
	// if (driving != null) {
	// Robot.logf("????? Error -- turnAway driving still active%n");
	// return false;
	// }
	// angle = vision.angle + yaw;
	// Robot.logf("********** turnAway TurnTo angle:%.2f %s%n", angle,
	// visionParms());
	// turnTo = new TurnToOrig(angle, 2);
	// turnTo.initialize();
	// return true;
	// }

	protected boolean targeting() {
		double angle;
		if (turnTo != null) {
			if (turnTo.isFinished()) {
				turnTo.end();
				turnTo = null;
				return true;
			} else {
				return false;
			}
		} else {
			if (Math.abs(vision.angle) < .8) {
				Robot.logf("No turn robot at vision angle %.2f%n", vision.angle);
				return true;
			}
			angle = vision.angle + yaw;
			Robot.logf("Targeting TurnTo %s%n", visionParms());
			turnTo = new TurnToOrig(angle, 2);
			turnTo.initialize();
		}
		return false;
	}

	String visionParms() {
		return String.format("Vision angle:%.2f cx:%.0f ar:%.2f sd:%.2f hei:%.0f wid:%.0f lidar:%.2f", vision.angle,
				vision.centerX, vision.area, vision.solidity, vision.height, vision.width,
				Robot.rangefinder.getDistanceFeet());
	}

}
