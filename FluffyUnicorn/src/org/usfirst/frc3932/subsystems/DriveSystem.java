// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3932.subsystems;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.RobotMap;
import org.usfirst.frc3932.commands.*;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotDrive;

import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class DriveSystem extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS
    // 360 tics per rotation, 84 in per second, 4.5 revolutions per second for arthur
	// 250 ticks per rotation, 108 in per second, 2900
	private static final double SPEED = 1000;
    
    private static final double MOTOR_I = 0.0001;
	private static final double MOTOR_P = .4;
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final CANTalon leftFront = RobotMap.driveSystemLeftFront;
    private final CANTalon leftRear = RobotMap.driveSystemLeftRear;
    private final CANTalon rightFront = RobotMap.driveSystemRightFront;
    private final CANTalon rightRear = RobotMap.driveSystemRightRear;
    private final RobotDrive tankDrive = RobotMap.driveSystemTankDrive;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    
    public DriveSystem() {
    	super();
//    	leftFront.changeControlMode(CANTalon.TalonControlMode.Speed);
    	leftRear.changeControlMode(CANTalon.TalonControlMode.Follower);

//    	rightFront.changeControlMode(CANTalon.TalonControlMode.Speed);
    	rightRear.changeControlMode(CANTalon.TalonControlMode.Follower);

//    	leftFront.setPID(MOTOR_P, MOTOR_I, 0);
//    	rightFront.setPID(MOTOR_P, MOTOR_I, 0);

    	
    	
    }


    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
    	
		
    	
    	setDefaultCommand(new DriveTeleop());
    }

	public void drive() {
		
		double left = 0, right = 0;
		
		if(! goForward.isLeftTriggerPressed)
		{
			GenericHID leftStick = Robot.oi.driverLeft;
			GenericHID rightStick = Robot.oi.driverRight;
			
			left = leftStick.getY();
			right = rightStick.getY();

		}
		
		else
		{
			GenericHID leftStick = Robot.oi.driverRight;
			GenericHID rightStick = Robot.oi.driverRight;
			
			left = leftStick.getY();
			right = leftStick.getY();

		}
		
//		leftFront.set(SPEED*left);
//		rightFront.set(SPEED*right);
		
		drivePercent(left, right);
	}


	public void drivePercent(double leftPercent, double rightPercent) {
		driveLeftPercent(leftPercent);
    	driveRightPercent(rightPercent);
	}


	public void driveRightPercent(double rightPercent) {
		rightRear.set(rightFront.getDeviceID());
    	rightFront.set(rightPercent);
	}


	public void driveLeftPercent(double leftPercent) {
		leftFront.set(leftPercent);
		leftRear.set(leftFront.getDeviceID());
	}
	
	public void setPercent(CANTalon masterTalon, CANTalon slaveTalon, double leftPercent) {
		masterTalon.set(leftPercent);
		slaveTalon.set(masterTalon.getDeviceID());
	}
}

