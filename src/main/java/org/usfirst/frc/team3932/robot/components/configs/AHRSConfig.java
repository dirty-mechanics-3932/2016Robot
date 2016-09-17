
package org.usfirst.frc.team3932.robot.components.configs;

import edu.wpi.first.wpilibj.SPI;
import lombok.Data;
import lombok.NonNull;

@Data
public class AHRSConfig {
    @NonNull
    private SPI.Port port; /* Alternatives: SPI.Port.kMXP, I2C.Port.kMXP, SerialPort.Port.kUS, or SerialPort.Port.kMXP */
    private byte updateRateHz = (byte) 50;

    /**
     * Sets {@link updateRateHz}.
     *
     * @param miliseconds
     *            Interal in ms to convert to Hertz.
     */
    public void setUpdateRateByInterval(int miliseconds) {
        updateRateHz = (byte) (1000 / miliseconds);
    }
}
