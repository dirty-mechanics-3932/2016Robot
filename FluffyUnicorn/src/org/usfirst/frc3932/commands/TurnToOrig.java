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

import edu.wpi.first.wpilibj.PIDController;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.RobotMap;
import org.usfirst.frc3932.Robot.ROBOTTYPES;
import org.usfirst.frc3932.pid.AhrsYawPIDSource;
import org.usfirst.frc3932.pid.DriveSystemRotatePIDOutput;
import java.util.Date;

/**
 *
 */
public class TurnToOrig extends Command {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	private double m_degrees;

	private PIDController controller;

	private boolean notUsingI = true;

	Preferences prefs;

	// oscillation period .44 -- Dave's p=.045,i=.007,d=0,f=1.3 for West Palm
	// Beach
	// Mini of working p=.045,i=0,d=.006,0 error 1, buffer 10
	// Mini working with Elias .036,.0025,.00025,0,.1,0 --Take 3 seconds to move
	// 90
	// Big 0.036,.0025,.0025,10,.1,5,Speed=.4
	// Elias .045,0,.040,1.3, error = .1 Works well
	// 9/19/16 .045,0,.006,0

	// private static final double P = .045;
	// private static final double I = .00;
	// private static final double D = .006;
	//
	private static double P;
	private static double I;
	private static double D;
	private static double F;
	private static double maxOutput;
	private static int maxErrorCount;
	private static double maxError;
	private static boolean brakeMode;
	private static boolean goodPid = true;

	private double m_timeout = 0;

	private int errorCounter = 0;
	private Date turnToCreate;
	private Date turnToInit;
	private int count = 0;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public TurnToOrig(double degrees, double timeout) {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		m_degrees = degrees;
		m_timeout = timeout;
		turnToCreate = new Date();
		requires(Robot.driveSystem);

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		// Add and F term at point 2 Keith fault

	}

	public TurnToOrig(double degrees) { // Overloaded for 5 second time out
		this(degrees, 5);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		errorCounter = 0;
		count = 0;

		if (Robot.robotType == ROBOTTYPES.MINI) {
			P = .055; // .055 kind of works
			I = .001;
			D = 0;
			F = 0;
			maxOutput = .7;
			maxErrorCount = 5;
			maxError = .5;
			brakeMode = true;
		}
		if (Robot.robotType == ROBOTTYPES.COMPETITION || Robot.robotType == ROBOTTYPES.SIBLING) {
			P = .045;
			I = 0;
			D = 0.006;
			F = 0;
			maxOutput = .8; // Was 9/19/16 = 0.3;
			maxErrorCount = 2;
			maxError = .5;
			brakeMode = true;
		}
		// Use preferences from the smart dash board
		//
		prefs = Preferences.getInstance();
		P = prefs.getDouble("P", P);
		I = prefs.getDouble("I", I);
		D = prefs.getDouble("D", D);
		F = prefs.getDouble("F", F);
		maxOutput = prefs.getDouble("maxO", maxOutput);
		double deltaYaw = Math.abs(m_degrees - Robot.ahrs.getYaw());

		if (Robot.robotType == ROBOTTYPES.COMPETITION && goodPid) {

			if (deltaYaw < 2) {
				maxOutput = .6;
				P = .15;
				I = 0;
				D = 0;
			} else if (deltaYaw < 5) {
				maxOutput = .6;
				P = .11;
				I = 0;
				D = 0;
			} else {
				maxOutput = .6;
				P = .06;
				I = 0;
				D = .05;
			}
		}
		if (Robot.robotType == ROBOTTYPES.SIBLING && goodPid) {
			if (deltaYaw < 3) {
				maxOutput = .5;
				P = 0.1;
				I = 0;
				D = 0.008;
			} else if (deltaYaw < 10) {
				maxOutput = .5;
				P = .07;
				I = 0;
				D = .008;
			} else {
				maxOutput = .5;
				P = .04;
				I = 0;
				D = 0;
			}
		}

		controller = new PIDController(P, I, D, F, new AhrsYawPIDSource(), new DriveSystemRotatePIDOutput());
		controller.setInputRange(-180, 180);
		controller.setContinuous(true);
		controller.setAbsoluteTolerance(1);
		// controller.setToleranceBuffer(1); // Was 10
		controller.setOutputRange(-maxOutput, maxOutput);
		Robot.logf("+++++ Turnto degrees:%.2f + timeOut:%.2f p:%.2f i:%.2f d:%.2f f:%.2f maxO:%.2f%n", m_degrees,
				m_timeout, P, I, D, F, maxOutput);
		controller.enable();
		controller.setSetpoint(m_degrees);
		turnToInit = new Date();
		if (brakeMode)
			Robot.setBrakeMode(true);

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		double err = controller.getError();
		if (Math.abs(err) < maxError)
			errorCounter++;
		else
			errorCounter = 0;

		double convTime = (new Date().getTime() - turnToInit.getTime());
		if (convTime > m_timeout * 1000)
			return true;
		Robot.logf(
				"TurnTo onTarget:%s YAW:%.2f Err:%.2f AvgErr:%.2f errCnt:%d onTgCnt:%d lc:%.2f rc:%.2f lv:%.2f rv:%.2f Conv:%.0f%n",
				controller.onTarget() ? "True" : "False", Robot.ahrs.getYaw(), err, controller.getAvgError(), count,
				errorCounter, RobotMap.driveSystemRightFront.getOutputCurrent(),
				RobotMap.driveSystemLeftFront.getOutputCurrent(), RobotMap.driveSystemLeftFront.getOutputVoltage(),
				RobotMap.driveSystemRightFront.getOutputVoltage(), convTime);
		boolean f1 = controller.onTarget();
		boolean f2 = ((errorCounter >= maxErrorCount));
		boolean f3 = controller.onTarget();
		boolean f4 = Math.abs(err) < maxError;
		boolean f5 = count > 20;
		if (f1)
			count++;
		boolean fmaster = f1;
		// if (fmaster)
		// return true;
		if (fmaster) {
			if (count > 2)
				return true;
		}
		// controller.disable();
		// count++;
		// } else {
		// count = 0;
		// }
		return false;

	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.log("------ TurnTo finished degrees = " + m_degrees + " timeOut:" + m_timeout + " Yaw:"
				+ Robot.ahrs.getYaw() + " Total Autonomous Time:" + (new Date().getTime() - turnToCreate.getTime()));
		double convTime = (new Date().getTime() - turnToInit.getTime());

		Robot.driveSystem.drivePercent(0, 0);
		if (brakeMode)
			Robot.setBrakeMode(true);

		SmartDashboard.putNumber("Time to Turn", convTime);
		controller.disable();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}

}
