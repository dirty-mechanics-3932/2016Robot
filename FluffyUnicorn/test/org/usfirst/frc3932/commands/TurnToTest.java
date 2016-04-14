package org.usfirst.frc3932.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TurnToTest {
	TurnTo turnTo;

	@Before
	public void setUp() throws Exception {
		turnTo = spy(new TurnTo(90));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOutput() throws Exception {
		double[] pAt180 = TurnTo.COMPETITION_P[0];
		assertEquals(toDouble(turnTo.getOutput(181d)), toDouble(pAt180[1]));
		assertEquals(toDouble(turnTo.getOutput(180d)), toDouble(pAt180[1]));
		assertEquals(toDouble(turnTo.getOutput(179d)), toDouble(pAt180[1]));
		assertEquals(toDouble(turnTo.getOutput(21d)), toDouble(pAt180[1]));
		double[] pAt20 = TurnTo.COMPETITION_P[1];
		assertEquals(toDouble(turnTo.getOutput(20d)), toDouble(pAt20[1]));
		assertEquals(toDouble(turnTo.getOutput(0d)), toDouble(-0d));
	}
	
	Double toDouble(double d) {
		return new Double(d);
	}

}
