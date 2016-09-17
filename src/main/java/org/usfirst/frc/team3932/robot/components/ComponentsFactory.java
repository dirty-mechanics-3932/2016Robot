
package org.usfirst.frc.team3932.robot.components;

import org.usfirst.frc.team3932.robot.components.configs.AHRSConfig;
import org.usfirst.frc.team3932.robot.components.configs.JoystickConfig;
import org.usfirst.frc.team3932.robot.components.configs.JoystickConfig.JoystickSide;
import org.usfirst.frc.team3932.robot.components.configs.TalonConfig;
import org.usfirst.frc.team3932.robot.components.configs.TalonPIDConfig;

import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.SPI;

public class ComponentsFactory {

    public static Components create() {
        ComponentsConfiguration config = new ComponentsConfiguration();
        try {
            create(config);
        } catch (CloneNotSupportedException e) {
            // Programming Error. This should never happen.
            // TODO: Implement correct error handling here.
        }
        Components components = new Components(config);
        return components;
    }

    private static void create(ComponentsConfiguration config) throws CloneNotSupportedException {
        AHRSConfig ahrsConfig = new AHRSConfig(SPI.Port.kMXP);
        ahrsConfig.setUpdateRateByInterval(20);

        // Integer argument is the channel of the joystick.
        JoystickConfig joyLeft = new JoystickConfig(0);
        JoystickConfig joyRight = new JoystickConfig(1);

        TalonConfig talLeft = new TalonConfig();
        talLeft.setChannel(2);
        talLeft.setEnableBrakeMode(false);
        talLeft.setEncoderTicksPerRevolution(360);
        talLeft.setCANUpdateInterval(20);
        talLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        talLeft.setGeneralFrame(20);
        talLeft.setFeedbackFrame(10);

        talLeft.setPIDProfileZero(new TalonPIDConfig());
        talLeft.setPIDProfileOne(new TalonPIDConfig());

        TalonConfig talRight = talLeft.clone();
        talRight.setChannel(3);

        config.setAhrs(ahrsConfig);
        config.addJoystick(JoystickSide.LEFT, joyLeft);
        config.addJoystick(JoystickSide.RIGHT, joyRight);

        config.addTalon(RobotSide.LEFT, talLeft);
        config.addTalon(RobotSide.RIGHT, talRight);
    }

}
