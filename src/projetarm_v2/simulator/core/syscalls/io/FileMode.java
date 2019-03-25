package projetarm_v2.simulator.core.syscalls.io;

public enum FileMode {
	ReadOnly(0),
	ReadBinaryOnly(1),
	ReadAndWrite(2),
	ReadAndWriteBinary(3),
	WriteOnly(4),
	WriteBinary(5),
	WriteAndRead(6),
	WriteAndReadBinary(7),
	Append(8),
	AppendBinary(9),
	AppendAndRead(10),
	AppendAndReadBinary(11);
			
	
	private int no;
	
	private FileMode(int no) {
		this.no = no;
	}
	
	public int getNo() {
		return this.no;
	}
	
	public static FileMode fromNo(int no) {
		switch(no) {
			case 0: return ReadOnly;
			case 1: return ReadBinaryOnly;
			case 2: return ReadAndWrite;
			case 3: return ReadAndWriteBinary;
			case 4: return WriteOnly;
			case 5: return WriteBinary;
			case 6: return WriteAndRead;
			case 7: return WriteAndReadBinary;
			case 8: return Append;
			case 9: return AppendBinary;
			case 10: return AppendAndRead;
			case 11: return AppendAndReadBinary;
			default: return null;
		}
	}
}
