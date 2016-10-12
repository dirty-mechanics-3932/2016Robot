package org.usfirst.frc3932;

import edu.wpi.first.wpilibj.I2C;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.TimerTask;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class LIDAR implements PIDSource {
	private I2C i2c;
	private byte[] distance;
	private int dist;
	private java.util.Timer updater;

	private final int LIDAR_ADDR = 0x62;
	private final int LIDAR_CONFIG_REGISTER = 0x00;
	
	// private final int LIDAR_DISTANCE_REGISTER = 0x8f;

	int i = 0;

	public LIDAR(Port port) {
		i2c = new I2C(port, LIDAR_ADDR);

		distance = new byte[1];

		updater = new java.util.Timer();
	}

	// Distance in cm
	public int getDistance() {
		return dist;
	}

	public double getDistanceFeet() {
		return dist / (12d * 2.54d);
	}

	public double pidGet() {
		return getDistance();
	}

	// Start 25 Hz polling
	public void start() {
		updater.scheduleAtFixedRate(new LIDARUpdater(), 0, 40);
	}

	// Start polling for period in milliseconds
	public void start(int period) {
		updater.scheduleAtFixedRate(new LIDARUpdater(), 0, period);
	}

	public void stop() {
		updater.cancel();
		updater = new java.util.Timer();
	}

	// Update distance variable
	public void update() {
		int d = 0;
		 // Initiate measurement
		boolean w1 = i2c.write(LIDAR_CONFIG_REGISTER, 0x04);
		Timer.delay(0.04); // Delay for measurement to be taken
		boolean r1 = i2c.read(0x0f, 1, distance); // Read in measurement
		Timer.delay(0.008); // Delay to prevent over polling
		d += Integer.toUnsignedLong(distance[0] << 8);
		boolean r2 = i2c.read(0x10, 1, distance);
		d += Byte.toUnsignedInt(distance[0]);
		dist = d;
		// Robot.log("LIDAR Dist:" + dist + " w1:" + w1 + " r1:" +r1 +" r2:" + r2);
		SmartDashboard.putNumber("Lidar Distance feet", dist / (12 * 2.54));
	}

	// Timer task to keep distance updated
	private class LIDARUpdater extends TimerTask {
		public void run() {
			while (true) {
				update();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub

	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return PIDSourceType.kDisplacement;
	}
}
