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
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Date;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.RobotMap;
import org.usfirst.frc3932.commands.TurnToSRXPid.SIDE;
import org.usfirst.frc3932.pid.AhrsYawPIDSource;
import org.usfirst.frc3932.pid.DriveSystemRotatePIDOutput;

/**
 *
 */
public class TurnToSmall extends Command {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	private double m_degrees;
	Date turnToInit;
	private int count = 0;
	private int timeOut;
	private int wait = 0;
	private double l0;
	private double r0;
	private double yaw0;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public TurnToSmall(double degrees, double timeout) {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		m_degrees = degrees;

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		requires(Robot.driveSystem);

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
	}

	protected void initialize() {
		count = 0;
		wait = 0;
		turnToInit = new Date();
		timeOut = Math.abs((int) (m_degrees * 16));
		double turnSpeed = 0.3d;
		timeOut = 1;
		l0 = RobotMap.driveSystemLeftFront.getPosition();
		r0 = RobotMap.driveSystemRightFront.getPosition();
		yaw0 = Robot.ahrs.getYaw();
		Robot.logf("+++++ TurnToSmall init deg:%.2f yaw:%.2f speed:%.2f timeOut:%d%n", m_degrees, yaw0, turnSpeed,
				timeOut);
		if (m_degrees > 0)
			Robot.driveSystem.drivePercent(-turnSpeed, turnSpeed);
		else
			Robot.driveSystem.drivePercent(turnSpeed, -turnSpeed);
		Robot.setBrakeMode(true);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		count++;
		Robot.logf("TurnToSmall count:%d wait:%d yaw:%.2f left %s right %s%n", count, wait, Robot.ahrs.getYaw(),
				Robot.motorData(RobotMap.driveSystemLeftFront),
				Robot.motorData(RobotMap.driveSystemRightFront));
		if (count >= timeOut) {
			if (wait == 0) {
				Robot.driveSystem.drivePercent(0, 0);
				wait = count + 10;
				return false;
			} else {
				if (wait < count) {
					Robot.setBrakeMode(false);
					return true;
				}
			}
		}
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.driveSystem.drivePercent(0, 0);
		double yaw = Robot.ahrs.getYaw();
		Robot.logf("----- TurnToSmall end deg:%.2f dYaw:%.2f dL:%.0f dR:%.0f%n", m_degrees, yaw - yaw0,
				RobotMap.driveSystemLeftFront.getPosition() - l0, RobotMap.driveSystemRightFront.getPosition() - r0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}


}
