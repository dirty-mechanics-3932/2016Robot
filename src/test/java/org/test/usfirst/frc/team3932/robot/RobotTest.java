
package org.test.usfirst.frc.team3932.robot;

import org.junit.Test;
import org.usfirst.frc.team3932.robot.Robot;

import static org.junit.Assert.*;

public class RobotTest {
    @Test
    public void testSanity() {
        Robot classUnderTest = new Robot();
        assertTrue(!classUnderTest.isDisabled() == classUnderTest.isEnabled());
    }
}
