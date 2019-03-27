/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.ui.javafx;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.dockfx.DockNode;
import projetarm_v2.gpl.TextFieldTableCellFixed;
import projetarm_v2.simulator.boilerplate.ArmSimulator;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * The registers view at the left of the gui
 */
public class RegistersView {

    private TabPane mainPane;
    private DockNode dockNode;
    private Image dockImage;
    private ObservableList<RegisterObjectView> registersHex;
    private ObservableList<RegisterObjectView> registersSigDec;
    private ObservableList<RegisterObjectView> registersDec;
    private ArmSimulator simulator;
    private Pane hexPane;
    private Pane sigDecPane;
    private Pane decPane;
    private Tab hexTab;
    private Tab sigDecTab;
    private Tab decTab;
    
    private RegisterObjectView flagHex;
    private RegisterObjectView flagSigDec;
    private RegisterObjectView flagDec;
	
	private TableView<RegisterObjectView> tableHex;
	private TableView<RegisterObjectView> tableSigDec;
	private TableView<RegisterObjectView> tableDec;
	
	private TableColumn<RegisterObjectView, String> nameHexCol;
	private TableColumn<RegisterObjectView, String> valueHexCol;
	private TableColumn<RegisterObjectView, String> nameSigDecCol;
	private TableColumn<RegisterObjectView, String> valueSigDecCol;
	private TableColumn<RegisterObjectView, String> nameDecCol;
	private TableColumn<RegisterObjectView, String> valueDecCol;

	
	private DecimalFormat fmt;

