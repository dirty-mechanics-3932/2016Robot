
package org.usfirst.frc.team3932.robot;

import org.usfirst.frc.team3932.robot.Periodic.PeriodicMode;
import org.usfirst.frc.team3932.robot.components.Components;
import org.usfirst.frc.team3932.robot.components.ConfigFactory;
import org.usfirst.frc.team3932.robot.components.RobotSide;
import org.usfirst.frc.team3932.robot.components.configs.JoystickConfig.JoystickSide;
import org.usfirst.frc.team3932.robot.drive.LeftRightDrive;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import lombok.Getter;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as described in the IterativeRobot documentation. If you change the name of
 * this class or the package after creating this project, you must also update build.properties to reflect the change.
 */
public class Robot extends IterativeRobot {

    private Components components;
    private LeftRightDrive drive;

    @Getter
    private PeriodicController periodicController;

    /**
     * This function is run when the robot is first started up and should be used for any initialization code.
     */
    @Override
    public void robotInit() {
        periodicController = new PeriodicController();
        components = ConfigFactory.create(this);

        drive = new LeftRightDrive(components.getTalon(RobotSide.LEFT), components.getTalon(RobotSide.RIGHT), components.isTalonInverted(RobotSide.LEFT),
                components.isTalonInverted(RobotSide.RIGHT));
    }

    @Override
    public void disabledInit() {

    }

    /**
     * This function is run once each time the robot enters autonomous mode
     */
    @Override
    public void autonomousInit() {
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
    }

    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    @Override
    public void teleopInit() {
    }

    /**
     * This function is called periodically during operator control
     */
    private int i;

    @Override
    public void teleopPeriodic() {
        periodicController.run(PeriodicMode.TELEOP);
        Joystick left = components.getJoystick(JoystickSide.LEFT);
        Joystick right = components.getJoystick(JoystickSide.RIGHT);
        if (i % 20 == 0) {
            // System.out.println("X: " + left.getX() + "Y: " + left.getY() + " Z: " + left.getZ());
            // System.out.println("X: " + right.getX() + "Y: " + right.getY() + " Z: " + right.getZ());
        }

        drive.tankDrive(left.getY(), right.getY());
        i++;
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
    }
}
