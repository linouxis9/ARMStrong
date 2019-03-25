/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.syscalls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import projetarm_v2.simulator.core.Cpu;

public class SVCCallSystem extends SVCCall {

	public SVCCallSystem(Cpu cpu) {
		super(cpu);
	}

	@Override
	protected int getSvcNumber() {
		return 0x12;
	}

	@Override
	protected void primitive() {
		try {
			int length = this.getRam().getValue(this.getCpu().getRegister(1).getValue()+4);
			String command = this.longToString(this.getRam().getValue(this.getCpu().getRegister(1).getValue()), length);
			Process p = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
