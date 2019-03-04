package projetarm_v2.simulator.ui.javafx;

import org.dockfx.DockNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.core.io.IOLed;

public class LedView {

    ScrollPane mainPane;
    DockNode dockNode;
    Image dockImage;
    TextFlow textArea;
    AnchorPane anchorPane;
    VBox ledContainer;
    
    private ArmSimulator simulator;
    
    public LedView(ArmSimulator simulator){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
        
        
        this.mainPane = new ScrollPane();
        
        Image ledOff = new Image(getClass().getResource("/resources/ledOff.png").toExternalForm());
        Image ledOn = new Image(getClass().getResource("/resources/ledOn.png").toExternalForm());
        
        
        
        /* The first element in the VBox is one anchorPane containing 2 buttons : more led and less led */
        Button moreLedButton = new Button();
        moreLedButton.setText("More Led");
        
        moreLedButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	IOLed led = simulator.newIOLed();
            	
                AnchorPane newLedAddress = new AnchorPane();
                ImageView newLedImage = new ImageView();
                if(led.isOn() == true) {
                	newLedImage.setImage(ledOn);
                }else {
                	newLedImage.setImage(ledOff);
                }
                
                newLedImage.setLayoutX(0);
                newLedImage.setLayoutY(0);
                Text newAddress = new Text();
                newAddress.setText("Address : 0x" + led.getPortAddress() + " Bit N°" + led.shift);
                newAddress.setLayoutX(150);
                newAddress.setLayoutY(55);
                
                newLedAddress.getChildren().addAll(newLedImage, newAddress);
                ledContainer.getChildren().add(newLedAddress);
            }
        });
        
        Button lessLedButton = new Button();
        lessLedButton.setText("Less Led");
        lessLedButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	if(ledContainer.getChildren().size()-1 != 0) {
            		ledContainer.getChildren().remove(ledContainer.getChildren().size()-1);	
            	}
            	
            }
        });
          
        AnchorPane buttonAnchor = new AnchorPane();
        
        moreLedButton.setLayoutX(20);
        moreLedButton.setLayoutY(20);
        
        lessLedButton.setLayoutX(100);
        lessLedButton.setLayoutY(20);
        
        buttonAnchor.getChildren().addAll(moreLedButton, lessLedButton);
        

        ledContainer = new VBox(buttonAnchor);
        
        this.dockNode = new DockNode(mainPane, "Led Game", new ImageView(dockImage));
        
        dockNode.setPrefSize(460,666);
        
        this.mainPane.setContent(ledContainer);  
        this.mainPane.setFitToWidth(true);
        this.mainPane.setFitToHeight(true);  
        this.mainPane.setHmin(dockNode.getHeight());
    }
    
    public void refresh() {
    	return;
    }
    
    public DockNode getNode(){
        return dockNode;
    }
   
}



