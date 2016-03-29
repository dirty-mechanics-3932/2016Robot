package org.usfirst.frc3932.commands;

import org.usfirst.frc3932.Robot;
import edu.wpi.first.wpilibj.smartdashboard.*;

import edu.wpi.first.wpilibj.CANTalon;

public class DriveAutomaticallyDelegate {


	
	//9.42 inches per rotation
	public static final double INCHES_PER_ROTATION = 9.42d;
	public static final double ROTATIONS_PER_FOOT = 12d /INCHES_PER_ROTATION;
	public static final double GEAR_RATIO = 12.75d; //doesn't enter into it, encoder connected to wheel
	public static final double TICKS_PER_FOOT = 1058.3d; // measured
	
	public static final boolean DRIVES_REVERSE = true;
	
	private double target;
	private double speed;
	
	int ticksMoved = 0;
	CANTalon masterTalon;
	CANTalon slaveTalon;
	private boolean encoderDecrements;
	
	public DriveAutomaticallyDelegate(CANTalon masterTalon, CANTalon slaveTalon, boolean encoderDecrements, double feet, double speed) {
		this.masterTalon = masterTalon;
		this.slaveTalon = slaveTalon; 
		this.encoderDecrements = encoderDecrements;
		this.target = feet * TICKS_PER_FOOT;
		this.speed = speed;
		if (DRIVES_REVERSE) {
			this.speed = -this.speed;
		}
		masterTalon.setEncPosition(0);	
	}
	
	// Called just before this Command runs the first time
    public void initialize() {
    	//360 ticks per rev
    	masterTalon.setEncPosition(0);    	
    }

    // Called repeatedly when this Command is scheduled to run
	public void execute() {
		Robot.driveSystem.setPercent(masterTalon, slaveTalon, speed);
		ticksMoved = masterTalon.getEncPosition();
	}

    // Make this return true when this Command no longer needs to run execute()
    public boolean isFinished() {
    	int ticksMovedAdjusted = ticksMoved;
    	if (encoderDecrements) {
    		ticksMovedAdjusted = ticksMovedAdjusted * -1;
    	}
        return ticksMovedAdjusted > target;
    }

    // Called once after isFinished returns true
    public void end() {
    	stop();
    }

	void stop() {
		Robot.driveSystem.setPercent(masterTalon, slaveTalon, 0d);
		ticksMoved = 0;
		masterTalon.setEncPosition(0);
	}

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    public void interrupted() {
    	stop();
    }

}
