
package org.usfirst.frc.team3932.robot.components;

import java.util.LinkedList;
import java.util.List;

import org.usfirst.frc.team3932.robot.components.configs.AHRSConfig;
import org.usfirst.frc.team3932.robot.components.configs.JoystickConfig;
import org.usfirst.frc.team3932.robot.components.configs.TalonConfig;

import lombok.Getter;
import lombok.Setter;

public class ComponentsConfiguration {
    private List<JoystickConfig> joysticks = new LinkedList<>();

    public boolean addJoystick(JoystickConfig config) {
        return joysticks.add(config);
    }

    public JoystickConfig[] getJoysticks() {
        return joysticks.toArray(new JoystickConfig[joysticks.size()]);
    }

    private List<TalonConfig> talons = new LinkedList<>();

    public boolean addTalon(TalonConfig config) {
        return talons.add(config);
    }

    public TalonConfig[] getTalons() {
        return talons.toArray(new TalonConfig[talons.size()]);
    }

    @Getter
    @Setter
    private AHRSConfig ahrs = null;
}
