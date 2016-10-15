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

import edu.wpi.first.wpilibj.command.Command;

import java.util.Date;

import org.usfirst.frc3932.Robot;

/**
 *
 */
public class TargetAndShoot extends Command {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	private double m_timeout;
	private MoveToTargetYawPid targeting;
	private ShooterSpeed shooter;
	private int waitCount;
	private int count;
	private long initTime;

	private enum State {
		Targeting, WaitTargeting, WaitShooter, Shoot, Shooting
	};

	private State state = State.Targeting;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public TargetAndShoot() {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		requires(Robot.shooterWheels);
		m_timeout = 15;

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
	}
	
	public TargetAndShoot(double timeout) {
		requires(Robot.shooterWheels);
		m_timeout = timeout;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		targeting = new MoveToTargetYawPid(6);
		targeting.initialize();
		shooter = new ShooterSpeed(3000, 15);
		shooter.initialize();
		count = 0;
		waitCount = 3;
		state = State.Targeting;
		initTime = new Date().getTime();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()

	// Actions that will be Done
	
	// Turn on the Shooter Wheels
	
	// Robot.log("TAS Turn on Wheels");
	// Robot.navXPin5ShooterActive.set(true);
	// Robot.shooterWheels.shoot(); // Set Wheels to Fast Out
	
	// Do Targeting
	
	// Robot.log("Activate Canon Initialized");
	// Robot.cannon.out();
	// setTimeout(1);
	// Robot.log("Activate Canon end");
	// Robot.cannon.in();
	// Robot.shooterWheels.off();
	// Robot.navXPin5ShooterActive.set(false);

	protected boolean isFinished() {
		count++;
		double elaspedTime = (new Date().getTime() - initTime);
		if (elaspedTime > m_timeout * 1000) {
			Robot.logf("TAS -- timeOut state:%s wait:%d time:%.2f %n", state.name(), waitCount, elaspedTime / 1000);
			return true;
		}
		if (waitCount > 0) {
			waitCount--;
			Robot.logf("TAS -- waiting state:%s wait:%d time:%.2f %n", state.name(), waitCount, elaspedTime / 1000);
			return false;
		}

		if (state == State.Targeting) {  // initial state
			if (targeting.isFinished()) {
				targeting.end();
				targeting = null;
				state = State.WaitShooter;
				waitCount = 3;
				return false;
			}
		}

		if (state == State.WaitShooter) {  // Targeting complete wait for the shooter
			if (shooter.speedOk(100d)) {
				state = State.Shoot;
				return false;
			}
		}

		if (state == State.WaitTargeting) {  // Should not be needed shooter will wait for 15 seconds
			if (targeting.isFinished()) {
				targeting.end();
				targeting = null;
				state = State.Shoot;
				return false;
			}
		}

		if (state == State.Shoot) {
			Robot.logf("TAS -- Canon Out time:%.2f %n", elaspedTime / 1000);
			Robot.cannon.out();
			state = State.Shooting;
			waitCount = 50; // Time for 1 second to let shooter fire
			return false;
		}

		if (state == State.Shooting) {  // Shooter fired end the task
			return true;
		}
		return false;

	}

	// Called once after isFinished returns true
	protected void end() {
		double elaspedTime = (new Date().getTime() - initTime);
		Robot.logf("TAS -- Complete Canon In and Motors Off state:%s time:%.2f %n", state.name(), elaspedTime / 1000);
		if(targeting != null)
			targeting.interrupted(); // Stop any active targeting
		if(shooter != null)
			shooter.interrupted();
		Robot.cannon.in();
		Robot.shooterWheels.off();
		Robot.navXPin5ShooterActive.set(false);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.log("TAS Interupted");
		end();
	}

}
