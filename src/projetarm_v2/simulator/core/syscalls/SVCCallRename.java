/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.syscalls;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import projetarm_v2.simulator.core.Cpu;

public class SVCCallRename extends SVCCall {

	public SVCCallRename(Cpu cpu) {
		super(cpu);
	}

	@Override
	protected int getSvcNumber() {
		return 0xf;
	}

	@Override
	protected void primitive() {
		try {
			int originalLength = this.getRam().getValue(this.getCpu().getRegister(1).getValue()+4);
			int newLength = this.getRam().getValue(this.getCpu().getRegister(1).getValue()+12);
			String originalName = this.longToString(this.getRam().getValue(this.getCpu().getRegister(1).getValue()), originalLength);
			String newName = this.longToString(this.getRam().getValue(this.getCpu().getRegister(1).getValue())+8, newLength);
			
			File file = new File(originalName);
			boolean success = file.renameTo(new File(newName));
			this.getCpu().getRegister(0).setValue(success ? 0 : -1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
