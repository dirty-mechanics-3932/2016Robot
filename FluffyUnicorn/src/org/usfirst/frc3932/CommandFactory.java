package org.usfirst.frc3932;

import org.usfirst.frc3932.commands.AutoLowBar;
import org.usfirst.frc3932.commands.AutoMoat;
import org.usfirst.frc3932.commands.AutoRampart;
import org.usfirst.frc3932.commands.AutoRockWall;
import org.usfirst.frc3932.commands.AutoRoughTerrain;
import org.usfirst.frc3932.commands.DoNothing;
import org.usfirst.frc3932.commands.DriveFromPosition1;
import org.usfirst.frc3932.commands.DriveFromPosition2;
import org.usfirst.frc3932.commands.DriveFromPosition2A;
import org.usfirst.frc3932.commands.DriveFromPosition3;
import org.usfirst.frc3932.commands.DriveFromPosition4;
import org.usfirst.frc3932.commands.DriveFromPosition5;
import org.usfirst.frc3932.commands.DriveFromPosition5A;
import org.usfirst.frc3932.commands.DriveFromPosition5B;

import edu.wpi.first.wpilibj.command.Command;

public class CommandFactory {
	public static Command getCommand(Commands c){
		switch(c){
		case AUTO_LOW_BAR:
			return new AutoLowBar();
		case AUTO_MOAT:
			return new AutoMoat();
		case AUTO_RAMPART:
			return new AutoRampart();
		case AUTO_ROCKWALL:
			return new AutoRockWall();
		case AUTO_ROUGHTERRAIN:
			return new AutoRoughTerrain();	
		case DRIVE_FROM_POSITION_1:
			return new DriveFromPosition1();
		case DRIVE_FROM_POSITION_2:
			return new DriveFromPosition2();
		case DRIVE_FROM_POSITION_2A:
			return new DriveFromPosition2A();
		case DRIVE_FROM_POSITION_3:
			return new DriveFromPosition3();
		case DRIVE_FROM_POSITION_4:
			return new DriveFromPosition4();
		case DRIVE_FROM_POSITION_5:
			return new DriveFromPosition5();
		case DRIVE_FROM_POSITION_5A:
			return new DriveFromPosition5A();
		case DRIVE_FROM_POSITION_5B:
			return new DriveFromPosition5B();
		case DO_NOTHING:
			return new DoNothing();
		default:
			return new DoNothing();
		}
	}
}