	/**
	 * Creates a new instance of a register view
	 * @param simulator the simulator
	 */
	public RegistersView(ArmSimulator simulator){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
    	this.simulator = simulator;
    	
    	fmt = new DecimalFormat("+#;-#");
    	
        this.hexPane = new Pane();
        this.sigDecPane = new Pane();
        this.decPane = new Pane();
        
        this.hexPane.getStyleClass().add("contentTabPane");
        this.sigDecPane.getStyleClass().add("contentTabPane");
        this.decPane.getStyleClass().add("contentTabPane");
 
        this.tableHex = new TableView<>();
        this.tableSigDec = new TableView<>();
        this.tableDec = new TableView<>();
        
        this.tableHex.getStyleClass().add("registersTable");
        this.tableSigDec.getStyleClass().add("registersTable");
        this.tableDec.getStyleClass().add("registersTable");
        
        this.hexPane.getChildren().add(this.tableHex);
        this.sigDecPane.getChildren().add(this.tableSigDec);
        this.decPane.getChildren().add(this.tableDec);
        
    	this.nameHexCol = new TableColumn<>();
    	this.valueHexCol = new TableColumn<>();
    	this.nameSigDecCol = new TableColumn<>();
    	this.valueSigDecCol = new TableColumn<>();
    	this.nameDecCol = new TableColumn<>();
    	this.valueDecCol = new TableColumn<>();
    	
    	this.nameHexCol.setSortable(false);
    	this.valueHexCol.setSortable(false);
    	this.nameSigDecCol.setSortable(false);
    	this.valueSigDecCol.setSortable(false);
    	this.nameDecCol.setSortable(false);
    	this.valueDecCol.setSortable(false);	
    	
    	this.nameHexCol.prefWidthProperty().bind(this.hexPane.widthProperty().multiply(0.458));
    	this.valueHexCol.prefWidthProperty().bind(this.hexPane.widthProperty().multiply(0.5));
    	this.nameSigDecCol.prefWidthProperty().bind(this.sigDecPane.widthProperty().multiply(0.458));
    	this.valueSigDecCol.prefWidthProperty().bind(this.sigDecPane.widthProperty().multiply(0.5));
    	this.nameDecCol.prefWidthProperty().bind(this.decPane.widthProperty().multiply(0.458));
    	this.valueDecCol.prefWidthProperty().bind(this.decPane.widthProperty().multiply(0.5));    	
    	
    	this.tableHex.prefHeightProperty().bind(this.hexPane.heightProperty());
    	this.tableHex.prefWidthProperty().bind(this.hexPane.widthProperty());
    	this.tableSigDec.prefHeightProperty().bind(this.sigDecPane.heightProperty());
    	this.tableSigDec.prefWidthProperty().bind(this.sigDecPane.widthProperty());
    	this.tableDec.prefHeightProperty().bind(this.decPane.heightProperty());
    	this.tableDec.prefWidthProperty().bind(this.decPane.widthProperty());

		ChangeListener<Number> widthListener = (source, oldWidth, newWidth) -> {
            ArrayList<Pane> panes = new ArrayList<>();
            panes.add((Pane) tableDec.lookup("TableHeaderRow"));
            panes.add((Pane) tableHex.lookup("TableHeaderRow"));
            panes.add((Pane) tableSigDec.lookup("TableHeaderRow"));
            for(Pane header : panes){
                if (header.isVisible()){
                    header.setMaxHeight(0);
                    header.setMinHeight(0);
                    header.setPrefHeight(0);
                    header.setVisible(false);
                }
            }
		};

		this.tableHex.widthProperty().addListener(widthListener);
    	this.tableSigDec.widthProperty().addListener(widthListener);
    	this.tableDec.widthProperty().addListener(widthListener);
    	
    	this.tableHex.setEditable(true);
        this.tableSigDec.setEditable(true);
        this.tableDec.setEditable(true);
    	
    	this.nameHexCol.setCellValueFactory(new PropertyValueFactory<>("nameRegister"));
    	this.valueHexCol.setCellValueFactory(new PropertyValueFactory<>("valueRegister"));
    	this.nameSigDecCol.setCellValueFactory(new PropertyValueFactory<>("nameRegister"));
    	this.valueSigDecCol.setCellValueFactory(new PropertyValueFactory<>("valueRegister"));
    	this.nameDecCol.setCellValueFactory(new PropertyValueFactory<>("nameRegister"));
    	this.valueDecCol.setCellValueFactory(new PropertyValueFactory<>("valueRegister"));
    	
    	this.valueHexCol.setCellFactory(TextFieldTableCellFixed.forTableColumn());
    	this.valueSigDecCol.setCellFactory(TextFieldTableCellFixed.forTableColumn());
    	this.valueDecCol.setCellFactory(TextFieldTableCellFixed.forTableColumn());

    	this.valueHexCol.setOnEditCommit(t -> {
					String newValue = t.getNewValue();
					int registerValue = t.getTableView().getItems().get(t.getTablePosition().getRow()).getRegister();

					if(registerValue == 17) {
						editFlags(newValue);
					} else {
						try {
							String[] hex = newValue.split("x");
							int value = Integer.parseInt(hex[hex.length-1], 16);
							if (registerValue < 16) {
								simulator.setRegisterValue(registerValue, value);
								if (registerValue == 15) {
									simulator.getCpu().setCurrentAddress(value);
								}
							}
						} catch (NumberFormatException e) {
							Gui.warningPopup(e.getMessage(), (_e)->{});
						}
					}
					
					t.getTableView().getItems().get(t.getTablePosition().getRow()).setValueRegister(newValue);
					updateRegisters();
				}
		);
    	
    	this.valueSigDecCol.setOnEditCommit(t -> {
					String newValue = t.getNewValue();
					int registerValue = t.getTableView().getItems().get(t.getTablePosition().getRow()).getRegister();

					if(registerValue == 17) {
						editFlags(newValue);
					} else if (isNumeric(newValue)) {
						try {
							int value = Integer.parseInt(newValue);
							if (registerValue < 16) {
								simulator.setRegisterValue(registerValue, value);
								if (registerValue == 15) {
									simulator.getCpu().setCurrentAddress(value);
								}
							}
						} catch (NumberFormatException e) {
							Gui.warningPopup(e.getMessage(), (_e)->{});
						}
					}
					
					t.getTableView().getItems().get(t.getTablePosition().getRow()).setValueRegister(newValue);
					updateRegisters();
				}
		);

    	this.valueDecCol.setOnEditCommit(t -> {
					String newValue = t.getNewValue();
					int registerValue = t.getTableView().getItems().get(t.getTablePosition().getRow()).getRegister();

					if(registerValue == 17) {
						editFlags(newValue);
					} else if (isNumeric(newValue)) {
						try {
							int value = Integer.parseUnsignedInt(newValue);
							if (registerValue < 16) {
								simulator.setRegisterValue(registerValue, value);
								if (registerValue == 15) {
									simulator.getCpu().setCurrentAddress(value);
								}
							}
						} catch (NumberFormatException e) {
							Gui.warningPopup(e.getMessage(), (_e)->{});
						}
					}
					
					t.getTableView().getItems().get(t.getTablePosition().getRow()).setValueRegister(newValue);
					updateRegisters();
				}
		);
    	
    	this.tableHex.getColumns().addAll(this.nameHexCol, this.valueHexCol);
        this.tableSigDec.getColumns().addAll(this.nameSigDecCol, this.valueSigDecCol);
        this.tableDec.getColumns().addAll(this.nameDecCol, this.valueDecCol);
        
        this.mainPane = new TabPane();
        this.mainPane.setMinWidth(175);
        
        this.hexTab = new Tab("Hexa", hexPane);
        this.sigDecTab = new Tab("Sig Dec", sigDecPane);
        this.decTab = new Tab("Decimal", decPane);
        this.mainPane.getTabs().addAll(hexTab, sigDecTab, decTab);
        this.mainPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        this.dockNode = new DockNode(mainPane, "Register View", new ImageView(dockImage));
        this.dockNode.setPrefSize(300, 100);
        this.dockNode.getStylesheets().add("/resources/style.css");
        
        this.registersHex = FXCollections.observableArrayList();
        this.registersSigDec = FXCollections.observableArrayList();
        this.registersDec = FXCollections.observableArrayList();
        
        for (int i = 0; i < 16; i++) {
        	String nameRegister = "R" + i;
        	
        	switch(i){
        		case 13:
        			nameRegister += " (SP)";
        			break;
        		case 14:
        			nameRegister += " (LR)";
        			break;
        		case 15:
        			nameRegister += " (PC)";
        	}
        	
        	String registerHex = String.format("0x%08x", simulator.getRegisterValue(i));	
  		   	this.registersHex.add(new RegisterObjectView(i, nameRegister, registerHex));
  		   	
		   	String registerDec = String.format("%s", Integer.toUnsignedString(simulator.getRegisterValue(i)));
  		   	this.registersDec.add(new RegisterObjectView(i, nameRegister, registerDec));
  		   	
  		   	String registerSigDec = fmt.format(simulator.getRegisterValue(i));
		  	this.registersSigDec.add(new RegisterObjectView(i, nameRegister, registerSigDec));
  		}
        
        this.flagHex = new RegisterObjectView(17, "[FLAGS]",simulator.getCpu().getCPSR().toString());
        this.flagSigDec = new RegisterObjectView(17, "[FLAGS]",simulator.getCpu().getCPSR().toString());
        this.flagDec = new RegisterObjectView(17, "[FLAGS]",simulator.getCpu().getCPSR().toString());

        this.registersHex.add(flagHex);
        this.registersSigDec.add(flagSigDec);
        this.registersDec.add(flagDec);       
        
    	this.tableHex.setItems(this.registersHex);
        this.tableSigDec.setItems(this.registersSigDec);
        this.tableDec.setItems(this.registersDec);
        
        this.hexTab.setContent(this.hexPane);
        this.sigDecTab.setContent(this.sigDecPane);
        this.decTab.setContent(this.decPane);

    }

