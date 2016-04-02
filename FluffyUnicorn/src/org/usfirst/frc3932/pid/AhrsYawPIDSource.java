package org.usfirst.frc3932.pid;

import org.usfirst.frc3932.Robot;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class AhrsYawPIDSource implements PIDSource {
	RollAdapter rollAdapter = new RollAdapter();

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub

	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		// TODO Auto-generated method stub
		float yaw = Robot.ahrs.getYaw();
		float roll = Robot.ahrs.getRoll();
		if (Robot.rollAdapterChoice) {
			yaw = rollAdapter.adjustYaw(yaw, roll);
		}
		return yaw;
	}

}
