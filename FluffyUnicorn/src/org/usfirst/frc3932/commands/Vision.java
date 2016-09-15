package org.usfirst.frc3932.commands;

import org.usfirst.frc3932.Robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
	private NetworkTable table = null;

	private static final double H_RES = 640;
	private static final double FIELD_OF_VIEW = 74;

	public double centerX;
	public double solidity;
	public double area;
	public double angle;
	public boolean found;
	public int length;

	public Vision() {
		table = NetworkTable.getTable("GRIP/myContoursReport");
	}

	public void updateSmartDashboard() {
		if (getTarget()) {
			SmartDashboard.putNumber("centerX", centerX);
			SmartDashboard.putNumber("Area", area);
			SmartDashboard.putNumber("solidity", solidity);
		} else {
			SmartDashboard.putNumber("centerX", 0);
			SmartDashboard.putNumber("Area", 0);
			SmartDashboard.putNumber("solidity", 0);
		}
	}

	public boolean getTarget() {
		table = NetworkTable.getTable("GRIP/myContoursReport");
		double[] defaultValue = new double[0];
		double[] solidityAr = table.getNumberArray("solidity", defaultValue);
		double[] centerXAr = table.getNumberArray("centerX", defaultValue);
		double[] areaAr = table.getNumberArray("area", defaultValue);
		double[] widthAr = table.getNumberArray("width", defaultValue);
		double[] heightAr = table.getNumberArray("height", defaultValue);
		length = solidityAr.length;
		if (length == 0) // return false if no objects found
			return false;
		int index = 0;
		found = false;
		int i = 0;
		try {

			for (i = 0; i < length; i++) {
				if (solidityAr[i] > .3 && solidityAr[i] < .6 && areaAr[i] > 2000 && areaAr[i] < 6000 && widthAr[i] > heightAr[i] ) {
					index = i;
					found = true;
				}
			}
			angle = (centerXAr[index] - H_RES / 2) / 10;
			area = areaAr[index];
			centerX = centerXAr[index];
			solidity = solidityAr[index];
		} catch (Exception e) {
			Robot.logf("Vision getTarget Exception: i:%d solidityAr:%d centerAr:%d areaAr:%d%n", i, solidityAr.length,
					centerXAr.length, areaAr.length);
			System.err.println(e.getStackTrace());
			return false;
		}
		return found;
	}

}
