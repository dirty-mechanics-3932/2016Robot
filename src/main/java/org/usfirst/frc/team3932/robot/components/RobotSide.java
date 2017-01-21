
package org.usfirst.frc.team3932.robot.components;

public enum RobotSide {
    FRONT, BACK, LEFT, RIGHT, FRONTLEFT, FRONTRIGHT, BACKLEFT, BACKRIGHT;

    public static boolean isLeft(RobotSide side) {
        switch (side) {
        case LEFT:
        case FRONTLEFT:
        case BACKLEFT:
            return true;
        default:
            return false;
        }
    }

    public static boolean isFront(RobotSide side) {
        switch (side) {
        case FRONT:
        case FRONTLEFT:
        case FRONTRIGHT:
            return true;
        default:
            return false;
        }
    }
}
