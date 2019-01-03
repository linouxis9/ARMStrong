package projetarm_v2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.core.Cpsr;
import unicorn.Unicorn;

class CpsrTdd {
	private ArmSimulator simulator;
	
	@BeforeEach
	public void Test() {
		this.simulator = new ArmSimulator();
		this.simulator.resetState();
	}
	
	@Test
	public void testN() {
		this.simulator.setProgram("mov r0,#20; mov r1,#15; cmp r0,r1");
		this.simulator.run();
		assertFalse(this.simulator.getN());
	}
	
	@Test
	public void testNnoValue() {
		assertFalse(this.simulator.getN());
	}
	
	@Test
	public void testZ() {
		this.simulator.setProgram("mov r0,#20; mov r1,#20; cmp r0,r1");
		this.simulator.run();
		assertTrue(this.simulator.getZ());
	}
	
	@Test
	public void testC() {
		this.simulator.setProgram("mov r0,#1; mov r1,#1; cmp r0,r1");
		this.simulator.run();
		assertTrue(this.simulator.getC());
	}

}
