package org.usfirst.frc.team3932.robot.components;

import org.usfirst.frc.team3932.robot.components.configs.AHRSConfig;
import org.usfirst.frc.team3932.robot.components.configs.JoystickConfig;
import org.usfirst.frc.team3932.robot.components.configs.TalonConfig;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

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
        talLeft.setEnableBreakMode(false);
        talLeft.setEncoderTicksPerRevolution(360);
        talLeft.setCANUpdateInterval(20);
        talLeft.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        talLeft.setGeneralFrame(20);
        talLeft.setFeedbackFrame(10);

        TalonConfig talRight = talLeft.clone();
        talRight.setChannel(3);

        config.setAhrs(ahrsConfig);
        config.addJoystick(joyLeft);
        config.addJoystick(joyRight);

        config.addTalon(talLeft);
        config.addTalon(talRight);
    }

}
