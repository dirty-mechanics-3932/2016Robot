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
import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.RobotMap;
import org.usfirst.frc3932.subsystems.*;

/**
 *
 */
public class DriveFromDefenses extends CommandGroup {


    private static final double SPEED = 0.6d;
    private static final double FEET_DEFENSES_TO_WALL = 16d;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
    public DriveFromDefenses(double feetFromDefenses, double angle) {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS
    //	DetectTarget GotTheTarget = new DetectTarget();
    	//addSequential(new TurnTo(0, 1));
    	//addSequential(new DriveStraight(feetFromDefenses, SPEED));
    	//addSequential (new WaitFor(1));
    	
    	addSequential(Robot.TurnToBest(angle, 60));
    	//addSequential (new WaitFor(1));
    	//addSequential(new DriveFromDefenses(FEET, ANGLE));
    	addSequential (new WaitFor(.5));
    	addSequential(new DetectTarget());
    //	addSequential (GotTheTarget);
    	//Robot.log("Encoder Positions"+ RobotMap.driveSystemLeftFront.getEncPosition() + " " + RobotMap.driveSystemRightFront.getEncPosition());
    	//Robot.log("Angle Size" + angle + "Detected Angle" + GotTheTarget.angle);
    	addSequential(new TurnToTarget());
    	//addSequential (new WaitFor(1));
    	addSequential(new ShooterOut());
    	//addSequential (new WaitFor(1));
    	addSequential(new ActivateCannon());
    	//addSequential (new WaitFor(1));
    	addSequential(new ShooterOff());

    	
    	
    	
 
    } 
}
