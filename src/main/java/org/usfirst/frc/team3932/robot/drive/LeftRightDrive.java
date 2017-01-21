
package org.usfirst.frc.team3932.robot.drive;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import lombok.Getter;
import lombok.Setter;

public class LeftRightDrive {

    private final CANTalon leftTal;
    private final CANTalon rightTal;

    @Getter
    @Setter
    private boolean invertLeft;
    @Getter
    @Setter
    private boolean invertRight;

    public LeftRightDrive(CANTalon left, CANTalon right, boolean invertLeft, boolean invertRight) {
        leftTal = left;
        rightTal = right;
        this.invertLeft = invertLeft;
        this.invertRight = invertRight;

        leftTal.changeControlMode(TalonControlMode.PercentVbus);
        rightTal.changeControlMode(TalonControlMode.PercentVbus);
        tankDrive(0, 0);
    }

    public void tankDrive(double left, double right) {
        left = limitMagnitude(left);
        right = limitMagnitude(right);
        if (invertLeft)
            leftTal.set(-left);
        else
            leftTal.set(left);
        if (invertRight)
            rightTal.set(-right);
        else
            rightTal.set(right);
    }

    private static final double MAX_MAGNITUDE = 1;

    private double limitMagnitude(double magnitude) {
        if (magnitude < -MAX_MAGNITUDE)
            return -MAX_MAGNITUDE;
        if (magnitude > MAX_MAGNITUDE)
            return MAX_MAGNITUDE;
        return magnitude;
    }

    private static final double DEFAULT_CURVE_SENSITIVITY = 0.5;

    public void curveDrive(double magnitude, double curve) {
        curveDrive(magnitude, curve, DEFAULT_CURVE_SENSITIVITY);
    }

    /**
     * Drive the motors at "magnitude" and "curve". Both magnitude and curve are -1.0 to +1.0 values, where 0.0 represents stopped and not turning.
     * {@literal curve < 0 will turn left and curve > 0} will turn right.
     *
     * The algorithm for steering provides a constant turn radius for any normal speed range, both forward and backward. Increasing sensitivity causes sharper turns for fixed values of
     * curve.
     *
     *
     * @param magnitude
     *            The speed setting for the outside wheel in a turn, +1 to -1.
     * @param curve
     *            The rate of turn, constant for different forward speeds. Set {@literal curve < 0 for left turn or curve > 0 for right turn.} Set curve = e^(-r/w) to get a turn radius r for
     *            wheelbase w of your robot. Conversely, turn radius r = -ln(curve)*w for a given value of curve and wheelbase w.
     */
    public void curveDrive(double magnitude, double curve, double sensitivity) {
        double leftOutput, rightOutput;

        if (curve < 0) {
            double value = Math.log(-curve);
            double ratio = (value - sensitivity) / (value + sensitivity);
            if (ratio == 0)
                ratio = .0000000001;
            leftOutput = magnitude / ratio;
            rightOutput = magnitude;
        } else if (curve > 0) {
            double value = Math.log(curve);
            double ratio = (value - sensitivity) / (value + sensitivity);
            if (ratio == 0)
                ratio = .0000000001;
            leftOutput = magnitude;
            rightOutput = magnitude / ratio;
        } else {
            leftOutput = magnitude;
            rightOutput = magnitude;
        }
        tankDrive(leftOutput, rightOutput);
    }
}
