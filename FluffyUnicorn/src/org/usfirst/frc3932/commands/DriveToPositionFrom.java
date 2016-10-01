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
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.pid.DriveSystemForwardPIDOutput;

/**
 *
 */
public class DriveToPositionFrom extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
    private double m_feet;
    private double m_speed;
    
    private PIDController controller;
    
    private static final double P = 1;
    private static final double I = 0;
    private static final double D = 0;
	private static final double MAX_OUTPUT = 0.6;
	private static final double TOLERANCE = 10; // in cm
	private static final double FEET_TO_CM = 12*2.54;
    
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public DriveToPositionFrom(double feet, double speed) {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        m_feet = feet;
        m_speed = speed;

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveSystem);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        
        controller = new PIDController(P, I, D, Robot.rangefinder, new DriveSystemForwardPIDOutput());
        controller.setOutputRange(-MAX_OUTPUT, MAX_OUTPUT);
        controller.setAbsoluteTolerance(TOLERANCE);
        
        controller.setSetpoint(feet * FEET_TO_CM);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	controller.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return controller.onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	controller.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	controller.disable();
    }
}