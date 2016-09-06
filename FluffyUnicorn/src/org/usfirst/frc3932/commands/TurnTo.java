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

//import org.usfirst.frc3932.pid.AhrsYawPIDSource;
//import org.usfirst.frc3932.pid.DriveSystemRotatePIDOutput;
import java.util.Date;

import org.usfirst.frc3932.Robot;
import org.usfirst.frc3932.RobotMap;

//import edu.wpi.first.wpilibj.PIDController;

//import edu.wpi.first.wpilibj.PIDOutput;
//import edu.wpi.first.wpilibj.PIDSource;
//import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TurnTo extends Command {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	private Date m_turnToCreate;
	private Date m_turnToInit;
	private double m_degrees;
	private double m_timeout = 0;
	private Robot.RunningAverage avgError;

	private int Samples = 10; // 10 was good for practice robot
	private double MaxError = .2; // .4 was good practice robot

	private final double miniP[][] = { { 180, .5 }, { 20, .3 }, { 10, .2 }, { 5, .15 }, { 3, .11 }, { 2, .08 },
			{ 1, .06 }, { 0, 0 } };
	private final double competitiveP[][] = { { 180, .4 }, { 20, .3 }, { 10, .25 }, { 5, .22 }, { 3, .20 }, { 2, .19 },
			{ 1, .175 }, { .75, .170 }, { 0, .17 } };
	
	private final double compGoodBatteryP[][] = { { 180, .4 }, { 20, .3 }, { 10, .22 }, { 5, .18 }, { 3, .16 },
			{ 2, .15 }, { 1, .15 }, { .75, .14 }, { 0, .14 } };
	private double activeP[][] = compGoodBatteryP;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public TurnTo(double degrees, double timeout) {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		m_degrees = degrees;
		m_timeout = timeout;
		m_turnToCreate = new Date();

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

	}

	public TurnTo(double degrees) { // Overloaded for 5 second time out
		this(degrees, 5);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		avgError = new Robot.RunningAverage(Samples);
		Robot.logf(
				"++++++++++ Turnto init Degrees:%.2f CurrentYaw: %.3f Timeout:%.2f AllowedError:%.2f Samples:%d Version:%s %n",
				m_degrees, Robot.ahrs.getYaw(), m_timeout, MaxError, Samples, Robot.ahrs.getFirmwareVersion());
		m_turnToInit = new Date();

		if (Robot.robotType == 3)
			activeP = miniP;
		Robot.setBrakeMode(true);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		// Get status of the turn
		double convTime = (new Date().getTime() - m_turnToInit.getTime());
		SmartDashboard.putNumber("Time to Turn", convTime);
		double leftSpeed = RobotMap.driveSystemLeftFront.getSpeed();
		double rightSpeed = RobotMap.driveSystemRightFront.getSpeed();
		double leftVoltage = RobotMap.driveSystemLeftFront.getBusVoltage();
		double rightVoltage = RobotMap.driveSystemRightFront.getBusVoltage();
		double leftPos = RobotMap.driveSystemLeftFront.getPosition();
		double rightPos = RobotMap.driveSystemRightFront.getPosition();

		// Get Output level for wheels given error and speed
		double yaw = Robot.ahrs.getYaw();
		double err = m_degrees - yaw;
		avgError.putNew(err);
		double output = getOutput(err);
		if ((Math.abs(leftSpeed) + Math.abs(rightSpeed)) < 10)
			output *= 1.25;
		Robot.driveSystem.drivePercent(-output, output);

		// See if finished with the turn by looking at the error
		double kError = (Math.abs(avgError.getAverage()) + Math.abs(err)) / 2;
		Robot.logf(
				"TurnTo YAW:%.3f Err:%.3f AvgErr:%.3f kErr:%.3f L:%.0f R:%.0f LS:%.0f RS:%.0f LV:%.2f RV:%.2f ConvTime:%.0f Out:%.3f%n",
				yaw, err, avgError.getAverage(), kError, leftPos, rightPos, leftSpeed, rightSpeed, leftVoltage,
				rightVoltage, convTime, output);
		if (convTime > m_timeout * 1000)
			Robot.log("!!!!!!!!!! TurnTo Timeed Out time:" + convTime);
		return Math.abs(kError) < MaxError || (convTime > m_timeout * 1000);
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.driveSystem.drivePercent(0, 0);
		Robot.log("---------- TurnTo end degrees:" + m_degrees + " timeOut:" + m_timeout + " Yaw:" + Robot.ahrs.getYaw()
				+ " Total Autonomous Time:" + (new Date().getTime() - m_turnToCreate.getTime()));
		Robot.setBrakeMode(false);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}

	protected double getOutput(double error) {
		int i = 0;
		while (i < activeP.length && activeP[i][0] >= Math.abs(error)) {
			i++;
		}
		i = (i == 0 ? 1 : i);
		return error > 0 ? activeP[i - 1][1] : -activeP[i - 1][1];

	}

	protected String bool(boolean val) {
		return val ? "True" : "False";
	}

	protected double round4(double d) {
		return Math.round(d * 10000) / 10000.0;
	}
	
	public double[][] getCompetitiveP() {
		return competitiveP;
	}

	
	
}
