/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.routines;

import projetarm_v2.simulator.core.Cpu;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CpuConsoleGetChar extends CpuRoutine {
	
	public static final long ROUTINE_ADDRESS = 0x1F0004L;
	private ConcurrentLinkedQueue<Character> consoleBuffer;
	private AtomicBoolean waitingForInput;

	public CpuConsoleGetChar(Cpu cpu, ConcurrentLinkedQueue<Character> consoleBuffer, AtomicBoolean waitingForInput) {
		super(cpu);
		this.consoleBuffer = consoleBuffer;
		this.waitingForInput = waitingForInput;
	}
	
	public long getRoutineAddress() { return ROUTINE_ADDRESS; }
	
	public static boolean shouldBeManuallyAdded() {	return true; }
	
	@Override
	protected void primitive()
	{
		System.out.println("[INPUT] Waiting for input");
		
		this.waitingForInput.set(true);
		while (this.consoleBuffer.peek() == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		this.waitingForInput.set(false);
		
		this.getRegister(0).setValue(this.consoleBuffer.poll());
	}
}
