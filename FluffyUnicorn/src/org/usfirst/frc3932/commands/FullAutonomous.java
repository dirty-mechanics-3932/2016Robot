package org.usfirst.frc3932.commands;

import org.usfirst.frc3932.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class FullAutonomous extends CommandGroup {
	public FullAutonomous(Command obstacle, Command position){
	//	Robot.driveSystem.resetEncoders();
		addSequential(new ShooterOff());
		addSequential(new ResetAngle());
		addSequential (obstacle);
		addSequential(position);
		//addSequential(obstacle);
		//addSequential(new AutoShoot());
		
		
	}
}
