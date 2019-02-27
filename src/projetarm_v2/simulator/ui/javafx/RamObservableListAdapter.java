package projetarm_v2.simulator.ui.javafx;

import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableListBase;
import projetarm_v2.simulator.core.Ram;

public class RamObservableListAdapter extends ModifiableObservableListBase<LineRam> {

	private Ram ram;
	private int offset;
	
    public RamObservableListAdapter(Ram ram) {
		this.ram = ram;
		this.offset = 0;
	}

	@Override
    public LineRam get(int i) {
		int index = i*4 + offset;
        return new LineRam(this.ram.getByte(index),this.ram.getByte(index+1), this.ram.getByte(index+2), this.ram.getByte(index+3));
    }

    @Override
    public int size() {
        return 10;
    }

	@Override
	protected void doAdd(int index, LineRam element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected LineRam doSet(int index, LineRam element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected LineRam doRemove(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
}
