package org.usfirst.frc3932.commands;

import org.usfirst.frc3932.Robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
	private NetworkTable table = null;

	private static final double H_RES = 640;
	private static final double Y_RES = 480;
	private static final double FIELD_OF_VIEW = 74;
	private String targetInfo;

	public double centerX;
	public double solidity;
	public double area;
	public double angle;
	public double xDist;
	public double height;
	public double totalYAngle;

	public boolean found;
	public int length;

	public Vision() {
		table = NetworkTable.getTable("GRIP/myContoursReport");
	}

	//
	public void updateSmartDashboard() {
		if (getTarget()) {
			Robot.navXPin8TargetFound.set(true);
			SmartDashboard.putNumber("centerX", centerX);
			SmartDashboard.putNumber("Target Angle", angle);
			SmartDashboard.putNumber("xDist", xDist / 12d);
			SmartDashboard.putNumber("areaDist", 38400d / area);
			SmartDashboard.putNumber("heightDist", 788d / height);
			SmartDashboard.putNumber("Y Angle", totalYAngle);
		} else {
			Robot.navXPin8TargetFound.set(false);
			SmartDashboard.putNumber("centerX", 0);
			SmartDashboard.putNumber("Target Angle", 9999);
			SmartDashboard.putNumber("xDist", 9999);
		}
		SmartDashboard.putString("Target Info", targetInfo);
		SmartDashboard.putNumber("Area", area);
		SmartDashboard.putNumber("solidity", solidity);
	}

	double lastArea = 0;

	public boolean getTarget() {
		table = NetworkTable.getTable("GRIP/myContoursReport");
		double[] defaultValue = new double[0];
		double[] solidityAr = table.getNumberArray("solidity", defaultValue);
		double[] centerXAr = table.getNumberArray("centerX", defaultValue);
		double[] centerYAr = table.getNumberArray("centerY", defaultValue);
		double[] areaAr = table.getNumberArray("area", defaultValue);
		double[] widthAr = table.getNumberArray("width", defaultValue);
		double[] heightAr = table.getNumberArray("height", defaultValue);
		targetInfo = "";
		length = solidityAr.length;
		if (length == 0) {// return false if no objects found
			targetInfo = "No Objects Found";
			return false;
		}
		int index = 0;
		if (centerXAr.length != length || centerYAr.length != length || areaAr.length != length
				|| widthAr.length != length || heightAr.length != length) {
			Robot.logf(
					"Vision getTarget Err: solidityAr:%d centerXAr:%d centerYAr:%d areaAr:%d widthAr:%d lengthAr:%d%n",
					solidityAr.length, centerXAr.length, centerYAr.length, areaAr.length, widthAr.length,
					heightAr.length);
			targetInfo = "Arrays not equal see log";
			return false;
		}
		found = false;
		int i = 0;
		try {
			for (i = 0; i < length; i++) {
				if (solidityAr[i] > .3 && solidityAr[i] < .6) {
					if (areaAr[i] > 1000 && areaAr[i] < 8000) {
						if (widthAr[i] > heightAr[i]) {
							index = i;
							found = true;
						} else {
							targetInfo = "width < height";
						}
					} else {
						targetInfo = "Bad Area " + areaAr[i];
					}
				} else {
					targetInfo = "bad solidity " + solidityAr[i];
				}
			}
			area = areaAr[index];
			if (lastArea == area) {
				targetInfo = "Coms Missing";
				return false;
			}
			lastArea = area;
			angle = (centerXAr[index] - H_RES / 2) * FIELD_OF_VIEW / H_RES;
			centerX = centerXAr[index];
			solidity = solidityAr[index];
			height = heightAr[index];

			if (found) {
				targetInfo = "OK " + length + " objects";
				// Sebastin's code to find a distance to the target
				totalYAngle = Robot.conf.mountAngle
						+ (((Y_RES - centerYAr[index]) - (Y_RES / 2)) * (FIELD_OF_VIEW / Y_RES));
				xDist = (Robot.conf.targetHeight - Robot.conf.mountHeight) / Math.tan(Math.toRadians(totalYAngle));
			}
		} catch (Exception e) {
			Robot.logf("Vision getTarget Exception: i:%d solidityAr:%d centerAr:%d areaAr:%d widthAr:%d lengthAr:%d%n",
					i, solidityAr.length, centerXAr.length, areaAr.length, widthAr.length, heightAr.length);
			e.printStackTrace();
			targetInfo = "Exception - See log";
			return false;
		}
		return found;
	}
}
