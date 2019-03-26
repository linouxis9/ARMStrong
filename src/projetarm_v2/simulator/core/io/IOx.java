/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.io;

import java.util.Arrays;
import java.util.List;

import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.core.RamRegister;

public class IOx {

	public static final int NUMBER_OF_SEGMENTS = 8;

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
	
	public IO8Segment newIO8Segment() {
		return newIO8Segment(getNextAvailableBit());
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
	
	public IO8Segment newIO8Segment(int bit) {
		if (!hasNAvailableSlot(NUMBER_OF_SEGMENTS)) {
			throw new RuntimeException("Too many IOComponents attached to that port already");
		}
		
		IOSegment[] segments = new IOSegment[NUMBER_OF_SEGMENTS];
		
		for (int i = 0; i < NUMBER_OF_SEGMENTS; i++) {
			segments[i] = newIOSegment(bit + i);
		}
		
		return new IO8Segment(segments);
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
		return noComponent + n <= REGISTER_SIZE;
	}
	
	public boolean hasAnAvailableSlot() {
		return noComponent < REGISTER_SIZE;
	}
}
