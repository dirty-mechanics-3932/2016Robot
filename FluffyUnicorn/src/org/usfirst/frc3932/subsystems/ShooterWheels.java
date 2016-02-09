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

import org.usfirst.frc3932.RobotMap;
import org.usfirst.frc3932.commands.*;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class ShooterWheels extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final CANTalon leftWheel = RobotMap.shooterWheelsLeftWheel;
    private final CANTalon rightWheel = RobotMap.shooterWheelsRightWheel;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final double FAST_OUT_SPEED = 1;
    private final double OFF_SPEED = 0;
    private final double SLOW_IN_SPEED = -0.35;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    	//set 7 seconds delay
    	rightWheel.setInverted(true);
    }
	
    // Set the shooter wheels to "feed" mode (slow in)
    public void feed() {
    	leftWheel.set(SLOW_IN_SPEED);
    	rightWheel.set(SLOW_IN_SPEED);
        }
    
    // Set the shooter wheels to "shoot" mode (fast out)
    public void shoot() {
    	leftWheel.set(FAST_OUT_SPEED);
    	rightWheel.set(FAST_OUT_SPEED);
    }
    
    // Turn the shooter wheels off
    public void off() {
    	leftWheel.set(OFF_SPEED);
    	rightWheel.set(OFF_SPEED);
    }
}
