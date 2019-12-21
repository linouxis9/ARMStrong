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

public class CpuConsoleGetString extends CpuRoutine {
	
	public static final long ROUTINE_ADDRESS = 0x1F000CL;
	private ConcurrentLinkedQueue<Character> consoleBuffer;
	
	private AtomicBoolean waitingForInput;
	
	public CpuConsoleGetString(Cpu cpu, ConcurrentLinkedQueue<Character> consoleBuffer, AtomicBoolean waitingForInput) {
		super(cpu);
		this.waitingForInput = waitingForInput;
		this.consoleBuffer = consoleBuffer;
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
		
		long address = (long) this.getRegister(0).getValue();
		
		for(Character ch : this.consoleBuffer)
		{
			this.getRam().setByte(address,(byte)(char)ch);
			address++;
		}
		this.getRam().setByte(address,(byte)0);
		this.consoleBuffer.clear();
	}
	
	public boolean isWaitingForInput() {
		return this.waitingForInput.get();
	}
}
