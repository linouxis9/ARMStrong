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

public class SVCCallOpen extends SVCIOCall {

	public SVCCallOpen(Cpu cpu, FileDescriptors fileDescriptors) {
		super(cpu, fileDescriptors);
	}

	@Override
	protected int getSvcNumber() {
		return 0x01;
	}

	@Override
	protected void primitive() {
		try {
			int length = this.getRam().getValue(this.getCpu().getRegister(1).getValue()+8);
			String name = this.longToString(this.getRam().getValue(this.getCpu().getRegister(1).getValue()), length);
			int mode = this.getRam().getValue(this.getCpu().getRegister(1).getValue()+4);

			File file = new File(name);
			FileMode fileMode = FileMode.fromNo(mode);
			if (fileMode == null) {
				this.getCpu().getRegister(0).setValue(-1);
				return;
			}
			
			OpenedFile openedFile = new OpenedFile(file, fileMode);
			
			int fd = this.getFileDescriptors().addNewFile(openedFile);
			
			this.getCpu().getRegister(0).setValue(fd);
		} catch (IOException e) {
			e.printStackTrace();
			this.getCpu().getRegister(0).setValue(-1);
		}
	}

}
