package projetarm_v2.simulator.core.syscalls.io;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FileDescriptors {
	private Map<Integer, OpenedFile> map;
	private AtomicInteger newId;
	
	
	public FileDescriptors() {
		this.map = new ConcurrentHashMap<>();
		this.newId = new AtomicInteger(3);
	}
	
	public OpenedFile getFileDescriptor(int no) {
		return this.map.get(no);
	}
	
	public int addNewFile(OpenedFile file) {
		int id = this.newId.getAndIncrement();
		this.map.put(id, file);
		return id;
	}
	
	public boolean closeFile(int fd) {
		return this.map.remove(fd) != null;
	}
}
