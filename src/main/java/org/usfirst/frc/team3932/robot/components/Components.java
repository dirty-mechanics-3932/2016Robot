
package org.usfirst.frc.team3932.robot.components;

import java.util.HashMap;

import org.usfirst.frc.team3932.robot.components.configs.AHRSConfig;
import org.usfirst.frc.team3932.robot.components.configs.JoystickConfig;
import org.usfirst.frc.team3932.robot.components.configs.TalonConfig;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;

public final class Components {

    private Joystick[] joysticks;
    private HashMap<RobotSide, CANTalon> talons = new HashMap<>();
    private AHRS ahrs;

    public Components(ComponentsConfiguration config) {
        JoystickConfig[] joys = config.getJoysticks();
        joysticks = new Joystick[joys.length];
        for (int i = 0; i < joys.length; i++)
            joysticks[i] = new Joystick(joys[i].getChannel());

        TalonConfig[] talonConfigs = config.getTalons();
        for (int i = 0; i < talonConfigs.length; i++) {
            TalonConfig talConfig = talonConfigs[i];
            CANTalon tal = new CANTalon(talonConfigs[i].getChannel());

            talons.put(talConfig.getSide(), tal);
        }

        AHRSConfig ahrsConfig = config.getAhrs();
        ahrs = new AHRS(ahrsConfig.getPort(), ahrsConfig.getUpdateRateHz());
    }

    public Joystick getJoystick(int index) {
        return joysticks[index];
    }

}
