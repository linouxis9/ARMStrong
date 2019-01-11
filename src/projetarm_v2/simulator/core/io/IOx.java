package projetarm_v2.simulator.core.io;

import java.util.Arrays;

import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.core.RamRegister;

public class IOx {

	private final int REGISTER_SIZE = 8;
	
	private RamRegister portX;
	private RamRegister dirX;

	private int noComponent = 0;
	
	private IOComponent[] components = new IOComponent[REGISTER_SIZE];
	
	public IOx(Ram ram, long portAddress, long dirAddress) {
		this.portX = new RamRegister(ram, portAddress);
		this.dirX = new RamRegister(ram, dirAddress);
	}

	public void removeComponent(IOComponent component) {
		int index = getComponentBit(component);
		
		if (index == -1) {
			throw new RuntimeException("This component is not part of this IO Register or has already been removed.");
		}
		
		noComponent--;
		
		components[index] = null;
	}
	
	public IOLed newIOLed() {
		if (hasAnAvailableSlot()) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		noComponent++;
		
		int bit = getNextAvailableBit();
		IOLed led = new IOLed(portX, bit);
		this.components[bit] = led;
		
		return led;
	}

	public IOButton newIOButton() {
		if (!hasAnAvailableSlot()) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		noComponent++;
		
		int bit = getNextAvailableBit();
		IOButton button = new IOButton(portX, bit);
		addComponent(bit, button);
		
		return button;
	}
	
	public IOSwitch newIOSwitch() {
		if (!hasAnAvailableSlot()) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		noComponent++;
		
		int bit = getNextAvailableBit();
		IOSwitch ioSwitch = new IOSwitch(portX, bit);
		this.components[bit] = ioSwitch;
		
		return ioSwitch;
	}
	
	public IO7Segment newIO7Segment() {
		if (!hasNAvailableSlot(7)) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		
		IOSegment[] segments = new IOSegment[7];
		
		for (int i = 0; i < 7; i++) {
			segments[i] = newIOSegment();
		}
		
		return new IO7Segment(segments);
	}
	
	private IOSegment newIOSegment() {
		if (!hasAnAvailableSlot()) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		noComponent++;
		
		int bit = getNextAvailableBit();
		IOSegment segment = new IOSegment(portX, bit);
		this.components[bit] = segment;
		
		return segment;
	}
	
	private void addComponent(int bit, IOButton button) {
		this.components[bit] = button;
	}

	private int getComponentBit(IOComponent component) {
		return Arrays.asList(components).indexOf(component);
	}
	
	private int getNextAvailableBit() {
		return Arrays.asList(components).indexOf(null);
	}

	public boolean hasNAvailableSlot(int n) {
		return noComponent + n < REGISTER_SIZE;
	}
	
	public boolean hasAnAvailableSlot() {
		return noComponent < REGISTER_SIZE-1;
	}
}
