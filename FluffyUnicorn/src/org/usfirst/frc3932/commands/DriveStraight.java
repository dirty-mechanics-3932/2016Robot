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

import edu.wpi.first.wpilibj.PIDController;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Date;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.RobotMap;
import org.usfirst.frc3932.Robot.ROBOTTYPES;
import org.usfirst.frc3932.pid.AhrsYawPIDSource;
import org.usfirst.frc3932.pid.DriveSystemReversePIDOutput;
import org.usfirst.frc3932.pid.DriveSystemStraightPIDOutput;
import com.kauailabs.navx.frc.AHRS;

/**
 *
 */
public class DriveStraight extends Command {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	private double m_feet;
	private double m_speed;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	private PIDController controller;
	private static final double P = 0.025;
	private static final double I = 0;
	private static final double D = 0;

	private double a0 = 0; // Value of encoders at initialization
	private double b0 = 0; // Value of encoders at initialization
	private double lastD = 0;
	private Date DriveStraightInit;
	private double lidarStart;
	private int count; // Used to count number of passes
	private int waitCount;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public DriveStraight(double feet, double speed) {
		setup(feet, speed);
	}

	protected void setup(double feet, double speed) {
		m_feet = feet;
		m_speed = speed;

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		requires(Robot.driveSystem);

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

		PIDOutput out;
		if (feet < 0) {
			out = new DriveSystemReversePIDOutput(speed);
		} else {
			out = new DriveSystemStraightPIDOutput(speed);
		}
		controller = new PIDController(P, I, D, new AhrsYawPIDSource(), out);
		controller.setInputRange(-180, 180);
		controller.setContinuous();
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		// a0,b0 value of left and right encoders at initialization time.
		a0 = RobotMap.driveSystemLeftFront.getPosition();
		b0 = RobotMap.driveSystemRightFront.getPosition();
		lidarStart = Robot.rangefinder.getDistanceFeet();
		DriveStraightInit = new Date();
		controller.setSetpoint(Robot.ahrs.getYaw());
		Robot.logf("+++++ Drive Straight Init dist:%.2f speed:%.2f yaw:%.2f LP:%.0f RP:%.0f Lidar:%.2f navX%s%n",
				m_feet, m_speed, Robot.ahrs.getYaw(), a0, b0, lidarStart, Robot.ahrs.getFirmwareVersion());
		controller.enable();
		count = 0;
		waitCount = 0;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		count++;
		double a1 = RobotMap.driveSystemLeftFront.getPosition();
		double b1 = RobotMap.driveSystemRightFront.getPosition();
		double d = getDistance(a1, b1);
		if (Math.abs(lastD - d) < .0001 || true) { // Log only if moved
			Robot.logf(
					"DriveS: %s LP:%.0f RP:%.0f LS:%.1f RS:%.1f LC:%.2f RC:%.2f Yaw:%.2f Dist:%.2f Time:%.2f Lidar Travel:%.2f%n",
					(waitCount > 0) ? "Wait" : "Norm", RobotMap.driveSystemLeftFront.getPosition(),
					RobotMap.driveSystemRightFront.getPosition(), RobotMap.driveSystemLeftFront.getSpeed(),
					RobotMap.driveSystemRightFront.getSpeed(), RobotMap.driveSystemLeftFront.getOutputCurrent(),
					RobotMap.driveSystemRightFront.getOutputCurrent(), Robot.ahrs.getYaw(), d,
					(new Date().getTime() - DriveStraightInit.getTime()) / 1000d,
					Robot.rangefinder.getDistanceFeet() - lidarStart);
		}
		if (waitCount > 0) { // In coasting mode -- could wait until speed stops
								// before finishing
			if (count >= waitCount)
				return true;
			return false;
		}
		lastD = d;
		// Determine if the required distance has been achieved
		// if so set waitCount to wait for 8 passes until things have settled
		// down
		boolean isComplete;
		if (Robot.robotType == Robot.ROBOTTYPES.MINI) {
			if (m_feet > 0)
				isComplete = Math.abs(d) > (Math.abs(m_feet));
			else
				isComplete = Math.abs(d) > (Math.abs(m_feet));
		} else { // Big Robots coast more than mini
			if (m_feet > 0)
				isComplete = Math.abs(d) > (Math.abs(m_feet - 6d / 12d));
			else
				isComplete = Math.abs(d) > (Math.abs(m_feet + 6d / 12d));
		}
		if (isComplete) {
			controller.disable();
			if (Robot.conf.brakeMode) // Turn on break mode if parameter is set
				Robot.setBrakeMode(true);
			Robot.driveSystem.drivePercent(0, 0);
			waitCount = count + 20;
		}
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		double a1 = RobotMap.driveSystemLeftFront.getPosition();
		double b1 = RobotMap.driveSystemRightFront.getPosition();
		double lidarDist = (lidarStart - Robot.rangefinder.getDistanceFeet());
		SmartDashboard.putNumber("Lidar Delta", lidarDist);
		SmartDashboard.putNumber("Lidar Final", Robot.rangefinder.getDistanceFeet());
		Robot.logf(
				"----- Drive Straight End reqDist:%.2f actDist:%.2f lidar travel:%.2f LP:%.0f RP:%.0f DL:%.0f DR:%.0f Yaw:%.2f Time:%d%n",
				m_feet, getDistance(a1, b1), lidarDist, a1, b1, a1 - a0, b1 - b0, Robot.ahrs.getYaw(),
				(new Date().getTime() - DriveStraightInit.getTime()));
		if (controller != null) {
			controller.disable();
			controller.free();
		} else {
			Robot.logf("????? Drive Straight No Controller ?????%n");
		}
		if (Robot.conf.brakeMode) // Turn on break mode if parameter is set
			Robot.setBrakeMode(false);
		Robot.driveSystem.drivePercent(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.logf("????? Drive Straight Interupted ?????%n");
		end();
	}

	protected double getDistance(double a1, double b1) {
		return (Math.abs(a0 - a1) + Math.abs(b0 - b1)) / (2 * Robot.conf.ticksPerFoot);
	}

}
