
package org.usfirst.frc.team3932.robot.components;

import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.Set;

import org.usfirst.frc.team3932.robot.components.configs.AHRSConfig;
import org.usfirst.frc.team3932.robot.components.configs.JoystickConfig;
import org.usfirst.frc.team3932.robot.components.configs.JoystickConfig.JoystickSide;
import org.usfirst.frc.team3932.robot.components.configs.TalonConfig;

import lombok.Getter;
import lombok.Setter;

public class ComponentsConfiguration {
    private final EnumMap<JoystickSide, JoystickConfig> joysticks = new EnumMap<>(JoystickSide.class);

    public Set<Entry<JoystickSide, JoystickConfig>> getJoysticks() {
        return joysticks.entrySet();
    }

    public void addJoystick(JoystickSide side, JoystickConfig config) {
        joysticks.put(side, config);
    }

    private final EnumMap<RobotSide, TalonConfig> talons = new EnumMap<>(RobotSide.class);

    public Set<Entry<RobotSide, TalonConfig>> getTalons() {
        return talons.entrySet();
    }

    public void addTalon(RobotSide side, TalonConfig config) {
        talons.put(side, config);
    }

    @Getter
    @Setter
    private AHRSConfig ahrs = null;
}
