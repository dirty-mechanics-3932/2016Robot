package org.usfirst.frc3932.commands;

import org.usfirst.frc3932.Robot;

import edu.wpi.first.wpilibj.CANTalon;

public class DriveTenFeet {

	
	//9.42 inches per rotation
	
	public static final double DRIVE_SPEED = .6d * -1;
	int ticksMoved = 0;
	CANTalon masterTalon;
	CANTalon slaveTalon;
	private boolean encoderDecrements;
	
	public DriveTenFeet(CANTalon masterTalon, CANTalon slaveTalon, boolean encoderDecrements) {
		this.masterTalon = masterTalon;
		this.slaveTalon = slaveTalon; 
		this.encoderDecrements = encoderDecrements;
		masterTalon.setEncPosition(0);   
		
	}
	
	// Called just before this Command runs the first time
    protected void initialize() {
    	//360 ticks per rev
    	masterTalon.setEncPosition(0);    	
    }

    // Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.driveSystem.setPercent(masterTalon, slaveTalon, DRIVE_SPEED);
		ticksMoved = masterTalon.getEncPosition();
	}

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	int ticksMovedAdjusted = ticksMoved;
    	if (encoderDecrements) {
    		ticksMovedAdjusted = ticksMovedAdjusted * -1;
    	}
        return ticksMovedAdjusted > DriveAutomatically.TICKS_TO_TEN_FEET;
    }

    // Called once after isFinished returns true
    protected void end() {
    	stop();
    }

	void stop() {
		Robot.driveSystem.setPercent(masterTalon, slaveTalon, 0d);
		ticksMoved = 0;
		masterTalon.setEncPosition(0);
	}

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	stop();
    }

}
