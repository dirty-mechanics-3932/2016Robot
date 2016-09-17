package org.usfirst.frc.team3932.robot.components.configs;

import java.util.EnumMap;

import org.usfirst.frc.team3932.robot.components.RobotSide;

import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.StatusFrameRate;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TalonConfig implements Cloneable {

    public TalonConfig clone() throws CloneNotSupportedException {
        TalonConfig copy = (TalonConfig) super.clone();
        copy.pid = this.pid.clone();
        copy.controlFramePeriods = this.controlFramePeriods.clone();
        return copy;
    }

    private int channel;
    private RobotSide side = RobotSide.UNKNOWN;

    private TalonPIDConfig pid;

    /**
     * Default is PercentVbus. Only Current, Position, and Speed (Velocity) modes are closed-loop.
     */
    private TalonControlMode controlMode = TalonControlMode.PercentVbus;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int masterId;

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
     * @return The channel of the master Talon. -1 if {@link #controlMode} is not {@link edu.wpi.first.wpilibj.CANTalon.TalonControlMode#Follower Follower}.
     */
    public int getMasterId() {
        return controlMode == TalonControlMode.Follower ? masterId : -1;
    }

    /**
     * Limits the rate at which the throttle will change, in volts / sec. Affects all modes.
     */
    private int voltageRampRate;

    // These only effect the closed loop logic.
    private boolean reverseCloseLoopOutput;
    private boolean reverseCloseLoopSensor;

    private boolean safetyEnabled;
    private double safetyExpireationSeconds;

    /**
     * Default is QuadratureEncoder.
     */
    private FeedbackDevice feedbackDevice;
    private int encoderTicksPerRevolution;

    /**
     * Between -12 and +12.
     */
    private int peakOutputVoltage;
    /**
     * Between -12 and +12.
     */
    private int nominalOutputVoltage;

    /**
     * True for break, false for coast.
     */
    private boolean enableBreakMode;

    // Limit switches and soft limits are a feature of Talons, but are not implemented here.
    // Auto clearing of Position using Index Pin is supported but not implemented here.

    // (Appears to be) Volts per 100ms to change voltage by in the Voltage TalonControlMode
    // private double voltageCompensationRampRate;

    /**
     * The period in ms to send the CAN control frame. Default is 10ms.
     * 
     * The control frame provides the control mode, feedback sensor, target/set points or duty cycle, (voltage) ramp rate, and overrides and reversal settings.
     */
    private int CANUpdateInterval = 10;

    @Setter(AccessLevel.NONE)
    private EnumMap<StatusFrameRate, Integer> controlFramePeriods = new EnumMap<StatusFrameRate, Integer>(StatusFrameRate.class);

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