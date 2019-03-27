/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.routines;


import projetarm_v2.simulator.core.Cpu;

public class CpuSleep extends CpuRoutine {

	public static final long ROUTINE_ADDRESS = 0x1F0010L;
	
	public CpuSleep(Cpu cpu) {
		super(cpu);
	}
	
	public long getRoutineAddress() {
		return ROUTINE_ADDRESS;
	}
	
	@Override
	protected void primitive() {
		try {
			Thread.sleep(this.getRegister(0).getValue());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
