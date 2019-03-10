/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleRegister implements Register {
	private AtomicInteger value = new AtomicInteger(0);
	
	public SimpleRegister() {}

	public SimpleRegister(int value) {
		this.value.set(value);
	}
	
	public int getValue() {
		return this.value.get();
	}

	@Override
	public void setValue(int value) {
		this.value.set(value);
	}

}
