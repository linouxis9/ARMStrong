package projetarm_v2.simulator.ui.javafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import projetarm_v2.simulator.boilerplate.ArmSimulator;

import org.dockfx.DockNode;

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
	private RegisterObjectView currentAddressHex;
	private RegisterObjectView currentAddressSigDec;
	private RegisterObjectView currentAddressDec;
	
	private TableView<RegisterObjectView> tableHex;
	private TableView<RegisterObjectView> tableSigDec;
	private TableView<RegisterObjectView> tableDec;
	
	private TableColumn<RegisterObjectView, String> nameHexCol;
	private TableColumn<RegisterObjectView, String> valueHexCol;
	private TableColumn<RegisterObjectView, String> nameSigDecCol;
	private TableColumn<RegisterObjectView, String> valueSigDecCol;
	private TableColumn<RegisterObjectView, String> nameDecCol;
	private TableColumn<RegisterObjectView, String> valueDecCol;
	
    @SuppressWarnings("unchecked")
	public RegistersView(ArmSimulator simulator){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
    	this.simulator = simulator;
    	
        this.hexPane = new Pane();
        this.sigDecPane = new Pane();
        this.decPane = new Pane();
        
        this.hexPane.getStyleClass().add("contentTabPane");
        this.sigDecPane.getStyleClass().add("contentTabPane");
        this.decPane.getStyleClass().add("contentTabPane");
 
        this.tableHex = new TableView<RegisterObjectView>();
        this.tableSigDec = new TableView<RegisterObjectView>();
        this.tableDec = new TableView<RegisterObjectView>();
        
        this.tableHex.getStyleClass().add("registersTable");
        this.tableSigDec.getStyleClass().add("registersTable");
        this.tableDec.getStyleClass().add("registersTable");
        
        this.hexPane.getChildren().add(this.tableHex);
        this.sigDecPane.getChildren().add(this.tableSigDec);
        this.decPane.getChildren().add(this.tableDec);
        
    	this.nameHexCol = new TableColumn<RegisterObjectView, String>();
    	this.valueHexCol = new TableColumn<RegisterObjectView, String>();
    	this.nameSigDecCol = new TableColumn<RegisterObjectView, String>();
    	this.valueSigDecCol = new TableColumn<RegisterObjectView, String>();
    	this.nameDecCol = new TableColumn<RegisterObjectView, String>();
    	this.valueDecCol = new TableColumn<RegisterObjectView, String>(); 
    	
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
    	
    	this.tableHex.widthProperty().addListener(new ChangeListener<Number>() {
    	    @Override
    	    public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
    	        Pane header = (Pane) tableHex.lookup("TableHeaderRow");
    	        if (header.isVisible()){
    	            header.setMaxHeight(0);
    	            header.setMinHeight(0);
    	            header.setPrefHeight(0);
    	            header.setVisible(false);
    	        }
    	    }
    	});
    	
    	this.tableSigDec.widthProperty().addListener(new ChangeListener<Number>() {
    	    @Override
    	    public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
    	        Pane header = (Pane) tableSigDec.lookup("TableHeaderRow");
    	        if (header.isVisible()){
    	            header.setMaxHeight(0);
    	            header.setMinHeight(0);
    	            header.setPrefHeight(0);
    	            header.setVisible(false);
    	        }
    	    }
    	});
    	
    	this.tableDec.widthProperty().addListener(new ChangeListener<Number>() {
    	    @Override
    	    public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
    	        Pane header = (Pane) tableDec.lookup("TableHeaderRow");
    	        if (header.isVisible()){
    	            header.setMaxHeight(0);
    	            header.setMinHeight(0);
    	            header.setPrefHeight(0);
    	            header.setVisible(false);
    	        }
    	    }
    	});
    	
    	this.tableHex.setEditable(true);
        this.tableSigDec.setEditable(true);
        this.tableDec.setEditable(true);
    	
    	this.nameHexCol.setCellValueFactory(new PropertyValueFactory<RegisterObjectView, String>("nameRegister"));
    	this.valueHexCol.setCellValueFactory(new PropertyValueFactory<RegisterObjectView, String>("valueRegister"));
    	this.nameSigDecCol.setCellValueFactory(new PropertyValueFactory<RegisterObjectView, String>("nameRegister"));
    	this.valueSigDecCol.setCellValueFactory(new PropertyValueFactory<RegisterObjectView, String>("valueRegister"));
    	this.nameDecCol.setCellValueFactory(new PropertyValueFactory<RegisterObjectView, String>("nameRegister"));
    	this.valueDecCol.setCellValueFactory(new PropertyValueFactory<RegisterObjectView, String>("valueRegister"));
    	
    	this.valueHexCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	this.valueSigDecCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	this.valueDecCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	
    	this.valueHexCol.setOnEditCommit(
            new EventHandler<CellEditEvent<RegisterObjectView, String>>() {
                @Override
                public void handle(CellEditEvent<RegisterObjectView, String> t) {
                	String newValue = t.getNewValue();
                	((RegisterObjectView) t.getTableView().getItems()
                    		.get(t.getTablePosition().getRow())).setValueRegister(newValue);
                	String[] hex = newValue.split("x");
                    int value = Integer.parseInt(hex[1], 16);  
                	simulator.setRegisterValue(((RegisterObjectView) t.getTableView().getItems()
                    		.get(t.getTablePosition().getRow())).getRegister(), value);
                	updateRegisters();                
                }
            }
        );
    	
    	this.valueSigDecCol.setOnEditCommit(
            new EventHandler<CellEditEvent<RegisterObjectView, String>>() {
                @Override
                public void handle(CellEditEvent<RegisterObjectView, String> t) {
                	String newValue = t.getNewValue();
                	((RegisterObjectView) t.getTableView().getItems()
                    		.get(t.getTablePosition().getRow())).setValueRegister(newValue);
                	simulator.setRegisterValue(((RegisterObjectView) t.getTableView().getItems()
                    		.get(t.getTablePosition().getRow())).getRegister(), Integer.parseInt(newValue));
                	updateRegisters();
                }
            }
        );
    	
    	this.valueDecCol.setOnEditCommit(
            new EventHandler<CellEditEvent<RegisterObjectView, String>>() {
                @Override
                public void handle(CellEditEvent<RegisterObjectView, String> t) {
                	String newValue = t.getNewValue();
                	((RegisterObjectView) t.getTableView().getItems()
                    		.get(t.getTablePosition().getRow())).setValueRegister(newValue);
                	simulator.setRegisterValue(((RegisterObjectView) t.getTableView().getItems()
                    		.get(t.getTablePosition().getRow())).getRegister(), Integer.parseInt(newValue));
                	updateRegisters();
                }
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
        	
        	String registerHex = String.format("0x%x", simulator.getRegisterValue(i));	
  		   	this.registersHex.add(new RegisterObjectView(i, nameRegister, registerHex));
  		   	
		   	String registerDec = String.format("%s", Integer.toUnsignedString(simulator.getRegisterValue(i)));
  		   	this.registersDec.add(new RegisterObjectView(i, nameRegister, registerDec));
  		   	
  		   	String registerSigDec = String.format("%d", simulator.getRegisterValue(i));
		  	this.registersSigDec.add(new RegisterObjectView(i, nameRegister, registerSigDec));
  		}
        
        this.flagHex = new RegisterObjectView(0, "[FLAGS]",simulator.getCpu().getCPSR().toString());
        this.flagSigDec = new RegisterObjectView(0, "[FLAGS]",simulator.getCpu().getCPSR().toString());
        this.flagDec = new RegisterObjectView(0, "[FLAGS]",simulator.getCpu().getCPSR().toString());

        this.registersHex.add(flagHex);
        this.registersSigDec.add(flagSigDec);
        this.registersDec.add(flagDec);       
        
        
        this.currentAddressHex = new RegisterObjectView(0, "nextAddress", String.format("0x%s", Long.toHexString(simulator.getCpu().getCurrentAddress())));
        this.currentAddressSigDec = new RegisterObjectView(0, "nextAddress", Long.toString(simulator.getCpu().getCurrentAddress()));
        this.currentAddressDec =  new RegisterObjectView(0, "nextAddress", Long.toUnsignedString(simulator.getCpu().getCurrentAddress()));
        
        this.registersHex.add(currentAddressHex);
        this.registersSigDec.add(currentAddressSigDec);
        this.registersDec.add(currentAddressDec);
        
    	this.tableHex.setItems(this.registersHex);
        this.tableSigDec.setItems(this.registersSigDec);
        this.tableDec.setItems(this.registersDec);
        
        this.hexTab.setContent(this.hexPane);
        this.sigDecTab.setContent(this.sigDecPane);
        this.decTab.setContent(this.decPane);

    }
    
    public void updateRegisters(){

    	for (int i = 0; i < 16; i++) {
    		this.registersHex.get(i).setValueRegister(String.format("0x%x", simulator.getRegisterValue(i)));
    		this.registersDec.get(i).setValueRegister(String.format("%s", Integer.toUnsignedString(simulator.getRegisterValue(i))));
    		this.registersSigDec.get(i).setValueRegister(String.format("%d", simulator.getRegisterValue(i)));
    	}
         
    	this.flagHex.setValueRegister(simulator.getCpu().getCPSR().toString());
        this.flagSigDec.setValueRegister(simulator.getCpu().getCPSR().toString());
        this.flagDec.setValueRegister(simulator.getCpu().getCPSR().toString());
        
        this.currentAddressHex.setValueRegister(String.format("0x%s", Long.toHexString(simulator.getCpu().getCurrentAddress())));
        this.currentAddressSigDec.setValueRegister(Long.toString(simulator.getCpu().getCurrentAddress()));
        this.currentAddressDec.setValueRegister(Long.toUnsignedString(simulator.getCpu().getCurrentAddress()));

        refreshColumnData();
    }

    public void refreshColumnData() {
    	this.tableHex.getColumns().get(1).setVisible(false);
        this.tableSigDec.getColumns().get(1).setVisible(false);
        this.tableDec.getColumns().get(1).setVisible(false);
        
        this.tableHex.getColumns().get(1).setVisible(true);
        this.tableSigDec.getColumns().get(1).setVisible(true);
        this.tableDec.getColumns().get(1).setVisible(true);  
    }
    
    DockNode getNode(){
        return dockNode;
    }
}
