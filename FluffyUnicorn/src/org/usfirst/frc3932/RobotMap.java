// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3932;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static CANTalon driveSystemLeftFront;
    public static CANTalon driveSystemLeftRear;
    public static CANTalon driveSystemRightFront;
    public static CANTalon driveSystemRightRear;
    public static RobotDrive driveSystemTankDrive;
    public static DoubleSolenoid platformPlatformSolenoid;
    public static DoubleSolenoid cameraCameraSolenoid;
    public static DoubleSolenoid cannonCannonSolenoid;
    public static Compressor onBoardCompressorRobotCompressor;
    public static CANTalon shooterWheelsLeftWheel;
    public static CANTalon shooterWheelsRightWheel;
    public static PowerDistributionPanel powerDistributionBoardPowerDistributionBoard;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public static void init() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        driveSystemLeftFront = new CANTalon(3);
        LiveWindow.addActuator("DriveSystem", "LeftFront", driveSystemLeftFront);
        
        driveSystemLeftRear = new CANTalon(5);
        LiveWindow.addActuator("DriveSystem", "LeftRear", driveSystemLeftRear);
        
        driveSystemRightFront = new CANTalon(2);
        LiveWindow.addActuator("DriveSystem", "RightFront", driveSystemRightFront);
        
        driveSystemRightRear = new CANTalon(4);
        LiveWindow.addActuator("DriveSystem", "RightRear", driveSystemRightRear);
        
        driveSystemTankDrive = new RobotDrive(driveSystemLeftFront, driveSystemLeftRear,
              driveSystemRightFront, driveSystemRightRear);
        
        driveSystemTankDrive.setSafetyEnabled(true);
        driveSystemTankDrive.setExpiration(0.1);
        driveSystemTankDrive.setSensitivity(0.5);
        driveSystemTankDrive.setMaxOutput(1.0);
        driveSystemTankDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        driveSystemTankDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        platformPlatformSolenoid = new DoubleSolenoid(0, 2, 3);
        LiveWindow.addActuator("Platform", "PlatformSolenoid", platformPlatformSolenoid);
        
        cameraCameraSolenoid = new DoubleSolenoid(0, 4, 5);
        LiveWindow.addActuator("Camera", "CameraSolenoid", cameraCameraSolenoid);
        
        cannonCannonSolenoid = new DoubleSolenoid(0, 0, 1);
        LiveWindow.addActuator("Cannon", "CannonSolenoid", cannonCannonSolenoid);
        
        onBoardCompressorRobotCompressor = new Compressor(0);
        
        
        shooterWheelsLeftWheel = new CANTalon(6);
        LiveWindow.addActuator("ShooterWheels", "LeftWheel", shooterWheelsLeftWheel);
        
        shooterWheelsRightWheel = new CANTalon(7);
        LiveWindow.addActuator("ShooterWheels", "RightWheel", shooterWheelsRightWheel);
        
        powerDistributionBoardPowerDistributionBoard = new PowerDistributionPanel(0);
        LiveWindow.addSensor("PowerDistributionBoard", "PowerDistributionBoard", powerDistributionBoardPowerDistributionBoard);
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }
}
