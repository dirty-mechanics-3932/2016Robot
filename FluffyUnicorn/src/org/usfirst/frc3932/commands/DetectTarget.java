// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc3932.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Date;

import org.usfirst.frc3932.Robot;

/**
 *
 */
public class DetectTarget extends Command {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	NetworkTable table = null;

	private static final double H_RES = 480;
	private static final double FIELD_OF_VIEW = 74;
	
	
	private boolean melborne = true;
	// private static final double FIELD_OF_VIEW = 47.5;

	double[] blob = null;
	public double angle = 0;
	public boolean angleReady = false;
	public double distance = 0;
	
	
	private Date m_DetectTargetCreate;
	private Date m_DetectTargetInit;

	public DetectTarget() {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		m_DetectTargetCreate = new Date();
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		if (melborne) return;
		angle = 0;
		angleReady = false;
		Robot.log("Detect Target Initialized");
		m_DetectTargetInit = new Date();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if(melborne) return;
		table = NetworkTable.getTable("SmartDashboard");
		double[] defaultValue = new double[0];
		double[] x = table.getNumberArray("XRoboRealmBlob", defaultValue);
		

		Robot.log("bloblength" + x.length + " Yaw:" + Robot.ahrs.getYaw() +" time:" + (new Date().getTime() - m_DetectTargetInit.getTime()));

		if (x.length > 0) {
			int arraysize = x.length;
			Robot.log("X[0]:" + x[0]);
			// angle = (x[arraysize-1] - H_RES/2) * (FIELD_OF_VIEW/H_RES);
			angle = (x[0] - H_RES / 2) * (FIELD_OF_VIEW / H_RES);
			angleReady = true;
			distance = table.getNumber("RoboRealmDistance", 0.0);
			//double distance2 = table.getNumber("RoboRealmDistance", 0.0);
			//distance = (distance1 + distance2)/2;
			// SmartDashboard.putNumber("angle", angle);

			SmartDashboard.putNumber("XRoboRealmBlob", x[0]);
			Robot.log("Blob X:" + x[0] + "Distance:" + distance);
		}

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if(melborne) {
			Robot.logf("Detect Target Melborn%n");
			return true;
		}
		double convTime = (new Date().getTime() - m_DetectTargetInit.getTime());
		return angleReady || convTime > 1000;
	}

	// Called once after isFinished returns true
	protected void end() {
		if(melborne) return;
		SmartDashboard.putNumber("x", angle);
		double convTime = (new Date().getTime() - m_DetectTargetInit.getTime());
		Robot.log("Target found:" + angle + " time:" + convTime);
		Robot.camera.turnByAngle = angle;
		Robot.camera.driveDistance = distance;
		SmartDashboard.putNumber("CameraAngle", angle);
	
		
		// Scheduler.getInstance().add(new TurnTo(Robot.ahrs.getYaw() + angle,
		// 5));

	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
