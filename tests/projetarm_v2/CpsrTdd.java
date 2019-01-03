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
	private Cpsr cpsr;
	
	@BeforeEach
	public void Test() {
		this.simulator = new ArmSimulator();
		this.simulator.resetState();
		this.cpsr = this.simulator.getCpu().getCPSR();
	}
	
	@Test
	public void testN() {
		this.simulator.setProgram("mov r0,#20; mov r1,#15; cmp r0,r1");
		this.simulator.run();
		assertFalse(this.simulator.getN());
		this.cpsr.setN(true);
		assertTrue(this.simulator.getN());
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
		this.cpsr.setZ(false);
		assertFalse(this.simulator.getZ());
	}
	
	@Test
	public void testC() {
		this.simulator.setProgram("mov r0,#1; mov r1,#1; cmp r0,r1");
		this.simulator.run();
		assertTrue(this.simulator.getC());
		this.cpsr.setC(false);
		assertFalse(this.simulator.getC());
	}
	
	@Test
	public void testV() {
		this.simulator.setProgram("mov r0, #0x80000000; mov  r1, #0x00000001; subs r2, r0, r1");
		this.simulator.run();
		assertTrue(this.simulator.getV());
		this.cpsr.setV(false);
		assertFalse(this.simulator.getV());
	}
	
	@Test
	public void testQ() {
		this.simulator.setProgram("mov r2,#0x70000000; qadd r3,r2,r2");
		this.simulator.run();
		assertTrue(this.simulator.getQ());
		this.cpsr.setQ(false);
		assertFalse(this.simulator.getQ());
	}

}
