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

import org.usfirst.frc3932.commands.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc3932.subsystems.*;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
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

    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand())Robot.shooterWheels.off();;


    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public JoystickButton setEqualToRightJoy;
    public JoystickButton driveSlowButton;
    public JoystickButton driveFastButton;
    public JoystickButton cameraLightButton;
    public Joystick driverLeft;
    public JoystickButton shootButton;
    public JoystickButton camera11Button;
    public JoystickButton camera12Button;
    public JoystickButton camera21Button;
    public JoystickButton cameraSwitchButton;
    public Joystick driverRight;
    public JoystickButton shootOut;
    public JoystickButton shootFeed;
    public JoystickButton shootOff;
    public JoystickButton cameraToggleButton;
    public JoystickButton platformTiltToggleButton;
    public JoystickButton shootSlowOut;
    public Joystick operatorJoystick;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public OI() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

        operatorJoystick = new Joystick(2);
        
        shootSlowOut = new JoystickButton(operatorJoystick, 6);
        shootSlowOut.whenPressed(new ShooterSlowOut());
        platformTiltToggleButton = new JoystickButton(operatorJoystick, 5);
        platformTiltToggleButton.whenPressed(new PlatformTiltToggle());
        cameraToggleButton = new JoystickButton(operatorJoystick, 4);
        cameraToggleButton.whenPressed(new CameraToggle());
        shootOff = new JoystickButton(operatorJoystick, 3);
        shootOff.whenPressed(new ShooterOff());
        shootFeed = new JoystickButton(operatorJoystick, 2);
        shootFeed.whenPressed(new ShooterFeed());
        shootOut = new JoystickButton(operatorJoystick, 1);
        shootOut.whenPressed(new ShooterOut());
        driverRight = new Joystick(1);
        
        cameraSwitchButton = new JoystickButton(driverRight, 2);
        cameraSwitchButton.whenPressed(new CameraSwitch());
        camera21Button = new JoystickButton(driverRight, 5);
        camera21Button.whenPressed(new Camera21());
        camera12Button = new JoystickButton(driverRight, 4);
        camera12Button.whenPressed(new Camera12());
        camera11Button = new JoystickButton(driverRight, 3);
        camera11Button.whenPressed(new Camera11());
        shootButton = new JoystickButton(driverRight, 1);
        shootButton.whenPressed(new ActivateCannon());
        driverLeft = new Joystick(0);
        
        cameraLightButton = new JoystickButton(driverLeft, 6);
        cameraLightButton.whenPressed(new CameraLightToggle());
        driveFastButton = new JoystickButton(driverLeft, 4);
        driveFastButton.whenReleased(new DriveFast());
        driveSlowButton = new JoystickButton(driverLeft, 4);
        driveSlowButton.whileHeld(new DriveSlow());
        setEqualToRightJoy = new JoystickButton(driverLeft, 1);
        setEqualToRightJoy.whileHeld(new goForward());


        // SmartDashboard Buttons
        SmartDashboard.putData("TurnToTenDegrees", new TurnToTenDegrees());
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
        SmartDashboard.putData("Camera21", new Camera21());
        SmartDashboard.putData("CameraSwitch", new CameraSwitch());
        SmartDashboard.putData("DriveAutoToRampart", new DriveAutoToRampart());
        SmartDashboard.putData("DriveSlow", new DriveSlow());
        SmartDashboard.putData("DriveFast", new DriveFast());
        SmartDashboard.putData("ShooterSlowOut", new ShooterSlowOut());
        SmartDashboard.putData("CameraLightToggle", new CameraLightToggle());
        SmartDashboard.putData("TurnTo: degrees", new TurnTo(0));
        SmartDashboard.putData("DriveOverMoat", new DriveOverMoat());
        SmartDashboard.putData("DriveOverRockWall", new DriveOverRockWall());
        SmartDashboard.putData("DriveThroughLowBar", new DriveThroughLowBar());
        SmartDashboard.putData("AutoMoat", new AutoMoat());
        SmartDashboard.putData("AutoLowBar", new AutoLowBar());
        SmartDashboard.putData("TurnForOneSecond", new TurnForOneSecond());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        setEqualToRightJoy.whenReleased(new returnToNormal());
        
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

