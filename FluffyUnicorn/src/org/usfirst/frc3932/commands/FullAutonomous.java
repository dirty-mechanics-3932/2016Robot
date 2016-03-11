package org.usfirst.frc3932.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class FullAutonomous extends CommandGroup {
	public FullAutonomous(Command obstacle, Command position){
		addSequential(new ResetAngle());
		addSequential(obstacle);
		addSequential(position);
	}
}
