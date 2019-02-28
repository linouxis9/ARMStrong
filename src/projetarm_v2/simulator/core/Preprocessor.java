package projetarm_v2.simulator.core;

import projetarm_v2.simulator.core.routines.CpuBreakpoint;

public class Preprocessor {
	public static String pass1(String assembly) {
		return assembly.replaceAll(System.lineSeparator(), ";").replaceAll(".breakpoint", "blx #" + CpuBreakpoint.ROUTINE_ADDRESS);
	}
}
