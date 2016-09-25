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

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.Robot.ROBOTTYPES;
import org.usfirst.frc3932.subsystems.*;

/**
 *
 */
public class AutoMoat extends CommandGroup {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
	public AutoMoat() {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PARAMETERS
		// Add Commands here:
		// e.g. addSequential(new Command1());
		// addSequential(new Command2());
		// these will run in order.

		// To run multiple commands at the same time,
		// use addParallel()
		// e.g. addParallel(new Command1());
		// addSequential(new Command2());
		// Command1 and Command2 will run in parallel.

		// A command group will require all of the subsystems that each member
		// would require.
		// e.g. if Command1 requires chassis, and Command2 requires arm,
		// a CommandGroup containing them would require both the chassis and the
		// arm.
		Robot.log("AutoMoat Created");
		addParallel(new CameraUp());
		addParallel(new PlatformUp());
		if (Robot.robotType == ROBOTTYPES.MINI) {  // Mini
			addSequential(new DriveStraight(2, .6));
			addSequential(Robot.TurnToBest(45, 2));
			addSequential(new DriveStraight(1, .6));
			addSequential(Robot.TurnToBest(-45, 2));
			addSequential(new DriveStraight(1, .6));
			addSequential(Robot.TurnToBest(0, 2));
			addSequential(new DriveStraight(1, .6));
		} else {  // Competition Robot
			addSequential(new DriveStraight(14.6, .6));
			addSequential(Robot.TurnToBest(0, 2));
		}

		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=COMMAND_DECLARATIONS

	}
}
