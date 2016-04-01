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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3932.Robot;

/**
 *
 */
public class MoveToTarget extends Command {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	private DriveStraight Move;
	private double d;
	private double m_timeout = 5;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public MoveToTarget() {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		//DetectTarget GotTheTarget = new DetectTarget();
		//GotTheTarget.initialize(); 
		//GotTheTarget.execute();
		this.setTimeout(m_timeout);
		System.out.println("MoveToTarget Initialized YAW:" + Robot.ahrs.getYaw() + "Distance:" + Robot.camera.driveDistance);
		//Robot.driveSystem.resetEncoders();
		SmartDashboard.putNumber("RoboRealmCamera", Robot.camera.driveDistance);
		//	t = new TurnTo(Robot.ahrs.getYaw(),5);
		if (Robot.camera.driveDistance <13 && Robot.camera.driveDistance > 2)
		{
			Move = new DriveStraight(Robot.camera.driveDistance -12.8, .5);  // The Adjust on value is 1 foot latter improve.
			//	System.out.println("MoveToTarget Distance:" + Robot.camera.driveDistance
			//	t= new TurnTo(Robot.ahrs.getYaw()+ GotTheTarget.angle -4, 5);
			Move.initialize();
		} 
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		//	t.execute();

	}



	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {

		//Robot.driveSystem.resetEncoders();
		boolean done = Move.isFinished();

		System.out.println("Movetotarget timeout:" + isTimedOut() + " m_timeout:" + m_timeout + " isFinished: " +
				done);
		return done || isTimedOut();
	}

	// Called once after isFinished returns true
	protected void end() {
		System.out.println("MoveToTarget is Finished");
		Move.end();
		//add by elias
		Robot.driveSystem.resetEncoders();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Move.interrupted();
		Robot.driveSystem.resetEncoders();
	}
}