	/**
	 * set the ram view in editable mode
	 * @param editable
	 */
	public void setEditable(boolean editable) {
    	this.tableDec.setEditable(editable);
    	this.tableSigDec.setEditable(editable);
    	this.tableHex.setEditable(editable);
    }

	/**
	 * parse the flags set by the user and report them to the simulator
	 * @param newValue the string typed in the flag field in the gui
	 */
	private void editFlags(String newValue) {
		char[] myCharacters = newValue.toCharArray();
		
		for(char c: myCharacters) {
			switch(c) {
				case 'N':
					this.simulator.setN(true);
					break;
				case 'n':
					this.simulator.setN(false);
					break;
				case 'Z':
					this.simulator.setZ(true);
					break;
				case 'z':
					this.simulator.setZ(false);
					break;
				case 'C':
					this.simulator.setC(true);
					break;
				case 'c':
					this.simulator.setC(false);
					break;
				case 'V':
					this.simulator.setV(true);
					break;
				case 'v':
					this.simulator.setV(false);		
			}
		}
	}

	/**
	 * updates the registers in the register view
	 */
	public void updateRegisters(){

    	for (int i = 0; i < 16; i++) {
    		this.registersHex.get(i).setValueRegister(String.format("0x%08x", simulator.getRegisterValue(i)));
    		this.registersDec.get(i).setValueRegister(String.format("%s", Integer.toUnsignedString(simulator.getRegisterValue(i))));
    		this.registersSigDec.get(i).setValueRegister(fmt.format(simulator.getRegisterValue(i)));
    	}
         
    	this.flagHex.setValueRegister(simulator.getCpu().getCPSR().toString());
        this.flagSigDec.setValueRegister(simulator.getCpu().getCPSR().toString());
        this.flagDec.setValueRegister(simulator.getCpu().getCPSR().toString());

        refreshColumnData();
    }

	/**
	 * refresh the display
	 */
	public void refreshColumnData() {
    	this.tableDec.refresh();
    	this.tableHex.refresh();
    	this.tableSigDec.refresh();
    }

	/**
	 * test if a string is numeric
	 * @param str string to parse
	 * @return true if the string is numeric
	 */
	private boolean isNumeric(String str) {
	  try {  
	    Integer.parseInt(str);  
	    return true;
	  } catch(NumberFormatException e){  
	    return false;  
	  }  
	}

	/**
	 * get the dock node
	 * @return dockNode
	 */
    DockNode getNode(){
        return dockNode;
    }
}
