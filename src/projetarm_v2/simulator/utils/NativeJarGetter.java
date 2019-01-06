package projetarm_v2.simulator.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class NativeJarGetter {

	private static final String NATIVE_DIR = "/natives/";
	private static NativeJarGetter nativeGetter;
	private File temporaryDir;

	private NativeJarGetter() throws IOException {
		temporaryDir = this.createTempDirectory("projectarm");
	}

	public static NativeJarGetter getInstance() {
		if (NativeJarGetter.nativeGetter == null) {
			try {
				NativeJarGetter.nativeGetter = new NativeJarGetter();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return NativeJarGetter.nativeGetter;
	}

	public void loadLibraryFromJar(String libraryName) throws IOException {
		System.load(this.getNativeLibrary(libraryName).getAbsolutePath());
	}

	public File getNativeLibrary(String libraryName) throws IOException {
		return this.getFile(libraryName + "-" + System.getProperty("os.arch") +  NativeJarGetter.getDynamicLibraryExtension());
	}

	public File getNativeExecutable(String executableName) throws IOException {
		return this.getFile(executableName + "-" + System.getProperty("os.arch") + NativeJarGetter.getExecutableExtension());
	}

	public File getFile(String executableName) throws IOException {
		File temp = new File(temporaryDir, executableName);

		InputStream is = NativeJarGetter.class.getResourceAsStream(NativeJarGetter.NATIVE_DIR + executableName);
		Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);

		temp.setExecutable(true);

		return temp;
	}

	private static String getExecutableExtension() {
		if (OSValidator.isWindows()) {
			return ".exe";
		}
		return "";
	}

	private static String getDynamicLibraryExtension() {
		if (OSValidator.isWindows()) {
			return ".dll";
		} else if (OSValidator.isMac()) {
			return ".dylib";
		}
		return ".so"; // Assumes a *NIX (Linux, FreeBSD, Solaris..) using ELF
	}

	public File createTempDirectory(String prefix) throws IOException {
		String tempDir = System.getProperty("java.io.tmpdir");
		File generatedDir = new File(tempDir, prefix + System.nanoTime());

		if (!generatedDir.mkdir())
			throw new IOException("Failed to create temp directory " + generatedDir.getName());

		generatedDir.deleteOnExit();

		return generatedDir;
	}
}
