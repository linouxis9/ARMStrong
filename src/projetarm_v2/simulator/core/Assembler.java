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
	private Pattern pattern;
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

			pattern = Pattern.compile("\\[ (.*) \\]");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] assemble(String assembly, long startingAddress) {
		try {
			Process p = new ProcessBuilder(temp.getAbsolutePath(), "arm", assembly, Long.toHexString(startingAddress))
					.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			Matcher matcher = pattern.matcher(reader.readLine());
			matcher.find();
			String[] output = matcher.group(1).split(" ");

			byte[] bytes = new byte[output.length];

			for (int i = 0; i < output.length; i++) {
				bytes[i] = (byte) Integer.parseInt(output[i], 16);
			}

			return bytes;
		} catch (IOException | IllegalStateException e) {
			throw new InvalidAssemblyException();
		}
	}
}
