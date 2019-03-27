/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.ui.javafx;

import javafx.beans.property.SimpleStringProperty;

//TODO: didi je te laisse faire la javadoc ce truc wtf xD
public class RegisterObjectView {
		 
	private final SimpleStringProperty nameRegister;
	private final SimpleStringProperty valueRegister; 
	private int myRegisterNumber;


    RegisterObjectView(int register, String fName, String fValue) {
    	this.nameRegister = new SimpleStringProperty(fName);
    	this.valueRegister = new SimpleStringProperty(fValue);
    	this.myRegisterNumber = register;
    }
 
    public String getNameRegister() {
    	return this.nameRegister.get();
    }
    
    public void setNameRegister(String fName) {
    	this.nameRegister.set(fName);
    }
 
    public String getValueRegister() {
    	return this.valueRegister.get();
    }
    
    public void setValueRegister(String fValue) {
    	this.valueRegister.set(fValue);
    }
    
    public int getRegister() {
    	return this.myRegisterNumber;
    }
    
    public void setRegister(int fRegister) {
    	this.myRegisterNumber = fRegister;
    }
}
