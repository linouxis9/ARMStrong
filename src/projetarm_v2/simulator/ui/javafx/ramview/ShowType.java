package projetarm_v2.simulator.ui.javafx.ramview;

public enum ShowType {
	WORD,
	HALFWORD,
	BYTE;
	
	public int toOffset() {
		switch (this) {
		case WORD:
			return 4;
		case HALFWORD:
			return 2;
		default:
		case BYTE:
			return 1;
		}
	}
}
