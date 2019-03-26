/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.routines;

import java.util.Random;

import projetarm_v2.simulator.core.Cpu;
import projetarm_v2.simulator.ui.javafx.ConsoleView;

public class CpuRandom extends CpuRoutine {
	
	public static final long ROUTINE_ADDRESS = 0xFF20L;
	private Random random;
	
	
	public CpuRandom(Cpu cpu) {
		super(cpu);
		this.random = new Random();
	}
	
	public long getRoutineAddress() { return ROUTINE_ADDRESS; }

	
	@Override
	protected void primitive()
	{
		this.getCpu().getRegister(0).setValue(this.random.nextInt());
	}
}
