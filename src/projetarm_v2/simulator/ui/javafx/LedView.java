/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.ui.javafx;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.dockfx.DockNode;
import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.core.io.IOLed;

import java.util.ArrayList;
import java.util.List;

public class LedView {

    ScrollPane mainPane;
    DockNode dockNode;
    Image dockImage;
    TextFlow textArea;
    AnchorPane anchorPane;    
    VBox ledContainer;
    HBox gameContainer;
    Image ledOff;
    Image ledOn;
    
    List<IOLed> ledArray = new ArrayList<IOLed>();
    List<ImageView> imageArrayList = new ArrayList<ImageView>();
    
    private ArmSimulator simulator;

    /**
     * Creates a new instance of Node
     * @param simulator the arm simulator
     */
    public LedView(ArmSimulator simulator){

        this.simulator = simulator;

        this.mainPane = new ScrollPane();
        
        ledOff = new Image(getClass().getResource("/resources/ledOff.png").toExternalForm());
        ledOn = new Image(getClass().getResource("/resources/ledOn.png").toExternalForm());
        
        gameContainer = new HBox();
        ledContainer = new VBox();
        AnchorPane buttonContainer = new AnchorPane();
        
        for(int i=0 ; i < 8 ; i++) {
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
            
            newLedAddress.getChildren().addAll(newLedImage, newAddress);
            ledContainer.getChildren().add(newLedAddress);
        }       
        
        ToggleButton leverButton =  new ToggleButton("", new ImageView(new Image(getClass().getResource("/resources/leverButton.png").toExternalForm())));
        Button pushingButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/pushingButton.png").toExternalForm())));
        
        pushingButton.setLayoutX(20);
        pushingButton.setLayoutY(20);
        
        leverButton.setLayoutX(100);
        leverButton.setLayoutY(20);
        
        buttonContainer.getChildren().addAll(pushingButton, leverButton);
        
        gameContainer.getChildren().addAll(ledContainer, buttonContainer);
        
        this.dockNode = new DockNode(mainPane, "Led Game", new ImageView(dockImage));
        
        dockNode.setPrefSize(460,666);
        
        this.mainPane.setContent(gameContainer);  
        this.mainPane.setFitToWidth(true);
        this.mainPane.setFitToHeight(true);  
        this.mainPane.setHmin(dockNode.getHeight());
    }

    /**
     * gets and display the new leds state
     */
    public void refresh() {
    	for (int i = 0; i < ledArray.size(); i++)	{
    		if (ledArray.get(i).isOn()) {
    			imageArrayList.get(i).setImage(ledOn);
            } else {
               	imageArrayList.get(i).setImage(ledOff);
            }
    	}
    }

    /**
     * get the dock node
     * @return dockNode
     */
    public DockNode getNode(){
        return dockNode;
    }
   
}



