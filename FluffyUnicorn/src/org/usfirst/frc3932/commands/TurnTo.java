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
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.pid.AhrsYawPIDSource;
import org.usfirst.frc3932.pid.DriveSystemRotatePIDOutput;

/**
 *
 */
public class TurnTo extends Command{

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
    private double m_degrees;
    
    private PIDController controller;

	private boolean notUsingI = true;
    
    //oscillation period .44
    private static final double P = .015;
    private static final double I = .001;
    private static final double D = 0;

    // Acceptable error
	private static final double EPSILON = 2;

	private static final double RELATIVELY_CLOSE = 10;
	
	private static final double MAX_OUTPUT = 0.3;
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public TurnTo(double degrees) {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        m_degrees = degrees;

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveSystem);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        
        controller = new PIDController(P, I, D, new AhrsYawPIDSource(), new DriveSystemRotatePIDOutput());
        
        controller.setInputRange(-180, 180);
        controller.setContinuous();
        controller.setAbsoluteTolerance(2);
        controller.setOutputRange(-MAX_OUTPUT, MAX_OUTPUT);

       
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	controller.setSetpoint(m_degrees);
    	controller.enable();
    	setTimeout(3);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//    	if (Math.abs(controller.getError()) < RELATIVELY_CLOSE && notUsingI){
//    		controller.setPID(P, I, D);
//    		controller.setSetpoint(m_degrees);
//    		controller.enable();
//    		notUsingI = false;
//    	}
//    	SmartDashboard.putNumber("Accumulation", controller.);
    	SmartDashboard.putNumber("Error", controller.getError());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return controller.onTarget() || isTimedOut();
        
    }

    // Called once after isFinished returns true
    protected void end() {
    	controller.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }

}
