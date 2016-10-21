package org.usfirst.frc3932.commands;

import org.usfirst.frc3932.Robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
	private NetworkTable table = null;
	
	// To change camera characteristics you must do the following 
	// 1. Adjust the parameters for camera below by toggle comment
	// 2. Either 640 X 480 at 15 frames per second
	// 3. Or 320 X 240 at 30 frames per second
	// 4. Note: the camera Settings must be exposure at 25 - 30, color level, Brightness, Sharpness, and Contrast at 50
	// 5. White balance to Automatic, Exposure Priority to Default, Back light Compensation to off, Enable View area off

	// Values for 320 x 240
//	private static final double H_RES = 320;
//	private static final double Y_RES = 240;
//	private static final double FIELD_OF_VIEW = 90;
//	private static double minArea = 200;
//	private static double maxArea = 1000;
//	private static double minSolidity = .2d;
//	private static double maxSolidity = .4d;
	
	//Values for 640 X 480
	private static final double H_RES = 640;
	private static final double Y_RES = 480;
	private static final double FIELD_OF_VIEW = 74;  //62 seems to also work??
	private static double minArea = 800;  // Was 1000 10/12
	private static double maxArea = 9800;
	private static double minSolidity = .27d; //Was .3 10/12
	private static double maxSolidity = .6d;
	
	private Robot.RunningAverage averageArea = new Robot.RunningAverage(10);
	private String targetInfo;

	public double centerX;
	public double solidity;
	public double area;
	public double angle;
	public double xDist;
	public double height;
	public double width;
	public double totalYAngle;

	public boolean found;
	public int length;

	public Vision() {
		table = NetworkTable.getTable("GRIP/myContoursReport");
	}

	public void updateSmartDashboard() {
		if (getTarget()) {
			Robot.navXPin8TargetFound.set(true);
			SmartDashboard.putNumber("centerX", centerX);
			SmartDashboard.putNumber("Target Angle", angle);
			SmartDashboard.putNumber("xDist", xDist / 12d);
			SmartDashboard.putNumber("areaDist", 12871d / area);
			SmartDashboard.putNumber("heightDist", 421d / height);
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
					"Vision getTarget Error Array Sizes do not match: solidityAr:%d centerXAr:%d centerYAr:%d areaAr:%d widthAr:%d lengthAr:%d%n",
					solidityAr.length, centerXAr.length, centerYAr.length, areaAr.length, widthAr.length,
					heightAr.length);
			targetInfo = "Arrays not equal see log";
			return false;
		}
		found = false;
		int i = 0;
		double largestArea = 0;
		//int bestIdx = 0;
		try {
			for (i = 0; i < length; i++) {
				if (solidityAr[i] > minSolidity && solidityAr[i] < maxSolidity) {
					if (areaAr[i] > minArea && areaAr[i] < maxArea) {
						if (widthAr[i] > heightAr[i]) {
							index = i;
							if(areaAr[i] > largestArea ) {
								largestArea = areaAr[i];
								//index = i;
							}
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
			
			//  Really weird clearly a bug,code was in for 100% run,why did it work???
			//Did we only ever see one object? Was object 0 the best?
			//index = bestIdx;
			
			area = areaAr[index];
			//Robot.logf("area problem %.2f average: %.2f%n", area, averageArea.getAverage());
			if (Math.abs(averageArea.getAverage() - area) <= .05) {
				targetInfo = "Coms Missing";
				return false;
			}
			averageArea.putNew(area);
			angle = (centerXAr[index] - H_RES / 2) * FIELD_OF_VIEW / H_RES;
			centerX = centerXAr[index];
			solidity = solidityAr[index];
			height = heightAr[index];
			width = widthAr[index];

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
