/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.syscalls;

import projetarm_v2.simulator.core.Cpu;

public class SVCLinuxHandler extends SVCCall {

	public SVCLinuxHandler(Cpu cpu) {
		super(cpu);
	}

	@Override
	protected int getSvcNumber() {
		return 0;
	}

	@Override
	protected void primitive() {
		System.out.println("[WARNING] This is the Linux syscall handler that aims to emulate some Linux syscalls.\n[WARNING] It is not yet made and you shouldn't rely on it.");
		
		int syscall = this.getCpu().getRegister(7).getValue();
	}

}
