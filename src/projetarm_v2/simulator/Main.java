package projetarm_v2.simulator;

import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.ui.cli.Cli;
import projetarm_v2.simulator.ui.javafx.Gui;

public class Main {
	public static void main(String[] args) {		
		System.out.print(">>> Launching Test sequence\n");
		
		ArmSimulator simulator = new ArmSimulator(); // PC is set @ 0x1000, 2 MB of RAM by default
		simulator.setProgram("b start;" + 
				"kek: .asciz \"test\";" + 
				".align;" + 
				"start: ldr r0,=kek;" + 
				"mov r1,#0xFF04;" +
				"blx r1");

		simulator.run();
		
		System.out.print(">>> Emulation done. Below is the CPU context%n");
			
		for (int i = 0; i < 16; i++) {
			System.out.print(String.format(">>> R%d = 0x%x%n", i,simulator.getRegisterValue(i)));
		}
		
		System.out.format(">>> Processor Flags : %s%n", simulator.getCpu().getCPSR());
		System.out.println(simulator.getRam());

		if (args.length != 0) {
			Gui.main(new String[0]);
			return;
		}
		
		Cli.main(new String[0]);
	}
}