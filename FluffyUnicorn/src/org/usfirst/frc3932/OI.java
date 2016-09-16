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

//import com.kauailabs.navx.frc.AHRS;

import org.usfirst.frc3932.commands.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc3932.subsystems.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand())Robot.shooterWheels.off();;

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	public JoystickButton setEqualToRightJoy;
	public JoystickButton driveSlowButton;
	public JoystickButton driveFastButton;
	public JoystickButton cameraLightButton;
	public JoystickButton lIDARDriveButton;
	public JoystickButton setEqualToRightJav;
	public Joystick driverLeft;
	public JoystickButton shootButton;
	public JoystickButton lineUpButton;
	public JoystickButton lowShotButton;
	public JoystickButton btnTurnToTargetandShoot;
	public Joystick driverRight;
	public JoystickButton shootOut;
	public JoystickButton shootFeed;
	public JoystickButton shootOff;
	public JoystickButton platformTiltToggleButton;
	public JoystickButton shootSlowOut;
	public JoystickButton cameraToggleCmd;
	public Joystick operatorJoystick;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	public OI() {
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

		operatorJoystick = new Joystick(2);

		cameraToggleCmd = new JoystickButton(operatorJoystick, 4);
		cameraToggleCmd.whenPressed(new CameraToggle());
		shootSlowOut = new JoystickButton(operatorJoystick, 6);
		shootSlowOut.whenPressed(new ShooterSlowOut());
		platformTiltToggleButton = new JoystickButton(operatorJoystick, 5);
		platformTiltToggleButton.whenPressed(new PlatformTiltToggle());
		shootOff = new JoystickButton(operatorJoystick, 3);
		shootOff.whenPressed(new ShooterOff());
		shootFeed = new JoystickButton(operatorJoystick, 2);
		shootFeed.whenPressed(new ShooterFeed());
		shootOut = new JoystickButton(operatorJoystick, 1);
		shootOut.whenPressed(new ShooterOut());
		driverRight = new Joystick(1);

		btnTurnToTargetandShoot = new JoystickButton(driverRight, 3);
		btnTurnToTargetandShoot.whenPressed(new TurnToTargetandShoot());
		lowShotButton = new JoystickButton(driverRight, 4);
		lowShotButton.whenPressed(new LowShot());
		lineUpButton = new JoystickButton(driverRight, 2);
		lineUpButton.whenPressed(new DetectTarget());
		shootButton = new JoystickButton(driverRight, 1);
		shootButton.whenPressed(new ActivateCannon());
		driverLeft = new Joystick(0);

		setEqualToRightJav = new JoystickButton(driverLeft, 1);
		setEqualToRightJav.whenReleased(new returnToNormal());
		lIDARDriveButton = new JoystickButton(driverLeft, 2);
		lIDARDriveButton.whenPressed(new LIDARDrive());
		cameraLightButton = new JoystickButton(driverLeft, 6);
		cameraLightButton.whenPressed(new CameraLightToggle());
		driveFastButton = new JoystickButton(driverLeft, 4);
		driveFastButton.whenReleased(new DriveFast());
		driveSlowButton = new JoystickButton(driverLeft, 4);
		driveSlowButton.whileHeld(new DriveSlow());
		setEqualToRightJoy = new JoystickButton(driverLeft, 1);
		setEqualToRightJoy.whileHeld(new goForward());

		// SmartDashboard Buttons

		SmartDashboard.putData("returnToNormal", new returnToNormal());
		SmartDashboard.putData("goForward", new goForward());
		SmartDashboard.putData("CameraToggle", new CameraToggle());
		SmartDashboard.putData("DriveTeleop", new DriveTeleop());
		SmartDashboard.putData("ShooterFeed", new ShooterFeed());
		SmartDashboard.putData("ShooterOut", new ShooterOut());
		SmartDashboard.putData("ShooterOff", new ShooterOff());
		SmartDashboard.putData("ActivateCannon", new ActivateCannon());
		SmartDashboard.putData("PlatformTiltToggle", new PlatformTiltToggle());
		SmartDashboard.putData("Camera11", new Camera11());
		SmartDashboard.putData("Camera12", new Camera12());
		SmartDashboard.putData("CameraSwitch", new CameraSwitch());
		SmartDashboard.putData("DriveAutoToRampart", new DriveAutoToRampart());
		SmartDashboard.putData("DriveSlow", new DriveSlow());
		SmartDashboard.putData("DriveFast", new DriveFast());
		SmartDashboard.putData("ShooterSlowOut", new ShooterSlowOut());
		SmartDashboard.putData("CameraLightToggle", new CameraLightToggle());
		SmartDashboard.putData("DriveOverMoat", new DriveOverMoat());
		SmartDashboard.putData("DriveOverRockWall", new DriveOverRockWall());
		SmartDashboard.putData("DriveThroughLowBar", new DriveThroughLowBar());
		SmartDashboard.putData("AutoMoat", new AutoMoat());
		SmartDashboard.putData("AutoLowBar", new AutoLowBar());
		SmartDashboard.putData("DriveFromPosition1", new DriveFromPosition1());
		SmartDashboard.putData("DriveFromPosition2", new DriveFromPosition2());
		SmartDashboard.putData("DriveFromPosition2A", new DriveFromPosition2A());
		SmartDashboard.putData("DriveFromPosition3", new DriveFromPosition3());
		SmartDashboard.putData("DriveFromPosition4", new DriveFromPosition4());
		SmartDashboard.putData("DriveFromPosition5", new DriveFromPosition5());
		SmartDashboard.putData("DriveFromPosition5A", new DriveFromPosition5A());
		SmartDashboard.putData("DriveFromPosition5B", new DriveFromPosition5B());
		SmartDashboard.putData("ResetAngle", new ResetAngle());
		SmartDashboard.putData("DetectTarget", new DetectTarget());
		SmartDashboard.putData("TurnToTarget", new TurnToTarget());
		SmartDashboard.putData("LIDARDrive", new LIDARDrive());
		SmartDashboard.putData("RoboRealmDistanceGet", new RoboRealmDistanceGet());
		SmartDashboard.putData("RoboRealmDistanceDrive", new RoboRealmDistanceDrive());
		SmartDashboard.putData("LowShot", new LowShot());
		SmartDashboard.putData("RoboRealmEncoderDrive", new RoboRealmEncoderDrive());
		SmartDashboard.putData("AutoShoot", new AutoShoot());
		SmartDashboard.putData("AutoRampart", new AutoRampart());
		SmartDashboard.putData("AutoRoughTerrain", new AutoRoughTerrain());
		SmartDashboard.putData("AutoRockWall", new AutoRockWall());
		SmartDashboard.putData("MoveToTarget", new MoveToTarget());
		SmartDashboard.putData("TurnTo0Degrees", new TurnTo(0, 2));
		SmartDashboard.putData("TurnTo5Degrees", new TurnTo(5, 2));
		SmartDashboard.putData("TurnTo10Degrees", new TurnTo(10, 2));
		SmartDashboard.putData("TurnTo15Degrees", new TurnTo(15, 2));
		SmartDashboard.putData("TurnTo30Degrees", new TurnTo(30, 4));
		SmartDashboard.putData("TurnTo45Degrees", new TurnTo(45, 4));
		SmartDashboard.putData("TurnTo90Degrees", new TurnTo(90, 5));
		SmartDashboard.putData("TurnToSRXPid 0", new TurnToSRXPid(0, 2));
		SmartDashboard.putData("TurnToSRXPid 1", new TurnToSRXPid(1, 2));
		SmartDashboard.putData("TurnToSRXPid -1", new TurnToSRXPid(-1, 2));
		SmartDashboard.putData("TurnToSRXPid 3", new TurnToSRXPid(3, 2));
		SmartDashboard.putData("TurnToSRXPid -3", new TurnToSRXPid(-3, 2));
		SmartDashboard.putData("TurnToSRXPid 10", new TurnToSRXPid(10, 2));
		SmartDashboard.putData("TurnToSRXPid -10", new TurnToSRXPid(-10, 2));
		SmartDashboard.putData("TurnToSRXPid 45", new TurnToSRXPid(45, 2));
		SmartDashboard.putData("TurnToSRXPid -45", new TurnToSRXPid(-45, 2));
		SmartDashboard.putData("TurnToSRXPid 90", new TurnToSRXPid(90, 2));
		SmartDashboard.putData("TurnToSRXPid -90", new TurnToSRXPid(-90, 2));
		SmartDashboard.putData("TurnToTargetandShoot", new TurnToTargetandShoot());
		SmartDashboard.putData("KeithMoveToTarget", new KeithMoveToTarget(10));

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

		/*
		 * SmartDashboard.putData("TurnToTenDegrees", new TurnToTenDegrees());
		 * SmartDashboard.putData("returnToNormal", new returnToNormal());
		 * SmartDashboard.putData("goForward", new goForward());
		 * SmartDashboard.putData("CameraToggle", new CameraToggle());
		 * SmartDashboard.putData("DriveTeleop", new DriveTeleop());
		 * SmartDashboard.putData("ShooterFeed", new ShooterFeed());
		 * SmartDashboard.putData("ShooterOut", new ShooterOut());
		 * SmartDashboard.putData("ShooterOff", new ShooterOff());
		 * SmartDashboard.putData("ActivateCannon", new ActivateCannon());
		 * SmartDashboard.putData("PlatformTiltToggle", new
		 * PlatformTiltToggle()); SmartDashboard.putData("Camera11", new
		 * Camera11()); SmartDashboard.putData("Camera12", new Camera12());
		 * SmartDashboard.putData("Camera21", new Camera21());
		 * SmartDashboard.putData("CameraSwitch", new CameraSwitch());
		 * SmartDashboard.putData("DriveAutoToRampart", new
		 * DriveAutoToRampart()); SmartDashboard.putData("DriveSlow", new
		 * DriveSlow()); SmartDashboard.putData("DriveFast", new DriveFast());
		 * SmartDashboard.putData("ShooterSlowOut", new ShooterSlowOut());
		 * SmartDashboard.putData("CameraLightToggle", new CameraLightToggle());
		 * SmartDashboard.putData("TurnTo: degrees", new TurnTo(0));
		 * SmartDashboard.putData("DriveOverMoat", new DriveOverMoat());
		 * SmartDashboard.putData("DriveOverRockWall", new DriveOverRockWall());
		 * SmartDashboard.putData("DriveThroughLowBar", new
		 * DriveThroughLowBar()); SmartDashboard.putData("AutoMoat", new
		 * AutoMoat()); SmartDashboard.putData("AutoLowBar", new AutoLowBar());
		 * SmartDashboard.putData("DriveFromPosition1", new
		 * DriveFromPosition1()); SmartDashboard.putData("DriveFromPosition2",
		 * new DriveFromPosition2());
		 * SmartDashboard.putData("DriveFromPosition3", new
		 * DriveFromPosition3()); SmartDashboard.putData("DriveFromPosition4",
		 * new DriveFromPosition4());
		 * SmartDashboard.putData("DriveFromPosition5", new
		 * DriveFromPosition5()); SmartDashboard.putData("ResetAngle", new
		 * ResetAngle()); SmartDashboard.putData("DetectTarget", new
		 * DetectTarget()); SmartDashboard.putData("TurnToTarget", new
		 * TurnToTarget()); SmartDashboard.putData("DriveReverse", new
		 * DriveReverse()); SmartDashboard.putData("LowShot", new LowShot());
		 * setEqualToRightJoy.whenReleased(new returnToNormal());
		 */

	}

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
	public Joystick getDriverLeft() {
		return driverLeft;
	}

	public Joystick getDriverRight() {
		return driverRight;
	}

	public Joystick getOperatorJoystick() {
		return operatorJoystick;
	}

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
}
