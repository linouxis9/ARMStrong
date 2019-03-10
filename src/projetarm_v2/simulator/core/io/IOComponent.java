/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.io;

import projetarm_v2.simulator.core.RamRegister;

public abstract class IOComponent {

	private final RamRegister register;
	public final int shift;
	public final int portNb;
	
	protected IOComponent(RamRegister port, int shift, int portNb) {
		this.register = port;
		this.shift = shift;
		this.portNb = portNb;
	}
	
	public boolean isOn() {
		return this.register.getBit(shift);
	}
	
	public void set(boolean value) {
		this.register.setBit(shift, value);
	}
	
	public long getPortAddress() {
		return this.register.getAddress();
	}
	
	public String toString() {
		return "[Adr: 0x" + Long.toHexString(this.register.getAddress()) + ", Bits N°" + this.shift + "]";
	}
}
