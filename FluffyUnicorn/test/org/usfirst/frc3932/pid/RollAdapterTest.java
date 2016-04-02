package org.usfirst.frc3932.pid;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RollAdapterTest {
	RollAdapter adapter = new RollAdapter();
	float yaw = 0f;
	float roll = 0f;
	
	@Before
	public void before() {
		yaw = 0f;
		roll = 0f;
	}
	
	@Test
	public void testWhenRoll90() {
		yaw = 5f;
		roll = 90f;
		yaw = adapter.adjustYaw(yaw, roll);
		assertTrue(yaw > 0f); 
	}
	
	@Test
	public void testWhenRollGT90() {
		yaw = 5f;
		roll = 90.01f;
		yaw = adapter.adjustYaw(yaw, roll);
		assertTrue(yaw < 0f); 
	}
	
	@Test
	public void testWhenRollGT90AndLT270() {
		yaw = 5f;
		roll = 269.9f;
		yaw = adapter.adjustYaw(yaw, roll);
		assertTrue(yaw < 0f); 
	}
	
	@Test
	public void testWhenRollGT270AndLT360() {
		yaw = 5f;
		roll = 270.1f;
		yaw = adapter.adjustYaw(yaw, roll);
		assertTrue(yaw > 0f); 
	}
	
	@Test
	public void testWhenRoll361() {
		yaw = 5f;
		roll = 361f;
		yaw = adapter.adjustYaw(yaw, roll);
		assertTrue(yaw > 0f); 
	}
	
	@Test
	public void testWhenRoll451() {
		yaw = 5f;
		roll = 451f;
		yaw = adapter.adjustYaw(yaw, roll);
		assertTrue(yaw < 0f); 
	}
	
	@Test
	public void testWhenRollNeg451() {
		yaw = 5f;
		roll = -451f;  //Would be -91 after mod
		yaw = adapter.adjustYaw(yaw, roll);
		assertTrue(yaw < 0f); 
	}
}
