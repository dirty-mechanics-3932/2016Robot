package org.usfirst.frc3932.pid;

public class RollAdapter {
	
	public float adjustYaw(float yaw, float roll) {
		if (inverted(roll)) {
			yaw = yaw * -1f;
		}
		return yaw;
	}

	boolean inverted(float roll) {
		boolean inverted = false;
		float modRoll = roll % 360;
		if (modRoll > 90f && modRoll < 270f) {
			inverted = true;
		}
		if (modRoll < -90f && modRoll > -270f) {
			inverted = true;
		}
		return inverted;
	}

}
