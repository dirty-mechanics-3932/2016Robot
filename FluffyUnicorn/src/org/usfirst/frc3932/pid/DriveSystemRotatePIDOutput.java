package org.usfirst.frc3932.pid;

import org.usfirst.frc3932.Robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class DriveSystemRotatePIDOutput implements PIDOutput {

	@Override
	public void pidWrite(double output) {
		Robot.driveSystem.drivePercent(output, -output);

	}

}
