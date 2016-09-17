package org.usfirst.frc.team3932.robot.components.configs;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TalonPIDConfig extends PIDConfig implements Cloneable {

    public TalonPIDConfig clone() throws CloneNotSupportedException {
        return (TalonPIDConfig) super.clone();
    }

    /**
     * Integration zone -- prevents accumulation of integration error with large errors. Setting this to zero will ignore any izone stuff. The maximum error where Integral Accumulation will
     * occur during a closed-loop Mode.
     */
    private int integrationZone;
    /**
     * Maximum change in voltage, in volts / sec.
     */
    private double closeLoopRampRate;

    /**
     * In Talon Native Units for position and velocity close-loops.
     */
    private int allowableCloseLoopError;

    private boolean profile;

    public void setProfile(int i) {
        profile = i == 1;
    }

    /**
     * @return Either 1 or 0.
     */
    public int getProfile() {
        return profile ? 1 : 0;
    }
}
