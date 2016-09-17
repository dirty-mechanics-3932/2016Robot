
package org.usfirst.frc.team3932.robot.components.configs;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TalonPIDConfig extends PIDConfig implements Cloneable {
    public TalonPIDConfig clone() throws CloneNotSupportedException {
        return (TalonPIDConfig) super.clone();
    }

    public TalonPIDConfig(double p, double i, double d, double f, int iZone, double closeRampRate, int closeLoopError) {
        super(p, i, d, f);
        this.integrationZone = iZone;
        this.closeLoopRampRate = closeRampRate;
        this.allowableCloseLoopError = closeLoopError;
    }

    public TalonPIDConfig(double p, double i, double d, double f) {
        super(p, i, d, f);
    }

    public TalonPIDConfig(PIDConfig pid) {
        super(pid.getP(), pid.getI(), pid.getD(), pid.getF());
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
}
