package projetarm_v2.simulator.ui.javafx.ramview;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import org.dockfx.DockNode;

import projetarm_v2.simulator.boilerplate.ArmSimulator;

import java.io.IOException;

public class RamView {

    private AnchorPane mainPane;
    private DockNode dockNode;
    private Image dockImage;

    private ArmSimulator simulator;

    private ScrollBar memoryScrollBar;
    private TableView<LineRam> tableView;
    
    private TableColumn<LineRam,String> a;
    private TableColumn<LineRam,String> c;
    private TableColumn<LineRam,String> b;
    private TableColumn<LineRam,String> d;

    private boolean shouldRefresh = true;
    
    private int firstDisplayedAddress = 0x1000;

    private RamObservableListAdapter UneSuperImplemFournieParValentinLeBg;
    
    public RamView(ArmSimulator simulator) {
        //dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
        try {
            mainPane = FXMLLoader.load(getClass().getResource("/resources/MemoryView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.simulator = simulator;
        
        dockNode = new DockNode(mainPane, "Ram View", new ImageView(dockImage));
        dockNode.setPrefSize(300, 100);

        mainPane.setStyle("-fx-line-spacing: -0.4em;");
        this.dockNode.getStylesheets().add("/resources/style.css");

        this.tableView = (TableView<LineRam>) mainPane.lookup("#tableView");
        
        UneSuperImplemFournieParValentinLeBg = new RamObservableListAdapter(simulator.getRam());
        UneSuperImplemFournieParValentinLeBg.setOffset(this.firstDisplayedAddress);
        
        this.tableView.setItems(UneSuperImplemFournieParValentinLeBg);
        this.tableView.setEditable(true);
        
        this.tableView.setOnKeyPressed(event -> {
            TablePosition<LineRam, ?> pos = this.tableView.getFocusModel().getFocusedCell() ;
            if (pos != null && (event.getCode().isDigitKey() || event.getCode().isLetterKey())) {
                this.tableView.edit(pos.getRow(), pos.getTableColumn());
            }
        });

        // single cell selection mode
        tableView.getSelectionModel().setCellSelectionEnabled(true);

        a = new TableColumn<LineRam, String>("a");
        a.setCellFactory(TextFieldTableCell.forTableColumn());
        a.setCellValueFactory(
                new PropertyValueFactory<LineRam, String>("a"));
 
        b = new TableColumn<LineRam, String>("b");
        b.setCellFactory(TextFieldTableCell.forTableColumn());
        b.setCellValueFactory(
                new PropertyValueFactory<LineRam, String>("b"));
 
        c = new TableColumn<LineRam, String>("c");
        c.setCellFactory(TextFieldTableCell.forTableColumn());
        c.setCellValueFactory(
                new PropertyValueFactory<LineRam, String>("c"));

        d = new TableColumn<LineRam, String>("d");
        d.setCellFactory(TextFieldTableCell.forTableColumn());
        d.setCellValueFactory(
                new PropertyValueFactory<LineRam, String>("d"));

        this.tableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        a.setMaxWidth( 1f * Integer.MAX_VALUE * 25 );
        b.setMaxWidth( 1f * Integer.MAX_VALUE * 25 );
        c.setMaxWidth( 1f * Integer.MAX_VALUE * 25 );
        d.setMaxWidth( 1f * Integer.MAX_VALUE * 25 );
        this.tableView.getColumns().addAll(a, b, c, d);
                
        
        loadButonsEvents();
        
        this.refresh();
    }

    public void refresh() {
    	int Aaddress;
    	int Baddress;
    	int Caddress;
    	int Daddress;
    	
    	switch(this.UneSuperImplemFournieParValentinLeBg.getShowType()) {
    	default:
    	case BYTE:
    		Aaddress = this.firstDisplayedAddress;
    		Baddress = this.firstDisplayedAddress+1;
    		Caddress = this.firstDisplayedAddress+2;
    		Daddress = this.firstDisplayedAddress+3;
    		break;
    	case HALFWORD:
    		Aaddress = this.firstDisplayedAddress;
    		Baddress = this.firstDisplayedAddress+2;
    		Caddress = this.firstDisplayedAddress+4;
    		Daddress = this.firstDisplayedAddress+6;
    		break;
    	case WORD:
    		Aaddress = this.firstDisplayedAddress;
    		Baddress = this.firstDisplayedAddress+4;
    		Caddress = this.firstDisplayedAddress+8;
    		Daddress = this.firstDisplayedAddress+12;
    	}
    	a.setText("0x" + Integer.toHexString(Aaddress));
    	b.setText("0x" + Integer.toHexString(Baddress));
    	c.setText("0x" + Integer.toHexString(Caddress));
    	d.setText("0x" + Integer.toHexString(Daddress));
    	
    	this.tableView.refresh();
    }
    
    private void loadButonsEvents() {
        memoryScrollBar = (ScrollBar) mainPane.lookup("#memoryScrollBar");
        memoryScrollBar.setMin(0);
        memoryScrollBar.setValue(this.firstDisplayedAddress);
        memoryScrollBar.setUnitIncrement(1);
        memoryScrollBar.setMax(2 *1024*1024); //TODO: set proper value
        
        memoryScrollBar.valueProperty().addListener((ObservableValue<? extends Number> ov, 
                Number old_val, Number new_val) -> {
                    this.firstDisplayedAddress = new_val.intValue();
                    this.UneSuperImplemFournieParValentinLeBg.setOffset(firstDisplayedAddress);
                    this.refresh();
        });
        
        tableView.setOnScroll((ScrollEvent event) -> {
        	if (event.getDeltaY() < 0) {
        		memoryScrollBar.setValue(this.firstDisplayedAddress + 1);
        	} else {
        		memoryScrollBar.setValue(this.firstDisplayedAddress - 1);
        	}
            this.firstDisplayedAddress = (int) memoryScrollBar.getValue();
            this.UneSuperImplemFournieParValentinLeBg.setOffset(firstDisplayedAddress);
            this.refresh();
        });
        
        Button button8Bit = (Button) mainPane.lookup("#button8Bit");
    	Button button16Bit = (Button) mainPane.lookup("#button16Bit");
    	Button button32Bit = (Button) mainPane.lookup("#button32Bit");

    	button8Bit.setOnMouseClicked((MouseEvent mouseEvent) -> {
            memoryScrollBar.setUnitIncrement(1);
            
            this.UneSuperImplemFournieParValentinLeBg.setOutputType(OutputType.HEX);
            this.UneSuperImplemFournieParValentinLeBg.setShowType(ShowType.BYTE);
            
            this.refresh();
    	});
    	button16Bit.setOnMouseClicked((MouseEvent mouseEvent) -> {
            memoryScrollBar.setUnitIncrement(2);
            firstDisplayedAddress -= firstDisplayedAddress % 2;
            memoryScrollBar.setValue(firstDisplayedAddress);
            
            UneSuperImplemFournieParValentinLeBg.setOffset(firstDisplayedAddress);
            
            this.UneSuperImplemFournieParValentinLeBg.setOutputType(OutputType.HEX);
            this.UneSuperImplemFournieParValentinLeBg.setShowType(ShowType.HALFWORD);
            
            this.refresh();
    	});
    	button32Bit.setOnMouseClicked((MouseEvent mouseEvent) -> {
            memoryScrollBar.setUnitIncrement(4);
            
            firstDisplayedAddress -= firstDisplayedAddress % 4;
            memoryScrollBar.setValue(firstDisplayedAddress);
            
            UneSuperImplemFournieParValentinLeBg.setOffset(firstDisplayedAddress);
            
            this.UneSuperImplemFournieParValentinLeBg.setOutputType(OutputType.HEX);
            this.UneSuperImplemFournieParValentinLeBg.setShowType(ShowType.WORD);
            
            this.refresh();
    	});
    }

    public DockNode getNode() {
        return dockNode;
    }
}
