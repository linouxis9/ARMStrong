package projetarm_v2.simulator.core.io;

import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.core.RamRegister;

public class IOx {

	private final int REGISTER_SIZE = 32;
	
	private RamRegister portX;
	private RamRegister dirX;

	private int currentBit = 0;
	
	private IOComponent[] components = new IOComponent[REGISTER_SIZE];
	
	public IOx(Ram ram, long portAddress, long dirAddress) {
		this.portX = new RamRegister(ram, portAddress);
		this.dirX = new RamRegister(ram, dirAddress);
	}

	public IOLed newIOLed() {
		if (currentBit >= REGISTER_SIZE-1) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		
		IOLed led = new IOLed(portX, currentBit);
		this.components[currentBit++] = led;
		return led;
	}

	public IOButton newIOButton() {
		if (currentBit >= REGISTER_SIZE-1) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		IOButton button = new IOButton(portX, currentBit);
		this.components[currentBit++] = button;
		return button;
	}
	
	public IOSwitch newIOSwitch() {
		if (currentBit >= REGISTER_SIZE-1) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		IOSwitch ioSwitch = new IOSwitch(portX, currentBit);
		this.components[currentBit++] = ioSwitch;
		return ioSwitch;
	}
	
	public IO7Segment newIO7Segment() {
		if (currentBit >= REGISTER_SIZE-1) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		
		IOSegment[] segments = new IOSegment[7];
		
		for (int i = 0; i < 7; i++) {
			segments[i] = newIOSegment();
		}
		return new IO7Segment(segments);
	}
	
	private IOSegment newIOSegment() {
		if (currentBit >= REGISTER_SIZE-1) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		IOSegment segment = new IOSegment(portX, currentBit);
		this.components[currentBit++] = segment;
		return segment;
	}
}
