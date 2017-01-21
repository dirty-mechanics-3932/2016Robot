
package org.usfirst.frc.team3932.robot.components.configs;

import org.usfirst.frc.team3932.robot.components.RobotSide;
import org.usfirst.frc.team3932.robot.components.configs.JoystickConfig.JoystickSide;
import org.usfirst.frc.team3932.robot.drive.DriveController.DriveMode;

import edu.wpi.first.wpilibj.Joystick;
import lombok.Data;

@Data
public class DriveControllerConfig {
    private JoystickSide leftInputSide = JoystickSide.LEFT;
    private JoystickSide rightInputSide = JoystickSide.RIGHT;

    private Joystick.AxisType leftInputAxis = Joystick.AxisType.kY;
    private Joystick.AxisType rightInputAxis = Joystick.AxisType.kY;

    private RobotSide leftOutputSide = RobotSide.LEFT;
    private RobotSide rightOutputSide = RobotSide.RIGHT;

    private DriveMode driveMode = DriveMode.TANK;
}
