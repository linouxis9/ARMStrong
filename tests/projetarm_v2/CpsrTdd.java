package projetarm_v2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.core.Cpsr;
import unicorn.Unicorn;

class CpsrTdd {
	
	@Test
	public void testN() {
		ArmSimulator simulator = new ArmSimulator();
		simulator.setProgram("mov r0,#20; mov r1,#15; cmp r0,r1");
		simulator.run();
		assertFalse(simulator.getN());
	}

}
