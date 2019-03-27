/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.syscalls;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import projetarm_v2.simulator.core.Cpu;
import projetarm_v2.simulator.core.syscalls.io.FileDescriptors;
import projetarm_v2.simulator.core.syscalls.io.OpenedFile;
import projetarm_v2.simulator.core.syscalls.io.SVCIOCall;
import unicorn.InterruptHook;
import unicorn.Unicorn;

/* http://infocenter.arm.com/help/index.jsp?topic=/com.arm.doc.dui0040d/Bcedijji.html */


public class SVCHandler {
	private Cpu cpu;
	
	private Map<Integer,SVCCall> interruptVector;
	
	private FileDescriptors fileDescriptors;

	
	public SVCHandler(Cpu cpu) {
		this.cpu = cpu;
		this.interruptVector = new HashMap<>();
		this.fileDescriptors = new FileDescriptors();

		findSyscalls();
		findIOSyscalls();
	}
	
	public void findSyscalls() {
		Reflections reflections = new Reflections("projetarm_v2.simulator.core.syscalls");

		Set<Class<? extends SVCCall>> calls = reflections.getSubTypesOf(SVCCall.class);
		
		for (Class<? extends SVCCall> classCall : calls) {
			try {
				SVCCall call = (SVCCall)(classCall.getDeclaredConstructor(Cpu.class).newInstance(this.cpu));
				this.interruptVector.put(call.getSvcNumber(),call);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
		}
	}
	
	public void findIOSyscalls() {
		Reflections reflections = new Reflections("projetarm_v2.simulator.core.syscalls.io");

		Set<Class<? extends SVCIOCall>> calls = reflections.getSubTypesOf(SVCIOCall.class);
		
		for (Class<? extends SVCIOCall> classCall : calls) {
			try {
				SVCCall call = (SVCIOCall)(classCall.getDeclaredConstructor(Cpu.class, FileDescriptors.class).newInstance(this.cpu, this.fileDescriptors));
				this.interruptVector.put(call.getSvcNumber(),call);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
		}
	}
	
	public InterruptHook getSVCCallHandler() {
		return new SVCCallHook(this.cpu);
	}
	
	private class SVCCallHook implements InterruptHook {
		private Cpu cpu;
		
		private SVCCallHook(Cpu cpu) {
			this.cpu = cpu;
		}
		
		 public void hook(Unicorn u, int intno, Object user) {
			if (intno == 2) {
				int instruction = this.cpu.getRam().getValue(this.cpu.getCurrentAddress());
				int svcCallNo = (instruction << 8) >> 8;
				
				SVCCall call = interruptVector.get(svcCallNo);
				
				if (call == null) {
					System.out.println("[WARNING] Unimplemented SVC call 0x" + Integer.toHexString(svcCallNo) + "... Ignoring");
					return;
				}
				
				call.run();
			} else {
				System.out.println("[ERROR] Unknown CPU exception no" + intno);
				System.out.println("[ERROR] EMULATION ABORTED!");
				this.cpu.interruptMe();
			}
		}
	}
}
