package org.usfirst.frc3932.pid;

import org.usfirst.frc3932.Robot;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AhrsYawPIDSource implements PIDSource {
	//RollAdapter rollAdapter = new RollAdapter();
	static int count =0;
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		double yaw = Robot.ahrs.getYaw();
		count++;
		// Count indicates how many times the Yaw is obtained for PID.
		// If count is changing while nothing is happening means 
		// that for some reason a PID has not been properly 
		// freed and stopped
		if(count % 10 == 0) SmartDashboard.putNumber("Pid yaw count", count);
		return yaw;
	}

}
