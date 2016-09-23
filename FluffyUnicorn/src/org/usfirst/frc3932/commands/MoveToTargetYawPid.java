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
	private double m_Timeout = 5;

	private Date moveToInit;
	private int count = 0;
	private static Vision vision;
	private double yaw;
	private double lidar;
	private long elaspedTime;

	private int waitUntilCount = 0;

	private enum State {
		INITIAL, TARGETING, MOVING, WAIT, FINALTARGETING, FINALCOAST
	};

	private State state = State.INITIAL;
	private double magicDistance = 11.7; // Dave Keith 9/21
	// private DriveStraight Move;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public MoveToTargetYawPid(double timeout) {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		requires(Robot.driveSystem);
		m_Timeout = timeout;
		driving = new DriveStraight(0, 0);

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		moveToInit = new Date();
		if (Robot.robotType == ROBOTTYPES.MINI) {
			magicDistance = 6;
		}
		Robot.setBrakeMode(true);
		avgError = new Robot.RunningAverage(Samples);
		vision = Robot.vision;
		Robot.logf("+++++ MoveToTarget Yaw Pid Started %s +++++%n", visionParms());
		count = 0;
		state = State.TARGETING;

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		elaspedTime = new Date().getTime() - moveToInit.getTime();
		// Return if timeout
		if (elaspedTime > m_Timeout * 1000)
			return true;
		// Verify Lidar is working
		lidar = Robot.rangefinder.getDistance() / (12 * 2.54);
		if (lidar < 3 || lidar > 30) {
			Robot.logf("%n***** MoveToTarget Will not target *****%n");
			Robot.logf("***** Error:  Lidar Out of range:%.2f ****** %n%n", lidar);
			return true;
		}
		// Verify that Vision is working
		if (vision.angle > 35 || vision.angle < -35) {
			Robot.logf("%n***** MoveToTarget Will not target *****%n");
			Robot.logf("***** Error:  Vision Angle Out of range:%.2f ****** %n%n", vision.angle);
			return true;
		}
		yaw = Robot.ahrs.getYaw();
		count++;
		// Do an action based upon state
		if (state == State.TARGETING) {
			if (targeting()) {
				if (Math.abs(lidar - magicDistance) < .5) {
					Robot.logf("Robot is at the correct distanace %.2f Complete action complete%n",lidar);
					state = State.FINALCOAST;
					waitUntilCount = count + 50;  // Delay one second  since motors need spin up 
					return false;
				}
				state = State.MOVING;
				driving.setup(lidar - magicDistance, .5);
				driving.initialize();
			}
		}
		if (state == State.MOVING) {
			if (driving.isFinished()) {
				state = State.WAIT;
				waitUntilCount = count + 20;
				driving.end();
			}
		}

		if (state == State.WAIT) {
			Robot.logf("Waiting %s yaw:%.2f wait:%d%n", visionParms(), yaw, waitUntilCount - count);
			if (count > waitUntilCount) {
				state = State.FINALTARGETING;
			}
		}

		if (state == State.FINALTARGETING) {
			if (targeting()) {
				state = State.FINALCOAST;
				waitUntilCount = count + 30;
			}
		}

		if (state == State.FINALCOAST) {
			Robot.logf("Final Wait %s yaw:%.2f wait:%d%n", visionParms(), yaw, waitUntilCount - count);
			if (count > waitUntilCount) {
				return true;
			}
		}
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.driveSystem.drivePercent(0, 0);
		Robot.setBrakeMode(false);
		Robot.log("------ MoveToTarget Yaw Pid Ended time:" + (new Date().getTime() - moveToInit.getTime()));
	}

	protected void interrupted() {
		end();
	}

	protected boolean targeting() {
		double angle;
		if (turnTo != null) {
			if (turnTo.isFinished()) {
				turnTo.end();
				turnTo = null;
				Robot.logf("MoveToTarget Turn Complete yaw:%.2f %n", yaw);
				return true;
			} else {
				return false;
			}
		} else {
			if(Math.abs(vision.angle) < 1) {
				Robot.logf("No need to turn since robot at vision angle %.2f%n", vision.angle);
				return true;
			}
			angle = vision.angle + yaw;
			Robot.logf("Start TurnTo %s %n", visionParms());
			turnTo = new TurnToOrig(angle, 2);
			turnTo.initialize();
		}
		return false;
	}
	
	String visionParms() {
		return String.format("Vision angle:%.2f cx:%.0f ar:%.2f sd:%h:.2f %.0f w:%.0f", vision.angle, vision.centerX,
				vision.area, vision.solidity, vision.height, vision.width);
	}

}
