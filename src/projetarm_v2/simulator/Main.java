package projetarm_v2.simulator;

import projetarm_v2.simulator.boilerplate.ArmSimulator;

public class Main {
	public static void main(String[] args) {
		ArmSimulator simulator = new ArmSimulator(); // PC is set @ 0x1000, 2 MB of RAM by default
		simulator.setProgram("b start\n" + 
				"kek: .asciz \"test\"\n" + 
				".align\n" + 
				"start: ldr r0,=kek\n" + 
				"mov r1,#0xFF04\n" +
				"blx r1");

		simulator.run();
		
		System.out.print(">>> Emulation done. Below is the CPU context\n");
			
		for (int i = 0; i < 16; i++) {
			System.out.print(String.format(">>> R%d = 0x%x%n", i,simulator.getRegisterValue(i)));
		}
		System.out.format(">>> Processor Flags : %s%n", simulator.getCpu().getCPSR());
		System.out.println(simulator.getRam());
	}
}