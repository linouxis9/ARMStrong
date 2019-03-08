package projetarm_v2.simulator.ui.javafx;

import org.dockfx.DockNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
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
    
    Image ledOff;
    Image ledOn;
    
    private ArmSimulator simulator;
    
    public LedView(ArmSimulator simulator){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
        
        
        this.mainPane = new ScrollPane();
        
        ledOff = new Image(getClass().getResource("/resources/ledOff.png").toExternalForm());
        ledOn = new Image(getClass().getResource("/resources/ledOn.png").toExternalForm());
        
        
        
        /* The first element in the VBox is one anchorPane containing 2 buttons : more led and less led */
        Button moreLedButton = new Button();
        moreLedButton.setText("More Led");
        
        moreLedButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	IOLed led = simulator.newIOLed();
            	
                AnchorPane newLedAddress = new AnchorPane();
                ImageView newLedImage = new ImageView();
                if(led.isOn()) {
                	newLedImage.setImage(ledOn);
                }else {
                	newLedImage.setImage(ledOff);
                }
                
                newLedImage.setLayoutX(0);
                newLedImage.setLayoutY(0);
                Text newAddress = new Text();
                newAddress.setText("Address : 0x" + Long.toHexString(led.getPortAddress()) + " Bit N°" + led.shift);
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
    	for (Node _pane : ledContainer.getChildren()) {
    		AnchorPane pane = (AnchorPane)_pane;
    		
    		ImageView ledImage = (ImageView)pane.getChildren().get(0);
    		
           /* if (led.isOn()) {
            	ledImage.setImage(ledOn);
            }else {
            	ledImage.setImage(ledOff);
            } Comment on fait pour mettre à jour Hugo si tu gardes pas l'objet de la led quelque part pour connaître son état ? */
    	}
    }
    
    public DockNode getNode(){
        return dockNode;
    }
   
}



