package projetarm_v2.simulator.ui.javafx;

import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.dockfx.DockNode;
import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.core.Ram;

import java.io.IOException;

public class RamView {

    private static final int VERTICAL_SPACE_BETWEEN_TEXT = 10;

    private AnchorPane mainPane;
    private DockNode dockNode;
    private Image dockImage;

    private ArmSimulator simulator;

    private ScrollBar memoryScrollBar;
    private TableView<LineRam> tableView;

    private int firstDisplayedAddress = 0x1000;
    private int memoryDisplayMode = 8;

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
        
        TableColumn<LineRam,String> a = new TableColumn("a");
        a.setCellFactory(TextFieldTableCell.forTableColumn());
        a.setCellValueFactory(
                new PropertyValueFactory<LineRam, String>("a"));
 
        TableColumn<LineRam,String>  b = new TableColumn("b");
        b.setCellFactory(TextFieldTableCell.forTableColumn());
        b.setCellValueFactory(
                new PropertyValueFactory<LineRam, String>("b"));
 
        TableColumn<LineRam,String>  c = new TableColumn("c");
        c.setCellFactory(TextFieldTableCell.forTableColumn());
        c.setCellValueFactory(
                new PropertyValueFactory<LineRam, String>("c"));

        TableColumn<LineRam,String>  d = new TableColumn("d");
        d.setCellFactory(TextFieldTableCell.forTableColumn());
        d.setCellValueFactory(
                new PropertyValueFactory<LineRam, String>("d"));
        
        this.tableView.getColumns().addAll(a, b, c, d);
                
        loadButonsEvents();
    }

    public void refresh() {
    	this.tableView.refresh();
    }
    
    private void loadButonsEvents() {
        memoryScrollBar = (ScrollBar) mainPane.lookup("#memoryScrollBar");
        memoryScrollBar.setMin(0);
        memoryScrollBar.setValue(this.firstDisplayedAddress);
        memoryScrollBar.setUnitIncrement(1);
        memoryScrollBar.setMax(2 *1024*1024); //TODO: set proper value

        memoryScrollBar.setOnScroll((ScrollEvent scrollEvent) -> {
            this.firstDisplayedAddress = (int) memoryScrollBar.getValue();
            this.UneSuperImplemFournieParValentinLeBg.setOffset(firstDisplayedAddress);
            this.tableView.refresh();
            //updateContents();
        });
        this.mainPane.setOnScroll((ScrollEvent scrollEvent) -> {
            if (scrollEvent.getDeltaY() < 0) {
                updateNewFirstAddress(-1);
            }
            else {
                updateNewFirstAddress(1);
            }
            //updateContents();
        });

        Button button8Bit = (Button) mainPane.lookup("#button8Bit");
    	Button button16Bit = (Button) mainPane.lookup("#button16Bit");
    	Button button32Bit = (Button) mainPane.lookup("#button32Bit");

    	button8Bit.setOnMouseClicked((MouseEvent mouseEvent) -> {
    		this.memoryDisplayMode = 8;
            memoryScrollBar.setUnitIncrement(1);
            firstDisplayedAddress = firstDisplayedAddress - firstDisplayedAddress % (memoryDisplayMode/8);
            //updateContents();
    	});
    	button16Bit.setOnMouseClicked((MouseEvent mouseEvent) -> {
    		this.memoryDisplayMode = 16;
            memoryScrollBar.setUnitIncrement(2);
            firstDisplayedAddress = firstDisplayedAddress - firstDisplayedAddress % (memoryDisplayMode/8);
            //updateContents();
    	});
    	button32Bit.setOnMouseClicked((MouseEvent mouseEvent) -> {
    		this.memoryDisplayMode = 32;
            memoryScrollBar.setUnitIncrement(4);
            firstDisplayedAddress = firstDisplayedAddress - firstDisplayedAddress % (memoryDisplayMode/8);
    		//updateContents();
    	});

        Text memoryViewTitleText = (Text) mainPane.lookup("#goToAddressLabel");
        TextField goToAddressField = (TextField) this.mainPane.lookup("#goToAddressField");
        goToAddressField.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                String addressTyped = goToAddressField.getText();
                int newAddress = 0;
                try {
                    newAddress = parseUserInput(addressTyped);
                } catch (NumberFormatException exeption) {
                    memoryViewTitleText.setText("invalidAddress");
                    memoryViewTitleText.setUnderline(true);
                    return;
                }

                if (newAddress < 0 || newAddress > Ram.DEFAULT_RAM_SIZE) {
                    memoryViewTitleText.setText("invalidAddress");
                    memoryViewTitleText.setUnderline(true);
                    return;
                }
                firstDisplayedAddress = newAddress;
                memoryScrollBar.setValue(firstDisplayedAddress);
                memoryViewTitleText.setText("goToAddress:");
                memoryViewTitleText.setUnderline(false);
                //updateContents();
            }
        });
    }

    static int parseUserInput(String input) {
        /*int parsedNumber;
        if (input.startsWith("0x") || input.startsWith("0X")) {
            parsedNumber = Integer.parseInt(input.substring(2), 16); // parsing a int in base 16, the 2

        } else if (input.startsWith("0b") || input.startsWith("0B")) {
            parsedNumber = Integer.parseInt(input.substring(2), 2); // parsing a int in base 2, the 2

        } else if (input.startsWith("0d") || input.startsWith("0D")) {
            parsedNumber = Integer.parseInt(input.substring(2)); // parsing a int in base 10, the 2

        } else {
            parsedNumber = Integer.parseInt(input);
        }
        return parsedNumber;*/
        return 0;
    }

    private void updateNewFirstAddress(int delta) {
        int oldAddress = this.firstDisplayedAddress;

        switch (this.memoryDisplayMode) {
            case 8:
                this.firstDisplayedAddress += -delta;
                break;
            case 16:
                this.firstDisplayedAddress += -2 * delta;
                break;
            case 32:
                this.firstDisplayedAddress += -4 * delta;
                break;
            default:
                this.memoryDisplayMode = 8;
        }

        if (firstDisplayedAddress < 0 || firstDisplayedAddress > Ram.DEFAULT_RAM_SIZE) {
            this.firstDisplayedAddress = oldAddress;
        }
    }

    public DockNode getNode() {
        return dockNode;
    }
}
