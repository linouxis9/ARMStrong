import java.util.Scanner;
import simulator.*;
/**
 * Our software's entrypoint
 */
public class Main {

	public static void main(String[] args) {
		Cpu cpuTest = new Cpu();
		Scanner in = new Scanner(System.in);
		
		String test = "";
		while (in.hasNext()) {
			test = test + in.nextLine() + System.lineSeparator();
		}
		
		in.close();
		
		System.out.println(test);
			
		Interpretor interpretorTest = new Interpretor(cpuTest,new Program(test));
		try {
			interpretorTest.parseProgram();
			while (!cpuTest.hasFinished()) {
					cpuTest.execute();
			}
		} catch (AssemblyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
