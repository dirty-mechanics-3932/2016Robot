package org.usfirst.frc3932.commands;

import org.usfirst.frc3932.Robot;
import java.lang.Math;
import java.util.HashMap;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
	private NetworkTable table = null;
	
	public static double angle;
	public static double area;
	public static double centerX;
	public static int length;
	public static double solidity;
	private static final double H_RES = 640;
	private static final double Y_RES = 480;
	private static final double FIELD_OF_VIEW = 74;
	private static final HashMap<Integer, Double> mountAngle = new HashMap<Integer, Double>(); // mount info should be in seperate class, this is temp
	private static final HashMap<Integer, Double> mountHeight = new HashMap<Integer, Double>();
	private static double xDist;
	private static double totalYAngle;

	public Vision() {
		table = NetworkTable.getTable("GRIP/myContoursReport");
	}

	public void updateSmartDashboard() {
		boolean hasTarget = getTarget();
		boolean hasXDist = !(getDist() == -1);
		SmartDashboard.putNumber("centerX", hasTarget ? centerX : 0);
		SmartDashboard.putNumber("Area", hasTarget ? area : 0);
		SmartDashboard.putNumber("solidity", hasTarget ? solidity : 0);
		SmartDashboard.putNumber("xDist", hasXDist ? xDist : 0);
		SmartDashboard.putNumber("totalYAngle", hasXDist ? totalYAngle : 0);
	}
	public double getMountAngle(int robotType) {
		mountAngle.put(0, (double) 0); //comp cam angle
		mountAngle.put(1, (double) 0); //daughter cam angle
		mountAngle.put(3, (double) 0);  //mini cam angle
		return mountAngle.get(robotType);
	}
	public double getMountHeight(int robotType) {
		mountHeight.put(0, (double) 0); //comp cam height
		mountHeight.put(1, (double) 0); //daughter cam height
		mountHeight.put(3, (double) 0);  //mini cam height
		return mountHeight.get(robotType);
	}

	public double getDist() {
		double[] defaultValue = new double[0];
		table = NetworkTable.getTable("GRIP/myContoursReport");
		double[] solidityAr = table.getNumberArray("solidity", defaultValue);
		double[] centerYAr = table.getNumberArray("centerY", defaultValue);
		xDist = -1; //dist to target in inches
		length = solidityAr.length;
		int objIndex = getObj(length, solidityAr);
		if (!(objIndex == -1)) {
			totalYAngle = getMountAngle(Robot.robotType) + (centerYAr[objIndex] - (Y_RES / 2) * (FIELD_OF_VIEW / Y_RES));
			xDist = (91 - getMountHeight(Robot.robotType)) / Math.tan(Math.toRadians(totalYAngle)); //target is 91 in tall
		}
		return xDist;
	}

	public int getObj(double l, double[] solidityAr) {
		int index = -1;
		if (l > 0) {
			for (int i = 0; i < length; i++) {
				if (solidityAr[i] > .28 && solidityAr[i] < .6) { // fix condition values
					index = i;
				}
			}
		}
		return index;
	}

	public boolean getTarget() {
		table = NetworkTable.getTable("GRIP/myContoursReport");
		double[] defaultValue = new double[0];
		double[] solidityAr = table.getNumberArray("solidity", defaultValue);
		double[] centerXAr = table.getNumberArray("centerX", defaultValue);
		double[] areaAr = table.getNumberArray("area", defaultValue);
		boolean found = false;
		length = solidityAr.length;
		int objIndex = getObj(length, solidityAr);
		if (!(objIndex == -1)) {
			angle = (centerXAr[objIndex] - (H_RES / 2)) / (H_RES / FIELD_OF_VIEW); // 1/10.4 degrees/pixels height of cam = 1ft, height of target = 7 ft 1, c x = 10 in from either side,										
			area = areaAr[objIndex];
			centerX = centerXAr[objIndex];
			solidity = solidityAr[objIndex];
			found = true;
		}
		return found;

	}

}
