/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.ui.javafx.ramview;

import projetarm_v2.simulator.core.Ram;

public class NewLineRam {

	private Ram ram;
	private int firstAddress;
	private ShowType showType;
	private OutputType type;
	
	private String[] cases;
	
	
	public NewLineRam(Ram ram, int offset, ShowType showType, OutputType type) {
		this.ram = ram;
		this.firstAddress = offset;
		this.type = type;
		this.showType = showType;
		
		this.cases = new String[8];
		
		for (int i = 0; i < this.cases.length; i++) {
			cases[i] = this.get2(i);
		}
	}
	
	private String asString(int a2) {
		switch (this.type) {
			default:
			case NORMAL:
				return Integer.toString(a2);
			case HEX:
				return String.format("0x%x",a2);
		}
	}

	public String get(int index) {
		return this.cases[index];
	}
	
	public String get2(int index) {
		int rs;

		switch(this.showType) {
			case WORD: rs = ram.getValue(this.firstAddress+index*this.showType.toOffset()); break;
			case HALFWORD: rs = ((int)ram.getHWord(this.firstAddress+index*this.showType.toOffset())) & 0xFFFF; break;
			default:
			case BYTE: rs = (int)ram.getByte(this.firstAddress+index*this.showType.toOffset()) & 0xFF; break;
		}

		return asString(rs);
	}
	
	public String getLine() {
		return "0x"+Integer.toHexString(firstAddress);
	}
	
	public String getA() {
		return get(0);
	}
	public void setA(String a) {
	}
	public String getB() {
		return get(1);
	}
	public void setB(String b) {
	}
	public String getC() {
		return get(2);
	}
	public void setC(String c) {
	}
	public String getD() {
		return get(3);
	}
	public void setD(String d) {
	}
	
	public void setE(String E) {
	}
	public String getE() {
		return get(4);
	}
	public String getF() {
		return get(5);
	}
	public void setF(String f) {
	}
	public String getG() {
		return get(6);
	}
	public void setG(String g) {
	}
	public String getH() {
		return get(7);
	}
	public void setH(String h) {
	}
}
