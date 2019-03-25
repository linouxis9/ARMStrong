/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.syscalls.io;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import projetarm_v2.simulator.core.Cpu;
import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.core.syscalls.SVCCall;


public abstract class SVCIOCall extends SVCCall {
	private FileDescriptors fileDescriptors;

	public SVCIOCall(Cpu cpu, FileDescriptors fileDescriptors) {
		super(cpu);
		this.fileDescriptors = fileDescriptors;
		
	}

	protected FileDescriptors getFileDescriptors() {
		return this.fileDescriptors;
	}
}
