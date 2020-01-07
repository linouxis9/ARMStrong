/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.ui.javafx.ramview;

import javafx.collections.ObservableListBase;
import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.ui.javafx.FormatException;
import projetarm_v2.simulator.ui.javafx.Gui;

public class RamObservableListAdapter extends ObservableListBase<NewLineRam> {

	private Ram ram;
	private int offset;
	private OutputType outputType;
	private ShowType showType;
	
	private RamView ramView;
	
    public RamObservableListAdapter(Ram ram, RamView ramView) {
		this.ram = ram;
		this.offset = 0;
		this.outputType = OutputType.HEX;
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
		
		if (this.outputType == OutputType.HEX && this.showType == ShowType.HALFWORD) {
			kek -= 1;
		}
		
		if (this.showType == ShowType.WORD) {
			kek = 4;
		}
		return kek;
	}
	
    @Override
    public int size() {
        return (int) Math.floor(ramView.getTableView().getHeight() / 27);
    }
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public void setOutputType(OutputType type) {
		this.outputType = type;
	}

	public OutputType getOutputType() {
		return outputType;
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
		int address = offset+row*this.showType.toOffset()*getColumns()+column*showType.toOffset();
		int newVal = 0;
		value = value.trim();
		try {
			switch (outputType) {
				case HEX:
					newVal = Integer.parseUnsignedInt(value.startsWith("0x") ? value.substring(2) : value, 16);
					break;
				case SIG_DEC:
					newVal = Integer.parseInt(value);
					break;
				case UNSIG_DEC:
					newVal = Integer.parseUnsignedInt(value);
					break;
				case ASCII:
					char[] charArray = value.toCharArray();
					newVal = charArray.length == 0 ? 0 : charArray[0];
					break;
			}
		} catch (NumberFormatException e) {
			Gui.warningPopup(e.getMessage(), (_e)->{});
		}
		switch (showType){
			case BYTE:
				if (newVal > Byte.MAX_VALUE){
					Gui.warningPopup("Maximum byte value exceeded", a -> {});
				}
				ram.setByte(address, (byte)newVal);
				break;
			case HALFWORD:
				if (newVal > Short.MAX_VALUE){
					Gui.warningPopup("Maximum halfword value exceeded", a -> {});
				}
				ram.setHWord(address, (short)newVal);
				break;
			case WORD:
				ram.setValue(address, newVal);
				break;
		}
		System.out.println("[INFO] " + newVal + " was written at address 0x" + Integer.toHexString(address));
    }
}
