/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.syscalls;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import projetarm_v2.simulator.core.Cpu;
import projetarm_v2.simulator.core.Ram;


public abstract class SVCCall {

	private Cpu cpu;

	public SVCCall(Cpu cpu) {
		this.cpu = cpu;
	}

	protected Cpu getCpu() {
		return this.cpu;
	}

	protected Ram getRam() {
		return this.cpu.getRam();
	}

	protected String longToString(long address, int length) throws UnsupportedEncodingException
	{
	
		List<Byte> list = new ArrayList<>();
		byte c = this.getRam().getByte(address++);
		
		for (int i = 0; i < length; i++) {
			list.add(c);
			c = this.getRam().getByte(address++);
		}
		
		byte[] array = new byte[list.size()];
		
		int i= 0;
		for (Byte current : list) {
			array[i] = current;
			i++;
		}
		
		return new String(array, "UTF-8");
	
	}
	
	protected abstract int getSvcNumber();

	protected abstract void primitive();
	
	protected void run() {
		this.primitive();
	}
}
