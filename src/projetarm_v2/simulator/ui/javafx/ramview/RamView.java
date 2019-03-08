package projetarm_v2.simulator.ui.javafx.ramview;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
import projetarm_v2.simulator.ui.javafx.Gui;

import java.io.IOException;

public class RamView {

    private AnchorPane mainPane;
    private DockNode dockNode;
    private Image dockImage;

    private ScrollBar memoryScrollBar;
    private TableView<NewLineRam> tableView;
    
    private int firstDisplayedAddress = 0x1000;
    
    private RamObservableListAdapter UneSuperImplemFournieParValentinLeBg;
    
    public RamView(ArmSimulator simulator) {
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
        this.tableView.setEditable(true);
        this.tableView.setSortPolicy(null);
        this.tableView.setOnKeyPressed(event -> {
            TablePosition<NewLineRam, ?> pos = this.tableView.getFocusModel().getFocusedCell() ;
            if (pos != null && (event.getCode().isDigitKey() || event.getCode().isLetterKey())) {
                this.tableView.edit(pos.getRow(), pos.getTableColumn());
            }
        });

        // single cell selection mode
        tableView.getSelectionModel().setCellSelectionEnabled(true);

        TableColumn<NewLineRam,String> line = new TableColumn<>("Address");
        line.setCellFactory(TextFieldTableCell.forTableColumn());
        line.setCellValueFactory(
                new PropertyValueFactory<>("line"));
        
        this.tableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        line.setMaxWidth( 1f * Integer.MAX_VALUE * 12 );
        this.tableView.getColumns().add(line);
        
        for (int i = 0; i < UneSuperImplemFournieParValentinLeBg.getColumns(); i++) {
	        TableColumn<NewLineRam,String> a = new TableColumn<>();
	        a.setCellFactory(TextFieldTableCell.forTableColumn());
	        a.setCellValueFactory(
	                new PropertyValueFactory<>(Character.toString('a'+i)));
	        a.setMaxWidth( 1f * Integer.MAX_VALUE * 11 );
	        this.tableView.getColumns().add(a);
        }

        tableView.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                // Get the table header
                Pane header = (Pane)tableView.lookup("TableHeaderRow");
                if(header!=null && header.isVisible()) {
                  header.setMaxHeight(0);
                  header.setMinHeight(0);
                  header.setPrefHeight(0);
                  header.setVisible(false);
                  header.setManaged(false);
                }
            }
        });
    	Button buttonRdm = (Button) mainPane.lookup("#buttonRdm");
    	buttonRdm.setOnAction(ActionEvent -> {
            simulator.setRandomPattern();
            this.refresh();
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
	        a.setCellFactory(TextFieldTableCell.forTableColumn());
	        a.setCellValueFactory(
	                new PropertyValueFactory<>(Character.toString('a'+i-1)));
	        a.setMaxWidth( 1f * Integer.MAX_VALUE * 11 );
	        this.tableView.getColumns().add(a);
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
    	});
    	button16Bit.setOnAction(ActionEvent -> {
            memoryScrollBar.setUnitIncrement(2);
            this.UneSuperImplemFournieParValentinLeBg.setShowType(ShowType.HALFWORD);
            alignMemory();
            this.refresh();
    	});
    	button32Bit.setOnAction(ActionEvent -> {
            memoryScrollBar.setUnitIncrement(4);
            this.UneSuperImplemFournieParValentinLeBg.setShowType(ShowType.WORD);
            alignMemory();
            this.refresh();
    	});

    	
        Button buttonHex = (Button) mainPane.lookup("#buttonHex");
        Button buttonDec = (Button) mainPane.lookup("#buttonDec");

        buttonHex.setOnAction(ActionEvent -> {
            this.UneSuperImplemFournieParValentinLeBg.setOutputType(OutputType.HEX);
            alignMemory();
            this.refresh();
        });
        buttonDec.setOnAction(ActionEvent -> {
            this.UneSuperImplemFournieParValentinLeBg.setOutputType(OutputType.NORMAL);
            alignMemory();
            this.refresh();
        });


        TextField goToAddressField = (TextField) mainPane.lookup("#goToAddressField");
        goToAddressField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                String addressTyped = goToAddressField.getText();

                int newAddress = 0;

                try {
                    if (addressTyped.startsWith("0x") || addressTyped.startsWith("0X")) {
                        newAddress = Integer.parseInt(addressTyped.substring(2), 16); // parsing a int in base 16, the 2
                        // first chars of the string are
                        // removed (0x)
                    } else if (addressTyped.startsWith("0b") || addressTyped.startsWith("0B")) {
                        newAddress = Integer.parseInt(addressTyped.substring(2), 2);
                    } else if (addressTyped.startsWith("0d") || addressTyped.startsWith("0D")) {
                        newAddress = Integer.parseInt(addressTyped.substring(2));
                    } else {
                        newAddress = Integer.parseInt(addressTyped);
                    }
                } catch (NumberFormatException exeption) {
                    Gui.warningPopup("Error in the number format\ntry with 0x1a35, 0b100101 or 0d300", ActionEvent -> {});
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
