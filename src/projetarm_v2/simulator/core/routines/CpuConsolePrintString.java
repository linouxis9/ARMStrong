package projetarm_v2.simulator.core.routines;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import projetarm_v2.simulator.core.Cpu;

public class CpuConsolePrintString extends CpuRoutine {

	public final static long ROUTINE_ADDRESS = 0xFF04L;
	
	public CpuConsolePrintString(Cpu cpu) {
		super(cpu);
	}
	
	public long getRoutineAddress() {
		return ROUTINE_ADDRESS;
	}

	@Override
	protected void primitive() {
		long address = (long)this.getRegister(0).getValue();

		List<Byte> list = new ArrayList<>();
		
		byte c = this.getRam().getByte(address++);
		while (c != 0) {
			list.add(c);
			c = this.getRam().getByte(address++);
		}
		
	    byte[] array = new byte[list.size()];
	    int i = 0;
	    for (Byte current : list) {
	        array[i] = current;
	        i++;
	    }

		try {
			System.out.println("[OUTPUT] " + new String(array, "UTF-8"));
		} catch (UnsupportedEncodingException e) {}
	}

}
