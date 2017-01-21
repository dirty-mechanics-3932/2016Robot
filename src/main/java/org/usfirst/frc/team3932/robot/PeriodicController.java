
package org.usfirst.frc.team3932.robot;

import java.util.LinkedList;
import java.util.List;

import org.usfirst.frc.team3932.robot.Periodic.PeriodicMode;

public class PeriodicController {

    private final List<Periodic> periodics = new LinkedList<>();

    public void register(Periodic periodic) {
        periodics.add(periodic);
    }

    public void run(PeriodicMode mode) {
        periodics.stream().filter(p -> p.hasMode(mode)).forEach(p -> p.run(mode));
    }

    public boolean unregisterPeriodic(Periodic periodic) {
        return periodics.remove(periodic);
    }

}
