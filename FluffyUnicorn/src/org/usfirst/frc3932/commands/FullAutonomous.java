package org.usfirst.frc3932.commands;

import org.usfirst.frc3932.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class FullAutonomous extends CommandGroup {
	public FullAutonomous(Command obstacle, Command position){
		addSequential(new ShooterOff());
		addSequential(new ResetAngle());
		addSequential(new WaitFor(.070));
		addSequential(obstacle);
		addSequential(position);		
	}
}
