package projetarm_v2.simulator.ui.javafx;

import javafx.beans.property.SimpleStringProperty;

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
