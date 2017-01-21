
package org.usfirst.frc.team3932.robot;

import java.util.EnumSet;

public abstract class Periodic {

    public static enum PeriodicMode {
        TELEOP, AUTONOMOUS, TEST;
    }

    private final EnumSet<PeriodicMode> modes;

    protected Periodic(PeriodicController controller) {
        this.modes = EnumSet.allOf(PeriodicMode.class);
        controller.register(this);
    }

    protected Periodic(PeriodicController controller, PeriodicMode mode, PeriodicMode... rest) {
        this.modes = EnumSet.of(mode, rest);
        controller.register(this);
    }

    public boolean hasMode(PeriodicMode mode) {
        return modes.contains(mode);
    }

    public boolean addMode(PeriodicMode mode) {
        return modes.add(mode);
    }

    public boolean removeMode(PeriodicMode mode) {
        return modes.remove(mode);
    }

    public abstract void run(PeriodicMode mode);
}
