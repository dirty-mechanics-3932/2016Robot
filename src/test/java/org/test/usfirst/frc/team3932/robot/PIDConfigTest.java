package org.test.usfirst.frc.team3932.robot;

import org.junit.Test;
import org.usfirst.frc.team3932.robot.components.configs.PIDConfig;

import static org.junit.Assert.assertTrue;

import lombok.val;

public class PIDConfigTest {
    @Test
    public void correctConstructorOrder() {
        val config = new PIDConfig(0, 1, 2, 3);
        assertTrue(config.getP() == 0);
        assertTrue(config.getI() == 1);
        assertTrue(config.getD() == 2);
        assertTrue(config.getF() == 3);
    }
}
