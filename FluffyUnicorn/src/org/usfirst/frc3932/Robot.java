// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc3932;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.usfirst.frc3932.Robot.ROBOTTYPES;
import org.usfirst.frc3932.commands.*;
import org.usfirst.frc3932.subsystems.*;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.I2C;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	Command autonomousCommand;

	public static OI oi;
	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	public static DriveSystem driveSystem;
	public static Platform platform;
	public static Camera camera;
	public static Cannon cannon;
	public static OnBoardCompressor onBoardCompressor;
	public static ShooterWheels shooterWheels;
	public static PowerDistributionBoard powerDistributionBoard;
	public static Vision vision;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	public static AHRS ahrs;
	public static Boolean rollAdapterChoice = Boolean.FALSE;

	public static Date taskInitTime = new Date();

	private DigitalInput config0;
	private DigitalInput config1;
	public static DigitalOutput navXPin8TargetFound;
	public static DigitalOutput navXPin9TurningSRXPid;
	public static DigitalOutput navXPin5ShooterActive;

	public static DigitalInput pin8;
	public static DigitalInput pin9;
	public static Config conf;

	// shootTime is set by MoveToTarget and used by WaitFor to determine how
	// much time is need to let the motors spin up
	public static double shootTime = 0;

	public static double visionAngle = 0;

	private int count = 0;

	// Data for Robot Configuration
	public enum ROBOTTYPES {
		MINI, COMPETITION, SIBLING
	}

	// Set ROBOTTYPES default to Competition
	public static ROBOTTYPES robotType = ROBOTTYPES.COMPETITION;

	//
	public static CameraConfig[] cameras = {
			// new CameraConfig("cameraA.local"),
			// new CameraConfig("cameraB.local"),
			// new CameraConfig("cameraC.local")
	};
	public static int cameraIndex = 0;

	// public static final int NUMBER_OF_CAMERAS = cameras.length;

	public static LIDAR rangefinder;
	private static SendableChooser obstacleChooser = new SendableChooser();
	private static SendableChooser positionChooser = new SendableChooser();
	// private static SendableChooser rollAdapterChooser = new
	// SendableChooser();
	public static double shootSpinup = 3.0d;

	public Robot() {
		obstacleChooser.addDefault("Auto_Moat:", Commands.AUTO_MOAT);
		obstacleChooser.addObject("Auto_Low Bar:", Commands.AUTO_LOW_BAR);
		obstacleChooser.addObject("Auto_Rampart:", Commands.AUTO_RAMPART);
		obstacleChooser.addObject("AutoRoughTerrain:", Commands.AUTO_ROUGHTERRAIN);
		obstacleChooser.addObject("Auto RockWall:", Commands.AUTO_ROCKWALL);
		obstacleChooser.addObject("Do Nothing:", Commands.DO_NOTHING);
		SmartDashboard.putData("Auto-Obstacle:", obstacleChooser);

		positionChooser.addDefault("Position1", Commands.DRIVE_FROM_POSITION_1);
		positionChooser.addObject("Position 2", Commands.DRIVE_FROM_POSITION_2);
		positionChooser.addObject("Position 2A", Commands.DRIVE_FROM_POSITION_2A);
		positionChooser.addObject("Position 3", Commands.DRIVE_FROM_POSITION_3);
		positionChooser.addObject("Position 4", Commands.DRIVE_FROM_POSITION_4);
		positionChooser.addObject("Position 5", Commands.DRIVE_FROM_POSITION_5);
		positionChooser.addObject("Position 5A", Commands.DRIVE_FROM_POSITION_5A);
		positionChooser.addObject("Positions 5B", Commands.DRIVE_FROM_POSITION_5B);
		positionChooser.addObject("Move Forward", Commands.MOVE_FORWARD);
		SmartDashboard.putData("Auto-Position", positionChooser);

		// rollAdapterChooser.addObject("On", Boolean.TRUE);
		// rollAdapterChooser.addObject("Off", Boolean.FALSE);
		// SmartDashboard.putData("Roll Adapter", rollAdapterChooser);
	}

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		RobotMap.init();
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
		driveSystem = new DriveSystem();
		platform = new Platform();
		camera = new Camera();
		cannon = new Cannon();
		onBoardCompressor = new OnBoardCompressor();
		shooterWheels = new ShooterWheels();
		powerDistributionBoard = new PowerDistributionBoard();
		vision = new Vision();

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
		// OI must be constructed after subsystems. If the OI creates Commands
		// (which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.
		// oi = new OI();

		// instantiate the command used for the autonomous period
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS

		ahrs = new AHRS(SPI.Port.kMXP, (byte) 200);  // Was 200
		rangefinder = new LIDAR(I2C.Port.kMXP);  // Use V2 of the Lidar
		//rangefinder = new LIDARV3(I2C.Port.kOnboard);
		rangefinder.start();
		camera.lightOn();

		// OI must be constructed after subsystems. If the OI creates Commands
		// (which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.

		// NetworkTable.setClientMode();
		// NetworkTable.setIPAddress("127.0.0.1");
		setRobotType();
		conf = new Config();
		oi = new OI(); // OI should be last as it will allow created commands to
						// have configuration data
		SmartDashboard.putString("Target Result", "Waiting");
	}

	private void setRobotType() {
		config0 = new DigitalInput(5);
		config1 = new DigitalInput(6);

		// Pins 8 and 9 are used to determine how the encoders work for each of
		// the robots
		pin8 = new DigitalInput(8);
		pin9 = new DigitalInput(9);

		navXPin8TargetFound = new DigitalOutput(22);
		navXPin9TurningSRXPid = new DigitalOutput(23);
		navXPin5ShooterActive = new DigitalOutput(19);

		int robotConfig = (config0.get() ? 1 : 0) + (config1.get() ? 2 : 0);
		log("C0:" + config0.get() + " C1:" + config1.get());
		// robotConfig 3 - Mini, default COMPETITION
		robotType = ROBOTTYPES.COMPETITION;
		if (robotConfig == 3)
			robotType = ROBOTTYPES.MINI;
		if (robotConfig == 2)
			robotType = ROBOTTYPES.SIBLING;
		log("***** Startup Robot Type:" + robotType + " naxV Ver:" + Robot.ahrs.getFirmwareVersion() + " *****");
	}

	/**
	 * This function is called when the disabled button is hit. You can use it
	 * to reset subsystems before shutting down.
	 */
	@Override
	public void disabledInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();

		Scheduler.getInstance().run();

	}

	@Override
	public void disabledPeriodic() {
		// Scheduler.getInstance().run();
		// Scheduler.getInstance().disable();
		Scheduler.getInstance().removeAll();
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		// schedule the autonomous command (example)
		taskInitTime = new Date();
		ahrs.reset();
		Commands obstacle = (Commands) obstacleChooser.getSelected();
		Command obstacleCommand = CommandFactory.getCommand(obstacle);

		Commands position = (Commands) positionChooser.getSelected();
		Command positionCommand = CommandFactory.getCommand(position);

		log("***** Autonomous Init obstacle:" + obstacle.toString() + " position:" + position.toString() + " *****");

		if (obstacle == Commands.DO_NOTHING) {
			autonomousCommand = obstacleCommand;
		} else {
			
			if(position == Commands.MOVE_FORWARD)
				obstacleCommand = null;
			autonomousCommand = new FullAutonomous(obstacleCommand, positionCommand);
		}
		
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		// Added at Melborne since Vision wasn't working autonomous mode
		updateStatus();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		log("+++++ Start Teleop  ++++++");
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		taskInitTime = new Date();
		Robot.platform.tiltDown();
		Robot.camera.TiltUp();

	}

	/**
	 * This function is called periodically during operator control
	 */

	private void runCamera() {
		// counter = !counter;
		// if (counter) {
		// currentCamera().sendImage();
		// }
	}

	// private void checkCompressor() {
	// if (powerDistributionBoard.getVoltage() < MIN_VOLTAGE) {
	// onBoardCompressor.stop();
	// }
	// else {
	// onBoardCompressor.automatic();
	// }
	// }

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();

		// checkCompressor();
		// runCamera();
		updateStatus();
		callsPerSecond();

	}

	private long lastSecond = 0;
	private int cpsCount =0;
	private long longestGap = 0;
	
	private void callsPerSecond() {
		cpsCount++;
		long second = new Date().getTime();
		long gap = second - lastSecond;
		if(gap > longestGap)
			longestGap = gap;
		if (second / 1000 != lastSecond / 1000) {
			SmartDashboard.putNumber("Robot CPS", cpsCount);
			SmartDashboard.putNumber("Robot Gap(ms)", longestGap);
			if(longestGap > 40)
				logf("??????? Large Gap %d%n", longestGap);
			longestGap = 0;
			cpsCount = 0;
		}
		lastSecond = second;
	}
	
	private void updateStatus() {
		if (count % 10 == 0) { // Update Dash board every 10 cycles (200Ms)
			//SmartDashboard.putNumber("LIDAR Distance (cm)", rangefinder.getDistance());
			//SmartDashboard.putNumber("Lidar Distance feet", rangefinder.getDistance() / (12 * 2.54));
			SmartDashboard.putNumber("Yaw", ahrs.getYaw());
			if (conf.shooterPresent) {
				if (Robot.robotType != Robot.ROBOTTYPES.MINI) {
					SmartDashboard.putNumber("ShootRight En:", RobotMap.shooterWheelsRightWheel.getPosition());
					SmartDashboard.putNumber("ShootRight Sp:", RobotMap.shooterWheelsRightWheel.getSpeed());
				}
				SmartDashboard.putNumber("ShootLeft En:", RobotMap.shooterWheelsLeftWheel.getPosition());
				SmartDashboard.putNumber("ShootLeft Sp:", RobotMap.shooterWheelsLeftWheel.getSpeed());
			}
			if (vision != null)
				vision.updateSmartDashboard();
		}
		count++;
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}

	public static void switchCameraTo(int camera) {
		// cameraIndex = camera;
	}

	public static void switchCameras() {
		// int numberOfSwitches = 1;
		// switchCameraToNext();
		//
		// while (!currentCamera().exists() && numberOfSwitches <
		// NUMBER_OF_CAMERAS){
		// switchCameraToNext();
		// numberOfSwitches++;
		// }
	}

	private static void switchCameraToNext() {
		// cameraIndex++;
		// if (cameraIndex >= NUMBER_OF_CAMERAS){
		// cameraIndex = 0;
		// }
	}

	@SuppressWarnings("unused")
	private static CameraConfig currentCamera() {
		return cameras[cameraIndex];
	}

	public static void setBrakeMode(boolean mode) {
		RobotMap.driveSystemLeftFront.enableBrakeMode(mode);
		RobotMap.driveSystemRightFront.enableBrakeMode(mode);

		RobotMap.driveSystemLeftRear.enableBrakeMode(mode);
		RobotMap.driveSystemRightRear.enableBrakeMode(mode);

		log("!!!!! setBrakeMode:" + mode);
	}

	public static void log(String s) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss-SSS ");
		dateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		Date date = new Date();
		System.out.println(dateFormat.format(date) + s);
	}

	public static void logf(String pattern, Object... arguments) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss-SSS ");
		dateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		Date date = new Date();
		System.out.printf(dateFormat.format(date) + String.format(pattern, arguments));
	}

	// Class for Running Average
	public static class RunningAverage {
		private int bufferSize;
		private int lastSample = 0;
		private int numSamples = 0;
		private double[] samples;

		public RunningAverage(int size) {
			samples = new double[size];
			bufferSize = size;
		}

		public void putNew(double d) {
			if (lastSample >= bufferSize)
				lastSample = 0;
			samples[lastSample] = d;
			lastSample++;
			numSamples++;
			if (numSamples >= bufferSize)
				numSamples = bufferSize;
		}

		public double getAverage() {
			double sum = 0;
			for (int i = 0; i < numSamples; i++)
				sum += samples[i];
			return sum / numSamples;
		}

		public double deviation() {
			double deviation = 0;
			double avg = getAverage();
			for (int i = 0; i < numSamples; i++)
				deviation += Math.abs(samples[i] - avg);
			return deviation / numSamples;
		}

		public boolean isAverageValid() {
			return numSamples >= bufferSize;
		}

		/**
		 * 
		 * @return Returns true if last x averages are within a specified error.
		 */
		public boolean compareLast(int lastNum, double maxError) {
			if (lastNum > samples.length)
				lastNum = samples.length;
			double lastSample = samples[samples.length - 1], currentSample;
			for (int i = 1; i <= lastNum; i++) {
				currentSample = samples[samples.length - i];
				if (lastSample - currentSample < maxError)
					lastSample = currentSample;
				else
					return false;
			}
			return true;
		}
	}

	// Logic for a PID that we can see how the PID is converging
	class ourTurnPid {

		private double iSum;
		private double Kp, Ki, Kd;
		private double initialTarget = 0;
		private double outMax = .4;
		private double outMin = -.4;
		private double maxError = .25;
		private RunningAverage averageError;
		private RunningAverage averageOutput;
		private RunningAverage averageYaw;
		private double lastError = 0;
		private double lastInput = 0;
		private double lastOut;
		private int numberExecutes = 0;
		private double origKp;

		public ourTurnPid(double Kp, double Ki, double Kd, double MaxError, int numSamples) {
			this.Ki = Ki;
			this.Kp = Kp;
			this.Kd = Kd;
			this.origKp = Kp;
			this.maxError = MaxError;
			averageError = new RunningAverage(numSamples);
			averageOutput = new RunningAverage(numSamples);
			averageYaw = new RunningAverage(numSamples);
		}

		public void setTarget(double target) {
			initialTarget = target;
		}

		public void disable() {
			Robot.driveSystem.drivePercent(0, 0);
		}

		public boolean onTarget() {
			double err = (Math.abs(averageError.getAverage()) + Math.abs(lastError)) / 2;
			return (err < maxError);
		}

		public boolean onTarget1() {
			double err = (Math.abs(averageError.getAverage()) + Math.abs(lastError)) / 2;
			return (err < maxError);

		}

		public boolean onTarget2() {
			if (averageError.isAverageValid()) {
				double err = (Math.abs(averageError.getAverage()) + Math.abs(lastError)) / 2;
				if (err < maxError)
					return true;
			} else {
				if (numberExecutes > 2 && Math.abs(averageError.getAverage()) < maxError)
					return true;
			}
			return false;

		}

		public double getAvgError() {
			return averageError.getAverage();
		}

		public double getAvgOutput() {
			return averageOutput.getAverage();
		}

		public double getAvgYaw() {
			return averageYaw.getAverage();
		}

		public double getError() {
			return lastError;
		}

		public double lastOuput() {
			return lastOut;
		}

		public void execute() {
			double input = Robot.ahrs.getYaw();
			averageYaw.putNew(input);

			// Use current value, no derivative action on first sample
			if (lastInput == 0)
				lastInput = input;

			double target = initialTarget;

			double outputMax = outMax;
			double outputMin = outMin;

			double error = target - input;
			lastError = error;
			averageError.putNew(lastError);

			iSum += Ki * error;

			if (iSum > outputMax)
				iSum = outputMax;
			else if (iSum < outputMin)
				iSum = outputMin;

			double inputDelta = input - lastInput;

			double output = (Kp * error) + iSum - (Kd * inputDelta);

			if (output > outputMax)
				output = outputMax;
			else if (output < outputMin)
				output = outputMin;

			double cap = .15;
			if (Math.abs(output) < cap) {
				// output *= 2; // Changed from 1.8 Keith 4/8
				// if (averageYaw.compareLast(4, 0.01))
				// cap += 0.1;
				output = output < 0 ? -cap : cap;
				Robot.log("Updated Output variance:" + averageError.deviation());
			}

			Robot.driveSystem.drivePercent(-output, output);
			averageOutput.putNew(output);
			lastOut = output;
			lastInput = input;
			numberExecutes++;
		}
	}

	protected double round2(double d) {
		return Math.round(d * 100) / 100;
	}

	public static Command TurnToBest(double degrees, double timeout) {
		// Support for a number of different turning algorithms potentially each
		// robot could be different
		// return new TurnToZach(degrees, timeout); // Method a) Zachs original
		return new TurnToOrig(degrees, timeout); // Method b) Yaw PID
		// return new TurnTo(degrees, timeout); // Method c) Yaw with KAGPID
		// return new TurnToSRXPid(degrees, timeout); // Method d) SRX PID

	}

	// Class to allow different data for each robot type.
	public class Config {
		// Competition Robot -- default values
		public double pid[] = { 1d, 0d, 0 };
		public double f = 0;
		public int iZone = 0;
		public double rotateFactor = 40d;
		public double maxError = 1.5d;
		public double minVoltage = 3d;
		public boolean brakeMode = true;
		public double voltageRampRate = 0d;
		public double mountAngle = 30d;
		public double mountHeight = 20d;
		public double targetHeight = 91d;
		public boolean deepDebug = false;
		public double ticksPerFoot = 1690d; // was 1409d for St. Louis and Palm
											// // Beach
		public double magicDistance = 9.5d;

		public boolean shooterPresent = true;
		public boolean targetAndShoot = false;

		public Config() {
			Robot.logf("Init Configuration for Robotype:" + Robot.robotType.name());
			if (Robot.robotType == ROBOTTYPES.MINI) {
				pid[0] = 6d;
				pid[1] = 0d;
				pid[2] = 0d;
				f = 0d;
				rotateFactor = 14.2d; // Previous 14.8
				maxError = .5d;
				minVoltage = 2d;
				brakeMode = true;
				voltageRampRate = 0d;
				mountAngle = 25d;
				mountHeight = 15.5d;
				targetHeight = 89d;
				ticksPerFoot = 1290d;
				shooterPresent = false;
				magicDistance = 6d;
			} else {
				// Items for competition robots
				if (Robot.robotType == ROBOTTYPES.COMPETITION) {
					magicDistance = 8.5;
				} else {
					magicDistance = 9.0;
				}
				ticksPerFoot = 1690d;
				shooterPresent = true;
			}
		}

		// Create a string that shows the configuration data
		public String currentConfig() {
			return String.format(
					"P:%.2f I:%.2f D:%.2f F:%.2f rotF:%.2f maxErr:%.2f Min Volt:%.2f Brake:%s VRamp:%.2f Type:%s",
					pid[0], pid[1], pid[2], f, rotateFactor, maxError, minVoltage, (brakeMode ? "True" : "False"),
					voltageRampRate, Robot.robotType.name());
		}
		// Create Misc data for a side

	}

	public static String motorData(CANTalon talon) {
		return String.format("P:%.0f En:%d Sp:%.0f V:%.2f C:%.2f Err:%d", talon.getPosition(), talon.getEncPosition(),
				talon.getSpeed(), talon.getOutputVoltage(), talon.getOutputCurrent(), talon.getClosedLoopError());
	}

	public static String motorDataAll() {
		return "Left:" + motorData(RobotMap.driveSystemLeftFront) + " Right:"
				+ motorData(RobotMap.driveSystemRightFront);
	}

	public static String motorsSpeed() {
		return String.format("ls:%.0f rs:%.0f", RobotMap.driveSystemLeftFront.getSpeed(),
				RobotMap.driveSystemRightFront.getSpeed());
	}

	public static String motorsPosition() {
		return String.format("lp:%.0f rp:%.0f", RobotMap.driveSystemLeftFront.getPosition(),
				RobotMap.driveSystemRightFront.getPosition());
	}

	public static String motorsCurrent() {
		return String.format("lc:%.2f rc:%.2f", RobotMap.driveSystemLeftFront.getOutputCurrent(),
				RobotMap.driveSystemRightFront.getOutputCurrent());
	}

	public static String motorsVoltage() {
		return String.format("lv:%.2f rv:%.2f", RobotMap.driveSystemLeftFront.getOutputVoltage(),
				RobotMap.driveSystemRightFront.getOutputVoltage());
	}

	public static String motorsEncoder() {
		return String.format("len:%.0f ren:%.0f", RobotMap.driveSystemLeftFront.getEncPosition(),
				RobotMap.driveSystemRightFront.getEncPosition());
	}

	public static String motorsErorr() {
		return String.format("ler:%.0f rer:%.0f", RobotMap.driveSystemLeftFront.getClosedLoopError(),
				RobotMap.driveSystemRightFront.getClosedLoopError());
	}
}
