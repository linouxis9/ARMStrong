package projetarm_v2.simulator.core.routines;

import projetarm_v2.simulator.core.Cpu;

import java.nio.file.Files;
import java.nio.file.Path;

public class CpuGetFile extends CpuRoutine
{
	public static final long ROUTINE_ADDRESS = 0xFF10L;
	
	public CpuGetFile(Cpu cpu) {
		super(cpu);
	}
	
	@Override
	public long getRoutineAddress() {
		return ROUTINE_ADDRESS;
	}
	
	@Override
	protected void primitive()
	{
		long address = (long) this.getRegister(0).getValue();
		long dest = (long) this.getRegister(1).getValue();
		
		try {
			
			byte[] array = Files.readAllBytes(Path.of(this.longToString(dest)));
			for(byte myByte: array)
			{
				this.getRam().setByte(address,myByte);
				address++;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
