package org.usfirst.frc3932.pid;

import org.usfirst.frc3932.Robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class DriveSystemReversePIDOutput implements PIDOutput {

	double speed = 0;
	
	public DriveSystemReversePIDOutput(double speed){
		this.speed = -speed;
	}

	@Override
	public void pidWrite(double output) {
		Robot.driveSystem.drivePercent(-speed-output, -speed+output);

	}

}
