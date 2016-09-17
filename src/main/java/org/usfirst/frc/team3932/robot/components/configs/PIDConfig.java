
package org.usfirst.frc.team3932.robot.components.configs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PIDConfig implements Cloneable {
    public PIDConfig clone() throws CloneNotSupportedException {
        return (PIDConfig) super.clone();
    }

    /**
     * Proportional constant
     */
    private double P;
    /**
     * Integration constant
     */
    private double I;
    /**
     * Differential constant
     */
    private double D;
    /**
     * Feedforward constant
     */
    private double F;
}
