/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.ui.javafx.ramview;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.dockfx.DockNode;
import projetarm_v2.gpl.TextFieldTableCellFixed;
import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.ui.javafx.FormatException;
import projetarm_v2.simulator.ui.javafx.Gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RamView {

    private AnchorPane mainPane;
    private DockNode dockNode;

    private ArmSimulator simulator;

    private ScrollBar memoryScrollBar;
    private TableView<NewLineRam> tableView;
    
    private int firstDisplayedAddress = 0;
    
    private RamObservableListAdapter UneSuperImplemFournieParValentinLeBg;
    
    private ArrayList<Button> buttonBits;
    
    private ArrayList<Button> buttonFormat;
    
    private boolean changeAscii = false;

    /**
     * Creates a new instance of a ram view
     * @param simulator the simulator
     */
    public RamView(ArmSimulator simulator) {
        this.simulator=simulator;
        try {
            mainPane = FXMLLoader.load(getClass().getResource("/resources/MemoryView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        dockNode = new DockNode(mainPane, "Ram View", new ImageView());
        dockNode.setPrefSize(300, 100);

        mainPane.setStyle("-fx-line-spacing: -0.4em;");
        this.dockNode.getStylesheets().add("/resources/style.css");

        this.tableView = (TableView<NewLineRam>) mainPane.lookup("#tableView");
        
        UneSuperImplemFournieParValentinLeBg = new RamObservableListAdapter(simulator.getRam(), this);
        UneSuperImplemFournieParValentinLeBg.setOffset(this.firstDisplayedAddress);

        this.tableView.setItems(UneSuperImplemFournieParValentinLeBg);

        this.tableView.setSortPolicy(null);

        tableView.getSelectionModel().setCellSelectionEnabled(true);

        TableColumn<NewLineRam,String> line = new TableColumn<>("Address");
        line.setCellValueFactory(new PropertyValueFactory<>("line"));
        
        this.tableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        line.setMaxWidth( 1f * Integer.MAX_VALUE * 13 );
        this.tableView.getColumns().add(line);

        for (int i = 0; i < UneSuperImplemFournieParValentinLeBg.getColumns(); i++) { //TODO: Le code dans ce for ne s'execute jamais, ou de maniere random getColumns() retourne 0 les premeires fois parce que ramView.getTableView().getWidth() retourne 0 au debut
	        TableColumn<NewLineRam,String> a = new TableColumn<>();
	        a.setCellFactory(TextFieldTableCellFixed.forTableColumn());
	        a.setCellValueFactory(new PropertyValueFactory<>(Character.toString('a'+i)));
	        a.setMaxWidth( 1f * Integer.MAX_VALUE * 11 );

	        this.tableView.getColumns().add(a);
        }

        tableView.widthProperty().addListener((ov, t, t1) -> {
            // Get the table header

            refresh();

            Pane header = (Pane)tableView.lookup("TableHeaderRow");
            if(header!=null && header.isVisible()) {
              header.setMaxHeight(0);
              header.setMinHeight(0);
              header.setPrefHeight(0);
              header.setVisible(false);
              header.setManaged(false);
            }
        });
        
        loadButonsEvents();
        
        this.refresh();
    }

    /**
     * refresh the ram values displayed
     */
    public void refresh() {
    	for (int i = this.tableView.getColumns().size()+1; i> UneSuperImplemFournieParValentinLeBg.getColumns()+2; i--) {
	        this.tableView.getColumns().remove(i-2);
        }
    	
        for (int i = this.tableView.getColumns().size(); i <= UneSuperImplemFournieParValentinLeBg.getColumns(); i++) {
	        TableColumn<NewLineRam,String> a = new TableColumn<>();
	        a.setCellFactory(TextFieldTableCellFixed.forTableColumn());
	        a.setCellValueFactory(
	                new PropertyValueFactory<>(Character.toString('a'+i-1)));
	        a.setMaxWidth( 1f * Integer.MAX_VALUE * 11 );
	        this.tableView.getColumns().add(a);

            a.setOnEditCommit(t -> UneSuperImplemFournieParValentinLeBg.setValue(t.getTablePosition().getColumn(), t.getTablePosition().getRow(), t.getNewValue()));

        }
    	
    	this.tableView.refresh();
    }

    /**
     * set the ram view in editable mode
     * @param editable
     */
    public void setEditable(boolean editable) {
    	this.tableView.setEditable(editable);
    }

    /**
     * load the buttons events to control how the ram is displayed
     */
    private void loadButonsEvents() {
        memoryScrollBar = (ScrollBar) mainPane.lookup("#memoryScrollBar");
        memoryScrollBar.setMin(0);
        memoryScrollBar.setValue(this.firstDisplayedAddress);
        memoryScrollBar.setUnitIncrement(1);
        memoryScrollBar.setMax(this.simulator.getRamSize());
        
        memoryScrollBar.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            this.firstDisplayedAddress = new_val.intValue();
            this.UneSuperImplemFournieParValentinLeBg.setOffset(firstDisplayedAddress);
            this.refresh();
        });
        
        tableView.setOnScroll((ScrollEvent event) -> {        	
        	if (event.getDeltaY() == 0) {
        		return;
        	}
        	
        	if (event.getDeltaY() < 0) {
				this.firstDisplayedAddress += this.UneSuperImplemFournieParValentinLeBg.getShowType().toOffset();
				int offset = this.simulator.getRamSize() - this.UneSuperImplemFournieParValentinLeBg.size()*this.UneSuperImplemFournieParValentinLeBg.getColumns();
				if (this.firstDisplayedAddress > offset) {
					this.firstDisplayedAddress = offset;
				}
        	} else {
                this.firstDisplayedAddress -= this.UneSuperImplemFournieParValentinLeBg.getShowType().toOffset();
                if (this.firstDisplayedAddress < 0) {
                    this.firstDisplayedAddress = 0;
                }
        	}

        	memoryScrollBar.setValue(this.firstDisplayedAddress);
            this.UneSuperImplemFournieParValentinLeBg.setOffset(firstDisplayedAddress);
            this.refresh();
        });
        
        Button button8Bit = (Button) mainPane.lookup("#button8Bit");
    	Button button16Bit = (Button) mainPane.lookup("#button16Bit");
    	Button button32Bit = (Button) mainPane.lookup("#button32Bit");

    	this.buttonBits = new ArrayList<Button>(){{
            add(button8Bit);
            add(button16Bit);
            add(button32Bit);
        }};
        
        changeStyleState(buttonBits, button8Bit);    
        
        Button buttonHex = (Button) mainPane.lookup("#buttonHex");
        Button buttonDec = (Button) mainPane.lookup("#buttonDec");
        Button buttonUnsigDec = (Button) mainPane.lookup("#buttonDecUnSig");
        Button buttonAscii = (Button) mainPane.lookup("#buttonAscii");
   
        this.buttonFormat = new ArrayList<Button>(){{
            add(buttonHex);
            add(buttonDec);
            add(buttonUnsigDec);
            add(buttonAscii);
        }};
        
        changeStyleState(buttonFormat, buttonHex);      
    	
    	button8Bit.setOnAction(ActionEvent -> {
            memoryScrollBar.setUnitIncrement(1);
            this.UneSuperImplemFournieParValentinLeBg.setShowType(ShowType.BYTE);
            this.refresh();

            changeStyleState(buttonBits, button8Bit);
    	});
    	button16Bit.setOnAction(ActionEvent -> {
            memoryScrollBar.setUnitIncrement(2);
            this.UneSuperImplemFournieParValentinLeBg.setShowType(ShowType.HALFWORD);
            alignMemory();
            this.refresh();
            
            changeStyleState(buttonBits, button16Bit);
            
            if(this.changeAscii) {
            	changeStyleState(buttonFormat, buttonHex);
            	this.changeAscii = false;
            }
    	});
    	button32Bit.setOnAction(ActionEvent -> {
            memoryScrollBar.setUnitIncrement(4);
            this.UneSuperImplemFournieParValentinLeBg.setShowType(ShowType.WORD);
            alignMemory();
            this.refresh();

            changeStyleState(buttonBits, button32Bit);
            
            if(this.changeAscii) {
            	changeStyleState(buttonFormat, buttonHex);
            	this.changeAscii = false;
            }
    	});

        buttonHex.setOnAction(ActionEvent -> {
            this.UneSuperImplemFournieParValentinLeBg.setOutputType(OutputType.HEX);
            alignMemory();
            this.refresh();

            changeStyleState(buttonFormat, buttonHex);
        });
        buttonDec.setOnAction(ActionEvent -> {
            this.UneSuperImplemFournieParValentinLeBg.setOutputType(OutputType.SIG_DEC);
            alignMemory();
            this.refresh();
            
            changeStyleState(buttonFormat, buttonDec);

        });

        buttonUnsigDec.setOnAction(ActionEvent -> {
            this.UneSuperImplemFournieParValentinLeBg.setOutputType(OutputType.UNSIG_DEC);
            alignMemory();
            this.refresh();
            
            changeStyleState(buttonFormat, buttonUnsigDec);
        });
        buttonAscii.setOnAction(ActionEvent -> {
        	button8Bit.fire();
            this.UneSuperImplemFournieParValentinLeBg.setOutputType(OutputType.ASCII);
            alignMemory();
            this.refresh(); 
            
            changeStyleState(buttonFormat, buttonAscii); 
            
            this.changeAscii = true;
        });

        TextField goToAddressField = (TextField) mainPane.lookup("#goToAddressField");
        goToAddressField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                String addressTyped = goToAddressField.getText();

                int newAddress = 0;
                try {
                    newAddress = Gui.parseUserAdress(addressTyped);
                } catch (FormatException formatException) {
                    return;
                }

                if (newAddress < 0 || newAddress > Ram.DEFAULT_RAM_SIZE - 4) { //TODO: 4 is the number of columns
                    Gui.warningPopup("The address provided is too big\nPlease stay between 0 and" + Ram.DEFAULT_RAM_SIZE, ActionEvent -> {});
                    return;
                }
                this.firstDisplayedAddress = newAddress;
	            this.firstDisplayedAddress -= this.firstDisplayedAddress % 16; // IUT: Students are lazy :-)
	            alignMemory();
                memoryScrollBar.setValue(this.firstDisplayedAddress);
                this.UneSuperImplemFournieParValentinLeBg.setOffset(firstDisplayedAddress);
                this.refresh();
            }
        });
    }

    /**
     * get the tableView containing the ram values
     * @return
     */
    public TableView<NewLineRam> getTableView() {
    	return this.tableView;
    }

    /**
     * get the dock node
     * @return dockNode
     */
    public DockNode getNode() {
        return dockNode;
    }

    /**
     * align the memory in 16 and 32bits mode
     * addressed have always be a multiple of 2 in 16bits mode and multiple of 4 in 32bits mode
     */
    private void alignMemory(){
        this.firstDisplayedAddress -= this.firstDisplayedAddress % this.UneSuperImplemFournieParValentinLeBg.getShowType().toOffset();
        memoryScrollBar.setValue(this.firstDisplayedAddress);
    }

    /**
     * updates the style on the buttons to indicate the selected mode/type
     * @param list the list of the button group
     * @param buttonChangeStyle the button to change
     */
    private void changeStyleState(List<Button> list, Button buttonChangeStyle){
    	for (Button button: list) {
    		button.getStyleClass().clear();
    		button.getStyleClass().add("button");
    		
    		if(button == buttonChangeStyle) {
    			button.getStyleClass().add("enableButton");
    		}else {
    			button.getStyleClass().add("disableButton");   
    		}		
    	} 	
    	
    }
}
