package projetarm_v2.simulator.ui.javafx.ramview;

import javafx.collections.ModifiableObservableListBase;
import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.ui.javafx.FormatExeption;
import projetarm_v2.simulator.ui.javafx.Gui;

public class RamObservableListAdapter extends ModifiableObservableListBase<NewLineRam> {

	private Ram ram;
	private int offset;
	private OutputType outputType;
	private ShowType showType;
	
	private RamView ramView;
	
    public RamObservableListAdapter(Ram ram, RamView ramView) {
		this.ram = ram;
		this.offset = 0;
		this.outputType = OutputType.NORMAL;
		this.showType = ShowType.BYTE;
		this.ramView = ramView;
	}

	@Override
    public NewLineRam get(int i) {
    	return new NewLineRam(ram,offset+i*this.showType.toOffset()*getColumns(),showType,outputType);
    }

	public int getColumns() {
		int kek = (int)ramView.getTableView().getWidth()/ 60;
		
		if (kek > 8) { kek = 8; }
		
		if (this.showType == ShowType.WORD) {
			kek -= 2;
		}
		return kek;
	}
	
    @Override
    public int size() {
        return (int) Math.ceil(ramView.getTableView().getHeight() / 30);
    }

	@Override
	protected void doAdd(int index, NewLineRam element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected NewLineRam doSet(int index, NewLineRam element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected NewLineRam doRemove(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public void setOutputType(OutputType type) {
		this.outputType = type;
	}
	
	public void setShowType(ShowType type) {
		this.showType = type;
	}
	
	public ShowType getShowType() {
		return this.showType;
	}

	public ShowType getShowTypeValue() {
    	return this.showType;
	}

	public void setValue(int column, int row, String value) {
    	column--;
		int address =  offset+row*this.showType.toOffset()*getColumns()+column*showType.toOffset();
		int newVal = 0;
		try {
			newVal = Gui.parseUserAdress(value);
		} catch (FormatExeption formatExeption) {}
		switch (showType){
			case BYTE:
				if (newVal>256){
					Gui.warningPopup("maximum byte value exceeded", a -> {});
				}
				ram.setByte(address, (byte) newVal);
				break;
			case HALFWORD:
				if (newVal>65536){
					Gui.warningPopup("maximum halfword value exceeded", a -> {});
				}
				ram.setHWord(address, (short) newVal);
				break;
			case WORD:
				ram.setValue(address, newVal);
		}
		System.out.println("[INFO] " + newVal + " was written at address 0x" + Integer.toHexString(address));
    }
}
