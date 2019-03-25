/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.syscalls;

import java.io.IOException;

import projetarm_v2.simulator.core.Cpu;

public class SVCCallTime extends SVCCall {

	public SVCCallTime(Cpu cpu) {
		super(cpu);
	}

	@Override
	protected int getSvcNumber() {
		return 0x11;
	}

	@Override
	protected void primitive() {
		this.getCpu().getRegister(0).setValue((int)java.time.Instant.now().getEpochSecond());
	}

}
