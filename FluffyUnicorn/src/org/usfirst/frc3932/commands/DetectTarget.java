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
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3932.Robot;

/**
 *
 */
public class DetectTarget extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	
	NetworkTable table = null;
	
	private static final double H_RES = 480;
	private static final double FIELD_OF_VIEW = 67;
	
	double[] blob = null;
	public double angle = 0;
	public boolean angleReady = false;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public DetectTarget() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	table = NetworkTable.getTable("dad");
    	double[] defaultValue = new double[0];
    	double[] x = table.getNumberArray("BLOBS", defaultValue);
    	double distance = table.getNumber("Distance", 0.0);
    	SmartDashboard.putNumber("Distance", distance);
    	
    	String hello = table.getString("Hello", "world");
    	SmartDashboard.putString("Hello", hello);
    	
    	if (x.length > 0){
    		System.out.println(x[0]);
    		angle = (x[0] - H_RES/2) * (FIELD_OF_VIEW/H_RES);
    		angleReady = true;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return angleReady;
    }

    // Called once after isFinished returns true
    protected void end() {
    	SmartDashboard.putNumber("x", angle);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
