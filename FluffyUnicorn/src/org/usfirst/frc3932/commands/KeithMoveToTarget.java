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
import org.usfirst.frc3932.Robot.RunningAverage;

/**
 *
 */
public class KeithMoveToTarget extends Command {

	private Robot.RunningAverage avgError;
	private DriveStraight driving;

	private int Samples = 10; // 10 was good for practice robot
	private double MaxError = .2; // .4 was good practice robot

	private final double miniP[][] = { { 180, .5 }, { 20, .3 }, { 10, .2 }, { 5, .15 }, { 3, .11 }, { 2, .08 },
			{ 1, .06 }, { 0, 0 } };
	private final double compP[][] = { { 180, .4 }, { 20, .3 }, { 10, .25 }, { 5, .22 }, { 3, .20 }, { 2, .19 },
			{ 1, .175 }, { .75, .170 }, { 0, .17 } };
	private final double compGoodBatteryP[][] = { { 180, .4 }, { 20, .3 }, { 10, .22 }, { 5, .18 }, { 3, .16 },
			{ 2, .15 }, { 1, .14 }, { .75, .14 }, { 0, .14 } };
	private double activeP[][] = compGoodBatteryP;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// private double distance;
	private double m_Timeout = 5;

	private double lastYaw = 0;
	private double lastAngle = 0;
	private Date m_MoveToInit;
	private int count = 0;
	private static Vision vision;
	private double yaw;
	private double lidar;
	private long elaspedTime;

	private enum State {
		IDLE, TARGETING, MOVING, FINETARGET
	};

	private State state = State.IDLE;
	private double magicDistance = 8;
	private DriveStraight Move;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public KeithMoveToTarget(double timeout) {

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
		m_MoveToInit = new Date();
		Robot.log("++++++++++ Keith MoveToTarget Started");
		if (Robot.robotType == ROBOTTYPES.MINI)
			activeP = miniP;
		Robot.setBrakeMode(true);
		avgError = new Robot.RunningAverage(Samples);
		vision = Robot.vision;
		count = 0;
		state = State.TARGETING;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		elaspedTime = new Date().getTime() - m_MoveToInit.getTime();
		if (elaspedTime > m_Timeout * 1000)
			return true;

		lidar = Robot.rangefinder.getDistance() / (12 * 2.54);
		yaw = Robot.ahrs.getYaw();
		count++;
		if (state == State.TARGETING) {
			if (targeting()) {
				state = State.MOVING;
				driving.setup(lidar - magicDistance, .5);
				driving.initialize();
			}
		}
		if (state == State.MOVING) {
			if (driving.isFinished()) {
				state = State.FINETARGET;
				count = 0;
				driving.end();
			}
		}
		if (state == State.FINETARGET) {
			if (targeting()) {
				return true;
			}
		}
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.driveSystem.drivePercent(0, 0);
		Robot.setBrakeMode(false);
		Robot.log("---------- Keith MoveToTarget Ended time:" + (new Date().getTime() - m_MoveToInit.getTime()));
	}

	protected void interrupted() {
		end();
	}

	protected boolean targeting() {
		double output;
		double angle;
		if ((count % 30) == 0) {
			if (!vision.getTarget()) {
				Robot.logf("No target len:%d angle:%.2f solidity[0]:%.2f time:%d%n", vision.length, vision.angle,
						vision.solidity, elaspedTime);
				Robot.driveSystem.drivePercent(0, 0);
				return false;
			}
			angle = vision.angle;
			output = getOutput(angle);
			Robot.logf(
					"new angle Len:%d x:%.2f area: %.2f solidity:%.4f Yaw:%.2f Dist:%.2f Angle:%.2f Output:%.2f Time:%d%n",
					vision.length, vision.centerX, vision.area, vision.solidity, yaw, lidar, angle, output,
					elaspedTime);
			Robot.driveSystem.drivePercent(-output, output);
			lastYaw = yaw;
			lastAngle = angle;
		} else {
			angle = (lastYaw - yaw) + lastAngle;
			output = getOutput(angle);
			Robot.logf("old angle Yaw:%.2f Dist:%.2f Angle:%.2f Output:%.2f Time:%d%n", yaw, lidar, angle, output,
					elaspedTime);
			Robot.driveSystem.drivePercent(-output, output);
		}

		avgError.putNew(Math.abs(angle));
		if (Math.abs(avgError.getAverage()) < MaxError) {
			SmartDashboard.putNumber("Time to Target", elaspedTime);
			return true;
		}
		return false;
	}

	protected double getOutput(double error) {
		int i = 0;
		while (i < activeP.length && activeP[i][0] >= Math.abs(error)) {
			i++;
		}
		i = (i == 0 ? 1 : i);
		return error > 0 ? activeP[i - 1][1] : -activeP[i - 1][1];

	}
}
