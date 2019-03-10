/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core;

import java.io.Serializable;

public class RamRegister implements Register, Serializable {
	private static final long serialVersionUID = -7962046527990859276L;
	private final transient Ram ram;
	private final long myAddress;

	public RamRegister(Ram ram, long address) {
		this.ram = ram;
		this.myAddress = address;
	}
	
	@Override
	public int getValue() {
		return this.ram.getValue(this.myAddress);
	}

	@Override
	public void setValue(int value) {
		this.ram.setValue(this.myAddress, value);
	}
	
	public long getAddress() {
		return this.myAddress;
	}
}
