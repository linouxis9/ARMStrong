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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import org.dockfx.DockNode;
import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.ui.javafx.FormatExeption;
import projetarm_v2.simulator.ui.javafx.Gui;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class RamView {

    private AnchorPane mainPane;
    private DockNode dockNode;
    private Image dockImage;

    private ArmSimulator simulator;

    private ScrollBar memoryScrollBar;
    private TableView<NewLineRam> tableView;
    
    private int firstDisplayedAddress = 0x1000;
    
    private RamObservableListAdapter UneSuperImplemFournieParValentinLeBg;
    
    public RamView(ArmSimulator simulator) {

        this.simulator=simulator;
        //dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
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
    
    private void loadButonsEvents() {
        memoryScrollBar = (ScrollBar) mainPane.lookup("#memoryScrollBar");
        memoryScrollBar.setMin(0);
        memoryScrollBar.setValue(this.firstDisplayedAddress);
        memoryScrollBar.setUnitIncrement(1);
        memoryScrollBar.setMax(2 *1024*1024); //TODO: set proper value
        
        memoryScrollBar.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            this.firstDisplayedAddress = new_val.intValue();
            this.UneSuperImplemFournieParValentinLeBg.setOffset(firstDisplayedAddress);
            this.refresh();
        });
        
        tableView.setOnScroll((ScrollEvent event) -> {
        	if (event.getDeltaY() < 0) {
        		this.firstDisplayedAddress += this.UneSuperImplemFournieParValentinLeBg.getShowType().toOffset();
        	} else {
                this.firstDisplayedAddress -= this.UneSuperImplemFournieParValentinLeBg.getShowType().toOffset();
        	}
            memoryScrollBar.setValue(this.firstDisplayedAddress);
            this.UneSuperImplemFournieParValentinLeBg.setOffset(firstDisplayedAddress);
            this.refresh();
            this.tableView.autosize();
        });
        
        Button button8Bit = (Button) mainPane.lookup("#button8Bit");
    	Button button16Bit = (Button) mainPane.lookup("#button16Bit");
    	Button button32Bit = (Button) mainPane.lookup("#button32Bit");

    	button8Bit.setOnAction(ActionEvent -> {
            memoryScrollBar.setUnitIncrement(1);
            this.UneSuperImplemFournieParValentinLeBg.setShowType(ShowType.BYTE);
            this.refresh();

            button8Bit.setStyle("-fx-background-color: white;-fx-text-fill: #2C3E50;");
            button16Bit.setStyle("-fx-background-color: #2C3E50;-fx-text-fill: white;");
            button32Bit.setStyle("-fx-background-color: #2C3E50;-fx-text-fill: white;");
    	});
    	button16Bit.setOnAction(ActionEvent -> {
            memoryScrollBar.setUnitIncrement(2);
            this.UneSuperImplemFournieParValentinLeBg.setShowType(ShowType.HALFWORD);
            alignMemory();
            this.refresh();

            button16Bit.setStyle("-fx-background-color: white; -fx-text-fill: #2C3E50;");
            button8Bit.setStyle("-fx-background-color: #2C3E50;-fx-text-fill: white;");
            button32Bit.setStyle("-fx-background-color: #2C3E50;-fx-text-fill: white;");
    	});
    	button32Bit.setOnAction(ActionEvent -> {
            memoryScrollBar.setUnitIncrement(4);
            this.UneSuperImplemFournieParValentinLeBg.setShowType(ShowType.WORD);
            alignMemory();
            this.refresh();

            button32Bit.setStyle("-fx-background-color: white;-fx-text-fill: #2C3E50;");
            button16Bit.setStyle("-fx-background-color: #2C3E50;-fx-text-fill: white;");
            button8Bit.setStyle("-fx-background-color: #2C3E50;-fx-text-fill: white;");
    	});

    	
        Button buttonHex = (Button) mainPane.lookup("#buttonHex");
        Button buttonDec = (Button) mainPane.lookup("#buttonDec");
        Button buttonUnsigDec = (Button) mainPane.lookup("#buttonDecUnSig");
        Button buttonAscii = (Button) mainPane.lookup("#buttonAscii");

        buttonHex.setOnAction(ActionEvent -> {
            this.UneSuperImplemFournieParValentinLeBg.setOutputType(OutputType.HEX);
            alignMemory();
            this.refresh();
        });
        buttonDec.setOnAction(ActionEvent -> {
            this.UneSuperImplemFournieParValentinLeBg.setOutputType(OutputType.SIG_DEC);
            alignMemory();
            this.refresh();
        });

        buttonUnsigDec.setOnAction(ActionEvent -> {
            this.UneSuperImplemFournieParValentinLeBg.setOutputType(OutputType.UNSIG_DEC);
            alignMemory();
            this.refresh();
        });
        buttonAscii.setOnAction(ActionEvent -> {
            button8Bit.fire();
            this.UneSuperImplemFournieParValentinLeBg.setOutputType(OutputType.ASCII);
            alignMemory();
            this.refresh();
        });

        TextField goToAddressField = (TextField) mainPane.lookup("#goToAddressField");
        goToAddressField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                String addressTyped = goToAddressField.getText();

                int newAddress = 0;
                try {
                    newAddress = Gui.parseUserAdress(addressTyped);
                } catch (FormatExeption formatExeption) {
                    return;
                }

                if (newAddress < 0 || newAddress > Ram.DEFAULT_RAM_SIZE - 4) { //TODO: 4 is the number of columns
                    Gui.warningPopup("The address provided is too big\nPlease stay between 0 and" + Ram.DEFAULT_RAM_SIZE, ActionEvent -> {});
                    return;
                }
                this.firstDisplayedAddress = newAddress;
                alignMemory();
                memoryScrollBar.setValue(this.firstDisplayedAddress);
                this.UneSuperImplemFournieParValentinLeBg.setOffset(firstDisplayedAddress);
                this.refresh();
            }
        });
    }

    public TableView<NewLineRam> getTableView() {
    	return this.tableView;
    }
    
    public DockNode getNode() {
        return dockNode;
    }

    private void alignMemory(){
        this.firstDisplayedAddress -= this.firstDisplayedAddress % this.UneSuperImplemFournieParValentinLeBg.getShowType().toOffset();
        memoryScrollBar.setValue(this.firstDisplayedAddress);
    }
}
