/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.routines;

import projetarm_v2.simulator.core.Cpu;
import projetarm_v2.simulator.ui.javafx.ConsoleView;

public class CpuConsoleClear extends CpuRoutine {
	
	public static final long ROUTINE_ADDRESS = 0x1F0024L;
	private ConsoleView consoleView;
	
	public CpuConsoleClear(Cpu cpu) {
		super(cpu);
	}
	
	public void setConsoleView(ConsoleView consoleView) {
		this.consoleView = consoleView;
	}
	
	public long getRoutineAddress() { return ROUTINE_ADDRESS; }
	
	public static boolean shouldBeManuallyAdded() {	return true; }
	
	@Override
	protected void primitive()
	{
		if (consoleView != null) {
			this.consoleView.clear();
		}
	}
}
