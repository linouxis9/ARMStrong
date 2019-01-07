package projetarm_v2.simulator.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import projetarm_v2.simulator.utils.NativeJarGetter;
import projetarm_v2.simulator.utils.OSValidator;

public class Assembler {
	/**
	 * Temporary directory which will contain the library.
	 */
	private Pattern pattern = Pattern.compile("\\[ (.*) \\]");
	private Pattern errorPattern = Pattern.compile("'(.*) \\(.*'");
	private static Assembler assembler;
	private File executable;
	
	public static Assembler getInstance() {
		if (Assembler.assembler == null) {
			try {
				Assembler.assembler = new Assembler();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Assembler.assembler;
	}

	private Assembler() throws IOException {
		this.executable = NativeJarGetter.getInstance().getNativeExecutable("kstool");
	}

	public byte[] assemble(String assembly, long startingAddress) {
		try {
			if (OSValidator.isWindows()) {
				assembly = assembly.replaceAll("\"", "\\\\\"");
			}
			
			Process p = new ProcessBuilder(executable.getAbsolutePath(), "arm", assembly, Long.toHexString(startingAddress))
					.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String result = reader.readLine();
			
			Matcher matcher = pattern.matcher(result);

			if (!matcher.find()) {
				Matcher errorMatcher = errorPattern.matcher(result);
				if (!errorMatcher.find()) {
					return new byte[0];
				}
				throw new InvalidAssemblyException (errorMatcher.group(1));
			}
			
			String[] output = matcher.group(1).split(" ");
			
			byte[] bytes = new byte[output.length];

			for (int i = 0; i < output.length; i++) {
				bytes[i] = (byte) Integer.parseInt(output[i], 16);
			}

			return bytes;
		} catch (IOException e) {
			throw new InvalidAssemblyException(e.toString());
		}
	}
}
