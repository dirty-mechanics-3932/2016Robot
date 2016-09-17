
package org.usfirst.frc.team3932.robot;

import org.usfirst.frc.team3932.robot.components.Components;
import org.usfirst.frc.team3932.robot.components.ComponentsFactory;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as described in the IterativeRobot documentation. If you change the name of
 * this class or the package after creating this project, you must also update build.properties to reflect the change.
 */
public class Robot extends IterativeRobot {

    private Components components;

    /**
     * This function is run when the robot is first started up and should be used for any initialization code.
     */
    public void robotInit() {
        components = ComponentsFactory.create();
    }

    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        // TODO: Make CUSTOM RobotDrive class that works.
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
