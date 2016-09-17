package org.usfirst.frc3932.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import java.util.Date;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.Robot.ROBOTTYPES;
import org.usfirst.frc3932.Robot.RunningAverage;

public class MoveToTargetSRXPid extends Command {

	private DriveStraight driving;
	private Robot.RunningAverage avgError;

	private int Samples = 10; // 10 was good for practice robot
	private double MaxError = .2; // .4 was good practice robot

	private double m_Timeout = 3;

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
	private double magicDistance = 6;
	private TurnToSRXPid turnTo;

	public MoveToTargetSRXPid(double timeout) {
		requires(Robot.driveSystem);
		m_Timeout = timeout;
		driving = new DriveStraight(0, 0);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		m_MoveToInit = new Date();
		vision = Robot.vision;
		Robot.logf("++++++++++ MoveToTargetSRXPid Started  angle:%.2f +++++++++%n", vision.angle);
		avgError = new Robot.RunningAverage(Samples);
		count = 0;
		state = State.TARGETING;
		turnTo = new TurnToSRXPid(vision.angle, 2, true);
		turnTo.initialize();
		turnTo.adjustSetPoint(vision.angle);
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
		turnTo.end();
		Robot.setBrakeMode(false);
		Robot.log("------ MoveToTargetSRXPid Ended time:" + (new Date().getTime() - m_MoveToInit.getTime()));
	}

	protected void interrupted() {
		end();
	}

	protected boolean targeting() {
		double angle;
		if ((count % 30) == 0) {
			if (!vision.getTarget()) {
				Robot.logf("No target len:%d angle:%.2f solidity[0]:%.2f time:%d%n", vision.length, vision.angle,
						vision.solidity, elaspedTime);
				Robot.driveSystem.drivePercent(0, 0);
				return false;
			}
			angle = vision.angle;
			Robot.logf(
					"new angle Len:%d x:%.2f area: %.2f solidity:%.4f Yaw:%.2f Dist:%.2f Angle:%.2f Time:%d%n",
					vision.length, vision.centerX, vision.area, vision.solidity, yaw, lidar, angle, 
					elaspedTime);

			lastYaw = yaw;
			lastAngle = angle;
		} else {
			angle = (lastYaw - yaw) + lastAngle;
			Robot.logf("old angle Yaw:%.2f Dist:%.2f Angle:%.2f Time:%d%n", yaw, lidar, angle, 
					elaspedTime);
			return turnTo.isFinished();
		}
		avgError.putNew(Math.abs(angle));
		if (Math.abs(avgError.getAverage()) < MaxError) {
			SmartDashboard.putNumber("Time to Target", elaspedTime);
			return true;
		}
		return false;
	}
}
