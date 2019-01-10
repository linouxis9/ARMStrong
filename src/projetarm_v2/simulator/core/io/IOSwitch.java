package projetarm_v2.simulator.core.io;

import projetarm_v2.simulator.core.Register;

public class IOSwitch extends IOComponent {

	protected IOSwitch(Register port, int shift) {
		super(port, shift);
	}
	
}
