/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.ui.javafx;

import java.util.ArrayList;
import java.util.List;

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
    
    List<IOLed> ledArray = new ArrayList<IOLed>();
    List<ImageView> imageArrayList = new ArrayList<ImageView>();
    
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
                
                ledArray.add(led);
            	imageArrayList.add(newLedImage);
            	
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
                System.out.println(ledArray.size());
                newLedAddress.getChildren().addAll(newLedImage, newAddress);
                ledContainer.getChildren().add(newLedAddress);
            }
        });
        
        Button lessLedButton = new Button();
        lessLedButton.setText("Less Led");
        lessLedButton.setOnAction((ActionEvent event) -> {
            	if(ledContainer.getChildren().size()-1 > 0) {
            		
            		IOLed led = ledArray.remove(ledArray.size()-1);
            		//imageArrayList.remove(imageArrayList.size()-1);
            		simulator.removeIOComponent(led);
            		System.out.println(ledArray.size());
            		ledContainer.getChildren().remove(ledContainer.getChildren().size()-1);	
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
    		
    		for (int i = 0; i < ledArray.size(); i++)	{
    			if (ledArray.get(i).isOn()) {
    				imageArrayList.get(i).setImage(ledOn);
                }else {
                	imageArrayList.get(i).setImage(ledOff);
                }
    		}
           
    	}
    }
    
    public DockNode getNode(){
        return dockNode;
    }
   
}



