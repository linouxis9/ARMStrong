package projetarm_v2.simulator.core.syscalls.io;

import java.io.File;

public class OpenedFile {
	private File file;
	private FileMode mode;
	
	public OpenedFile(File file, FileMode mode) {
		this.file = file;
		this.mode = mode;
	}

	public File getFile() {
		return file;
	}

	public FileMode getMode() {
		return mode;
	}
}
