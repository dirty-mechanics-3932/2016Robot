
package org.usfirst.frc.team3932.robot.components;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.usfirst.frc.team3932.robot.Robot;
import org.usfirst.frc.team3932.robot.components.configs.AHRSConfig;
import org.usfirst.frc.team3932.robot.components.configs.JoystickConfig;
import org.usfirst.frc.team3932.robot.components.configs.JoystickConfig.JoystickSide;
import org.usfirst.frc.team3932.robot.components.configs.TalonConfig;
import org.usfirst.frc.team3932.robot.components.configs.TalonPIDConfig;
import org.usfirst.frc.team3932.robot.drive.DriveController;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.StatusFrameRate;
import com.ctre.CANTalon.TalonControlMode;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Joystick;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public class Components {

    private final EnumMap<JoystickSide, Joystick> joysticks = new EnumMap<>(JoystickSide.class);

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    private class TalonKey {
        private CANTalon talon;
        private boolean isInverted;
    }

    private final EnumMap<RobotSide, TalonKey> talons = new EnumMap<>(RobotSide.class);
    @Getter
    private final AHRS ahrs;

    @Getter
    private final DriveController driveController;

    public Components(Robot robot, ComponentsConfiguration config) {
        Iterator<Entry<JoystickSide, JoystickConfig>> joys = config.getJoysticks().iterator();
        while (joys.hasNext()) {
            Entry<JoystickSide, JoystickConfig> entry = joys.next();
            joysticks.put(entry.getKey(), new Joystick(entry.getValue().getChannel()));
        }

        Iterator<Entry<RobotSide, TalonConfig>> talonConfigs = config.getTalons().iterator();
        while (talonConfigs.hasNext()) {
            Entry<RobotSide, TalonConfig> entry = talonConfigs.next();
            TalonConfig talConf = entry.getValue();
            CANTalon tal = new CANTalon(talConf.getChannel(), talConf.getCANUpdateInterval());
            if (talConf.getVoltageRampRate() != 0)
                tal.setVoltageRampRate(talConf.getVoltageRampRate());
            tal.reverseOutput(talConf.getInvertCloseLoopOutput());
            tal.reverseSensor(talConf.getInvertCloseLoopSensor());
            if (talConf.getFeedbackDevice() != FeedbackDevice.QuadEncoder)
                tal.setFeedbackDevice(talConf.getFeedbackDevice());
            if (talConf.getEncoderTicksPerRevolution() != 0)
                tal.configEncoderCodesPerRev(talConf.getEncoderTicksPerRevolution());
            if (talConf.getMaxOutputVoltageForward() != 0 || talConf.getMaxOutputVoltageReverse() != 0)
                tal.configPeakOutputVoltage(talConf.getMaxOutputVoltageForward(), talConf.getMaxOutputVoltageReverse());
            if (talConf.getNominalOutputVoltageForward() != 0 || talConf.getNominalOutputVoltageReverse() != 0)
                tal.configNominalOutputVoltage(talConf.getNominalOutputVoltageForward(), talConf.getNominalOutputVoltageReverse());
            tal.enableBrakeMode(talConf.getEnableBrakeMode());

            TalonPIDConfig zero = talConf.getPIDProfileZero();
            if (zero != null) {
                tal.setPID(zero.getP(), zero.getI(), zero.getD(), zero.getF(), zero.getIntegrationZone(), zero.getAllowableCloseLoopError(), 0);
                tal.setCloseLoopRampRate(zero.getCloseLoopRampRate());
            }

            TalonPIDConfig one = talConf.getPIDProfileZero();
            if (one != null) {
                tal.setPID(one.getP(), one.getI(), one.getD(), one.getF(), one.getIntegrationZone(), one.getAllowableCloseLoopError(), 0);
                tal.setCloseLoopRampRate(one.getCloseLoopRampRate());
            }

            tal.setProfile(talConf.getActiveProfile());

            Iterator<Entry<StatusFrameRate, Integer>> periods = talConf.getControlFramePeriods().iterator();
            while (periods.hasNext()) {
                Entry<StatusFrameRate, Integer> frameEntry = periods.next();
                tal.setStatusFrameRateMs(frameEntry.getKey(), frameEntry.getValue());
            }

            if (talConf.getSafetyExpireationSeconds() != 0)
                tal.setExpiration(talConf.getSafetyExpireationSeconds());
            tal.setSafetyEnabled(talConf.getSafetyEnabled());

            if (talConf.getControlMode() != TalonControlMode.PercentVbus) {
                tal.changeControlMode(talConf.getControlMode());
                if (talConf.getControlMode() == TalonControlMode.Follower && talConf.getMasterId() > 0)
                    tal.set(talConf.getMasterId());
                else
                    tal.set(0); // Calling set is required to change the control mode
            }

            talons.put(entry.getKey(), new TalonKey(tal, talConf.getInvertMotorOutput()));
        }

        AHRSConfig ahrsConfig = config.getAhrs();
        ahrs = ahrsConfig == null ? null : new AHRS(ahrsConfig.getPort(), ahrsConfig.getUpdateRateHz());

        driveController = config.getDriveController() == null ? null : new DriveController(robot, this, config.getDriveController());
    }

    public Joystick getJoystick(JoystickSide side) {
        return joysticks.get(side);
    }

    public CANTalon getTalon(RobotSide side) {
        return talons.get(side).talon;
    }

    public boolean isTalonInverted(RobotSide side) {
        return talons.get(side).isInverted;
    }
}
