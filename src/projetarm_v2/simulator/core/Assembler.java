package projetarm_v2.simulator.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.adamh.utils.NativeUtils;

public class Assembler {
	/**
	 * Temporary directory which will contain the library.
	 */
	private File temporaryDir;
	private File temp;
	private Pattern pattern = Pattern.compile("\\[ (.*) \\]");
	private Pattern errorPattern = Pattern.compile("'(.*) \\(.*'");
	private static Assembler assembler;

	public static Assembler getInstance() {
		if (Assembler.assembler == null) {
			Assembler.assembler = new Assembler();
		}
		return Assembler.assembler;
	}

	private Assembler() {
		try {
			temporaryDir = NativeUtils.createTempDirectory("projectarm");
			temp = new File(temporaryDir, "kstool");
			
			InputStream is = getClass().getResourceAsStream("/natives/kstool");
			Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			temp.setExecutable(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] assemble(String assembly, long startingAddress) {
		try {
			Process p = new ProcessBuilder(temp.getAbsolutePath(), "arm", assembly, Long.toHexString(startingAddress))
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
