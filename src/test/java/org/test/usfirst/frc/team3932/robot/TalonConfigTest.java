
package org.test.usfirst.frc.team3932.robot;

import org.junit.BeforeClass;
import org.junit.Test;
import org.usfirst.frc.team3932.robot.components.configs.TalonConfig;
import org.usfirst.frc.team3932.robot.components.configs.TalonPIDConfig;

import com.rits.cloning.Cloner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class TalonConfigTest {

    private static Cloner cloner;

    @BeforeClass
    public static void createCloner() {
        cloner = new Cloner();
    }

    @Test
    public void defaultControlMode() {
        assertTrue(new TalonConfig().getControlMode() == TalonControlMode.PercentVbus);
    }

    @Test
    public void profileIsZeroOrOne() {
        TalonConfig config = new TalonConfig();
        assertTrue(config.getActiveProfile() == 0);
        config.setActiveProfile(1);
        assertTrue(config.getActiveProfile() == 1);
        config.setActiveProfile(123);
        assertTrue(config.getActiveProfile() == 0);
        config.setActiveProfile(-99999);
        assertTrue(config.getActiveProfile() == 0);
        config.setActiveProfile(1);
        assertTrue(config.getActiveProfile() == 1);
    }

    @Test
    public void equalsWorksCorrectly() {
        TalonConfig config1 = new TalonConfig();
        TalonConfig config2 = new TalonConfig();

        assertTrue(config1.equals(config1));
        assertFalse(config1.equals(null));

        config1.setPIDProfileOne(new TalonPIDConfig());
        assertFalse(config1.equals(config2));
        config2.setPIDProfileOne(new TalonPIDConfig());
        assertTrue(config1.equals(config2));

        config1.setFeedbackFrame(123);
        assertFalse(config1.equals(config2));
        config2.setFeedbackFrame(123);
        assertTrue(config1.equals(config2));
    }

    @Test
    public void settingMasterIdSetsControlMode() {
        TalonConfig config = new TalonConfig();
        assertTrue(config.getMasterId() == -1);
        config.setMasterId(16);
        assertTrue(config.getMasterId() == 16);
        assertTrue(config.getControlMode() == TalonControlMode.Follower);
        config.setMasterId(32);
        config.setControlMode(TalonControlMode.PercentVbus);
        assertTrue(config.getMasterId() == -1);
        config.setControlMode(TalonControlMode.Follower);
        assertTrue(config.getMasterId() == 32);
        config.setControlMode(TalonControlMode.PercentVbus);
        assertTrue(config.getMasterId() == -1);
    }

    @Test(expected = NullPointerException.class)
    public void cannotSetControlModeToNull() {
        new TalonConfig().setControlMode(null);
    }

    @Test
    public void cloneIsImplemented() throws CloneNotSupportedException {
        new TalonConfig().clone();
    }

    @Test
    public void cloneWorksProperly() throws CloneNotSupportedException {
        TalonConfig config = new TalonConfig();
        cloneWorksProperly(config);
        config.setActiveProfile(1);
        cloneWorksProperly(config);
        config.setMasterId(24);
        cloneWorksProperly(config);
        config.setControlMode(TalonControlMode.Speed);
        cloneWorksProperly(config);
        config.setMasterId(3);
        cloneWorksProperly(config);
        config.setPIDProfileOne(new TalonPIDConfig(0.5, 0.1, 2, 4));
        cloneWorksProperly(config);
        config.setPIDProfileZero(new TalonPIDConfig(3, 0, 0.1, 0));
        cloneWorksProperly(config);
        config.setGeneralFrame(36);
        cloneWorksProperly(config);
        config.setAnalogFrame(12);
        config.setFeedbackFrame(64);
        cloneWorksProperly(config);
        config.setActiveProfile(123);
        cloneWorksProperly(config);
    }

    public void cloneWorksProperly(TalonConfig config) throws CloneNotSupportedException {
        System.out.println("Testing clone with: " + config.toString());
        TalonConfig clone = config.clone();
        assertFalse(config == clone);
        assertTrue(config.equals(clone));
        assertTrue(clone.equals(config));

        TalonConfig deepClone = cloner.deepClone(config);
        assertFalse(config == deepClone);
        assertTrue(config.equals(deepClone));
        assertTrue(deepClone.equals(config));

        assertFalse(clone == deepClone);
        assertTrue(clone.equals(deepClone));
        assertTrue(deepClone.equals(clone));
    }
}
