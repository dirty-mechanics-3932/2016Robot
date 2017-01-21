
package org.usfirst.frc.team3932.robot.components.configs;

import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.Set;

import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.StatusFrameRate;
import com.ctre.CANTalon.TalonControlMode;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Data
public class TalonConfig implements Cloneable {

    public TalonConfig clone() throws CloneNotSupportedException {
        TalonConfig copy = (TalonConfig) super.clone();
        if (this.PIDProfileZero != null)
            copy.PIDProfileZero = this.PIDProfileZero.clone();
        if (this.PIDProfileOne != null)
            copy.PIDProfileOne = this.PIDProfileOne.clone();
        copy.controlFramePeriods = this.controlFramePeriods.clone();
        return copy;
    }

    private int channel;

    private TalonPIDConfig PIDProfileZero;
    private TalonPIDConfig PIDProfileOne;

    private boolean activeProfile;

    public void setActiveProfile(int i) {
        activeProfile = i == 1;
    }

    /**
     * @return Either 1 or 0.
     */
    public int getActiveProfile() {
        return activeProfile ? 1 : 0;
    }

    /**
     * Default is PercentVbus. Only Current, Position, and Speed (Velocity) modes are closed-loop. Voltage Compensation Mode not implemented here.
     */
    @NonNull
    private TalonControlMode controlMode = TalonControlMode.PercentVbus;

    public void setControlMode(@NonNull TalonControlMode controlMode) {
        this.controlMode = controlMode;
        if (controlMode == TalonControlMode.Follower)
            masterId = lastMasterId;
        else {
            lastMasterId = masterId;
            masterId = -1;
        }
    }

    /**
     * The channel of the master Talon. -1 if {@link #controlMode} is not {@link edu.wpi.first.wpilibj.CANTalon.TalonControlMode#Follower Follower}.
     */
    @Setter(AccessLevel.NONE)
    private int masterId = -1;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private transient int lastMasterId;

    /**
     * Will make this Talon a slave by setting the {@link #controlMode} to {@link edu.wpi.first.wpilibj.CANTalon.TalonControlMode#Follower Follower}.
     *
     * @param id
     *            Channel of the master Talon.
     */
    public void setMasterId(int id) {
        controlMode = TalonControlMode.Follower;
        masterId = id;
    }

    /**
     * Limits the rate at which the throttle will change, in volts / sec. Affects all modes.
     */
    private int voltageRampRate;

    // These only effect the closed loop logic.
    private boolean invertCloseLoopOutput;
    private boolean invertCloseLoopSensor;

    // Not implemented by CANTalon, but manually implemented in this project.
    private boolean invertMotorOutput;

    private boolean safetyEnabled;
    private double safetyExpireationSeconds;

    /**
     * Default is QuadratureEncoder.
     */
    private FeedbackDevice feedbackDevice = FeedbackDevice.QuadEncoder;
    private int encoderTicksPerRevolution;

    /**
     * Between -12 and +12.
     */
    private int maxOutputVoltageForward;
    private int maxOutputVoltageReverse;
    /**
     * Between -12 and +12.
     */
    private int nominalOutputVoltageForward;
    private int nominalOutputVoltageReverse;

    /**
     * True for brake, false for coast.
     */
    private boolean enableBrakeMode;

    // Limit switches and soft limits are a feature of Talons, but are not implemented here.
    // Auto clearing of Position using Index Pin is supported but not implemented here.

    // (Appears to be) Volts per 100ms to change voltage by in the Voltage TalonControlMode
    // private double voltageCompensationRampRate;

    /**
     * The period in ms to send the CAN control frame. Default is 10ms. Between 1 and 95ms.
     *
     * The control frame provides the control mode, feedback sensor, target/set points or duty cycle, (voltage) ramp rate, and overrides and reversal settings.
     */
    private int CANUpdateInterval = 10;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @NonNull
    private EnumMap<StatusFrameRate, Integer> controlFramePeriods = new EnumMap<StatusFrameRate, Integer>(StatusFrameRate.class);

    public Set<Entry<StatusFrameRate, Integer>> getControlFramePeriods() {
        return controlFramePeriods.entrySet();
    }

    /**
     * The General frame provides closed loop error, throttle, limit switch pins, fault bits, applied control mode.
     *
     * @param period
     *            Interval of frame in ms. Default 10ms.
     */
    public void setGeneralFrame(int period) {
        controlFramePeriods.put(StatusFrameRate.General, period);
    }

    /**
     * The Feedback frame provides sensor position and velocity, motor current, sticky faults, brake neutral state, and motor control profile select.
     *
     * @param period
     *            Interval of frame in ms. Default 20ms.
     */
    public void setFeedbackFrame(int period) {
        controlFramePeriods.put(StatusFrameRate.Feedback, period);
    }

    /**
     * The Quadrature Encoder frame provides encoder position and velocity, number of rising edges counted on the index pin, and quad a, b, and index pin states.
     *
     * @param period
     *            Interval of frame in ms. Default 100ms.
     */
    public void setQuadratureEncoderFrame(int period) {
        controlFramePeriods.put(StatusFrameRate.QuadEncoder, period);
    }

    /**
     * The Analog frame provides analog position and velocity, temperature, and battery voltage.
     *
     * @param period
     *            Interval of frame in ms. Default 100ms.
     */
    public void setAnalogFrame(int period) {
        controlFramePeriods.put(StatusFrameRate.AnalogTempVbat, period);
    }
}
