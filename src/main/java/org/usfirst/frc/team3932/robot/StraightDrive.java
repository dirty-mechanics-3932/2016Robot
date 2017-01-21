
package org.usfirst.frc.team3932.robot;

import org.usfirst.frc.team3932.robot.components.Components;

import com.kauailabs.navx.frc.AHRS;

public class StraightDrive {

    private AHRS ahrs;

    public StraightDrive(Components comps) {
        ahrs = comps.getAhrs();
        if (ahrs == null)
            throw new IllegalStateException("Must have a valid AHRS component!");
    }

    private static final double feedBackPoorManPID = 20;

    public void driveStraight(double magnitude) {
        double left = magnitude, right = magnitude;
        double initYaw = 0; // TODO
        double currentYaw = ahrs.getAngle();
        double deltaYaw = currentYaw - initYaw;
        double factorYaw = deltaYaw / feedBackPoorManPID;

        if (deltaYaw == 0)
            return;
        if (magnitude < 0) {
            if (deltaYaw < 0) {
                // compensate for left drive
                left = left * (1 - factorYaw);
            } else {
                // compensate for right drive
                right = right * (1 + factorYaw);
            }
        } else {
            if (deltaYaw < 0) {

                // compensate for left drive
                left = left * (1 + factorYaw);
            } else {
                // compensate for right drive
                right = right * (1 - factorYaw);

            }

        }
    }
}
