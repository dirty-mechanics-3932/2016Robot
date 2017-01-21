
package org.usfirst.frc.team3932.robot;

import com.kauailabs.navx.frc.AHRS;

public class YawProvider extends Periodic {

    private final AHRS ahrs;

    public YawProvider(PeriodicController controller, AHRS ahrs) {
        super(controller);
        this.ahrs = ahrs;
    }

    private double lastYaw;

    @Override
    public void run(PeriodicMode mode) {
        lastYaw = getYaw();
    }

    public double getYaw() {
        return ahrs.getAngle();
    }

    public double getDeltaYaw() {
        return getYaw() - lastYaw;
    }

    public void zeroYaw() {
        ahrs.zeroYaw();
    }
}
