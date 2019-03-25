/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.syscalls.io;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import projetarm_v2.simulator.core.Cpu;

public class SVCCallClose extends SVCIOCall {

	public SVCCallClose(Cpu cpu, FileDescriptors fileDescriptors) {
		super(cpu, fileDescriptors);
	}

	@Override
	protected int getSvcNumber() {
		return 0x02;
	}

	@Override
	protected void primitive() {
		int fd = this.getRam().getValue(this.getCpu().getRegister(1).getValue());

		boolean success = this.getFileDescriptors().closeFile(fd);

		this.getCpu().getRegister(0).setValue(success ? 0 : -1);
	}

}
