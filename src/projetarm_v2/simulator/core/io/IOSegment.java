package projetarm_v2.simulator.core.io;

import projetarm_v2.simulator.core.RamRegister;

public class IOSegment extends IOComponent {

	protected IOSegment(RamRegister port, int shift) {
		super(port, shift);
	}

}
