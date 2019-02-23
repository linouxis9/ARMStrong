package projetarm_v2.simulator.core.io;

import projetarm_v2.simulator.core.RamRegister;

public class IOSegment extends IOComponent {

	protected IOSegment(RamRegister port, int shift, int portNb) {
		super(port, shift, portNb);
	}

}
