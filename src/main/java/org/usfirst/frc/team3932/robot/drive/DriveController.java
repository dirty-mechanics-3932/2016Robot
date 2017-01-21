
package org.usfirst.frc.team3932.robot.drive;

import org.usfirst.frc.team3932.robot.Periodic;
import org.usfirst.frc.team3932.robot.Robot;
import org.usfirst.frc.team3932.robot.components.Components;
import org.usfirst.frc.team3932.robot.components.RobotSide;
import org.usfirst.frc.team3932.robot.components.configs.DriveControllerConfig;

import edu.wpi.first.wpilibj.Joystick;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class DriveController extends Periodic {

    public static enum DriveMode {
        TANK, ARCADE;
    }

    private Joystick leftInput;
    private Joystick rightInput;

    private Joystick.AxisType leftInputAxis;
    private Joystick.AxisType rightInputAxis;

    private final LeftRightDrive drive;

    @Getter
    @Setter
    private boolean driveStraight;

    @Getter
    @Setter
    @NonNull
    private DriveMode driveMode;

    public DriveController(Robot robot, Components comps, DriveControllerConfig config) {
        super(robot.getPeriodicController());
        leftInput = comps.getJoystick(config.getLeftInputSide());
        rightInput = comps.getJoystick(config.getRightInputSide());

        leftInputAxis = config.getLeftInputAxis();
        rightInputAxis = config.getRightInputAxis();

        driveMode = config.getDriveMode();

        RobotSide left = config.getLeftOutputSide();
        RobotSide right = config.getRightOutputSide();
        drive = new LeftRightDrive(comps.getTalon(left), comps.getTalon(right), comps.isTalonInverted(left), comps.isTalonInverted(right));

        // TODO: Make Yaw/Signal provider Periodic class.
    }

    public void run(PeriodicMode mode) {
        if (mode != PeriodicMode.TELEOP)
            return;
        if (driveMode == DriveMode.TANK) {
            double left, right;
            left = leftInput.getAxis(leftInputAxis);
            right = rightInput.getAxis(rightInputAxis);
            // TODO: Put axis value manipulation logic here
            drive.tankDrive(left, right);
        } // TODO: Implement Arcade Drive
    }
}
