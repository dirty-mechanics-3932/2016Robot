
package org.usfirst.frc.team3932.robot.components;

import org.usfirst.frc.team3932.robot.Robot;
import org.usfirst.frc.team3932.robot.components.configs.AHRSConfig;
import org.usfirst.frc.team3932.robot.components.configs.DriveControllerConfig;
import org.usfirst.frc.team3932.robot.components.configs.JoystickConfig;
import org.usfirst.frc.team3932.robot.components.configs.JoystickConfig.JoystickSide;
import org.usfirst.frc.team3932.robot.components.configs.TalonConfig;
import org.usfirst.frc.team3932.robot.components.configs.TalonPIDConfig;

import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.SPI;

public class ConfigFactory {

    public static Components create(Robot robot) {
        ComponentsConfiguration config = new ComponentsConfiguration();
        try {
            initilize(config);
        } catch (CloneNotSupportedException e) {
            // Programming Error. This should never happen.
            // TODO: Implement correct error handling here.
        }
        return new Components(robot, config);
    }

    private static void initilize(final ComponentsConfiguration config) throws CloneNotSupportedException {
        AHRSConfig ahrsConfig = new AHRSConfig(SPI.Port.kMXP);
        ahrsConfig.setUpdateRateByInterval(20);

        // Integer argument is the channel of the joystick.
        JoystickConfig joyLeft = new JoystickConfig(0);
        JoystickConfig joyRight = new JoystickConfig(1);

        TalonConfig talLeft = new TalonConfig();
        talLeft.setChannel(3);
        talLeft.setEnableBrakeMode(false);
        talLeft.setEncoderTicksPerRevolution(360);
        talLeft.setCANUpdateInterval(20);
        talLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        talLeft.setGeneralFrame(20);
        talLeft.setFeedbackFrame(20);
        talLeft.setSafetyEnabled(false);

        talLeft.setPIDProfileZero(new TalonPIDConfig());
        talLeft.setPIDProfileOne(new TalonPIDConfig());

        talLeft.setInvertMotorOutput(true);

        TalonConfig talRight = talLeft.clone();
        talRight.setChannel(2);
        talRight.setInvertMotorOutput(false);

        config.setAhrs(ahrsConfig);
        config.addJoystick(JoystickSide.LEFT, joyLeft);
        config.addJoystick(JoystickSide.RIGHT, joyRight);

        config.addTalon(RobotSide.LEFT, talLeft);
        config.addTalon(RobotSide.RIGHT, talRight);

        DriveControllerConfig driveController = new DriveControllerConfig();
        config.setDriveController(driveController);
    }

}
