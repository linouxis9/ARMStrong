package projetarm_v2.simulator.core.routines;

import projetarm_v2.simulator.core.Cpu;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CpuGetFile extends CpuRoutine
{
	public static final long ROUTINE_ADDRESS = 0xFF0CL;
	
	public CpuGetFile(Cpu cpu) { super(cpu); }
	
	@Override
	public long getRoutineAddress() { return ROUTINE_ADDRESS; }
	
	@Override
	protected void primitive() throws IOException
	{
		long address = (long) this.getRegister(0).getValue();
		long dest = (long) this.getRegister(1).getValue();
		
		List<Byte> list = new ArrayList<>();
		
		File myFile = new File(list.toString());
		
		if(myFile.length() < 256)
		{
			byte[] array = Files.readAllBytes(new File(list.toString()).toPath());
			
			for(int i = 0 ; i < array.length ; i++)
			{
				this.getRam().setByte(dest,array[i]);
				dest++;
			}
		}
		else
		{
			System.out.println("Fichier trop volumineux !! N'essaye pas de tester la force de l'équipe ARMStrong !!");
		}
	}
}
