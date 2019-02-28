package projetarm_v2.simulator.core.io;

import java.util.Arrays;
import java.util.List;

import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.core.RamRegister;

public class IOx {

	private final static int REGISTER_SIZE = 8;
	
	private RamRegister portX;
	private RamRegister dirX;

	private int noComponent = 0;
	
	private IOComponent[] components = new IOComponent[REGISTER_SIZE];

	private int portNb;
	
	public IOx(Ram ram, int portNb, long portAddress, long dirAddress) {
		this.portX = new RamRegister(ram, portAddress);
		this.dirX = new RamRegister(ram, dirAddress);
		this.portNb = portNb;
	}

	public boolean removeComponent(IOComponent component) {
		int index = getComponentBit(component);
		
		if (components[index] == null) {
			return false;
		}
		
		noComponent--;
		
		components[index] = null;
		
		return true;
	}
	
	public IOLed newIOLed() {
		return newIOLed(getNextAvailableBit());
	}
	
	public IOButton newIOButton() {
		return newIOButton(getNextAvailableBit());
	}

	public IOSwitch newIOSwitch() {
		return newIOSwitch(getNextAvailableBit());
	}
	
	public IO7Segment newIO7Segment() {
		return newIO7Segment(getNextAvailableBit());
	}
	
	public IOLed newIOLed(int bit) {
		if (!hasAnAvailableSlot()) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		noComponent++;
		
		IOLed led = new IOLed(portX, bit, portNb);
		this.components[bit] = led;
		
		return led;
	}
	
	public IOButton newIOButton(int bit) {
		if (!hasAnAvailableSlot()) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		noComponent++;
		
		IOButton button = new IOButton(portX, bit, portNb);;
		this.components[bit] = button;
		
		return button;
	}
	
	public IOSwitch newIOSwitch(int bit) {
		if (!hasAnAvailableSlot()) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		noComponent++;
		
		IOSwitch ioSwitch = new IOSwitch(portX, bit, portNb);
		this.components[bit] = ioSwitch;
		
		return ioSwitch;
	}
	
	public IO7Segment newIO7Segment(int bit) {
		if (!hasNAvailableSlot(7)) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		
		IOSegment[] segments = new IOSegment[7];
		
		for (int i = 0; i < 7; i++) {
			segments[i] = newIOSegment(bit + i);
		}
		
		return new IO7Segment(segments);
	}
	
	private IOSegment newIOSegment(int bit) {
		if (!hasAnAvailableSlot()) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		noComponent++;
		
		IOSegment segment = new IOSegment(portX, bit, portNb);
		this.components[bit] = segment;
		
		return segment;
	}

	private int getComponentBit(IOComponent component) {
		return Arrays.asList(components).indexOf(component);
	}
	
	private int getNextAvailableBit() {
		return Arrays.asList(components).indexOf(null);
	}

	public List<IOComponent> getComponents() {
		return Arrays.asList(components);
	}
	
	public boolean hasNAvailableSlot(int n) {
		return noComponent + n < REGISTER_SIZE;
	}
	
	public boolean hasAnAvailableSlot() {
		return noComponent < REGISTER_SIZE-1;
	}
}
