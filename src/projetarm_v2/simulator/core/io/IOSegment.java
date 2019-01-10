package projetarm_v2.simulator.core.io;

import projetarm_v2.simulator.core.Register;

public class IOSegment extends IOComponent {

	protected IOSegment(Register port, int shift) {
		super(port, shift);
	}

}
