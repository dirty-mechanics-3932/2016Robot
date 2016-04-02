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
import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.RobotMap;
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
	
	public static final double TICKS_PER_FOOT = 1409d; // measured

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public DriveStraight(double feet, double speed) {
    	

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        m_feet = feet;
        m_speed = speed;

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveSystem);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        
        PIDOutput out;
        if (feet < 0){
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
    //Code added by elias and dave
//    	Robot.ahrs.reset();
    	System.out.println("Before Init L:" + RobotMap.driveSystemLeftFront.getEncPosition() + 
    			" R:" + RobotMap.driveSystemRightFront.getEncPosition());
        controller.disable();
    	Robot.driveSystem.resetEncoders();
    	controller.setSetpoint(Robot.ahrs.getYaw());
    	System.out.println("DriveStraighINIT YAW:"+ Robot.ahrs.getYaw() + 
    			" speed:" + m_speed + " distance:" + m_feet + 
    			" After L:" + RobotMap.driveSystemLeftFront.getEncPosition() + 
    			" R:" + RobotMap.driveSystemRightFront.getEncPosition());
    	controller.enable();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    
    protected double getDistance() {
    	return Robot.driveSystem.getDistance() / TICKS_PER_FOOT;
    }
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	System.out.println("L:" + RobotMap.driveSystemLeftFront.getEncPosition() + " R:" + RobotMap.driveSystemRightFront.getEncPosition()
    	+ " Yaw:" + Robot.ahrs.getYaw());
        return Math.abs(getDistance()) > Math.abs(m_feet);
    }

    // Called once after isFinished returns true
    protected void end() {
    	System.out.println(buildDebugString());
    	Robot.driveSystem.resetEncoders();
    	
     controller.disable();
    // controller.reset();
    //	Robot.driveSystem.resetEncoders();
    }

	private String buildDebugString() {
		return "End L:" + RobotMap.driveSystemLeftFront.getEncPosition() 
			+ " R:" + RobotMap.driveSystemRightFront.getEncPosition()
			+ " Pitch:" + Robot.ahrs.getPitch()
			+ " Yaw:" + Robot.ahrs.getYaw()
			+ " Roll:" + Robot.ahrs.getRoll()
			;
	}

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    	//Robot.driveSystem.resetEncoders();
    }
}
