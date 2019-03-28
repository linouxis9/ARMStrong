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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.dockfx.DockNode;
import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.core.io.IOButton;
import projetarm_v2.simulator.core.io.IOLed;
import projetarm_v2.simulator.core.io.IOSwitch;

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
    VBox buttonContainer;
    Image ledOff;
    Image ledOn;
    Image leverOff;
    Image leverOn;
    
    List<IOLed> ledArray = new ArrayList<IOLed>();
    List<ImageView> ledImageArrayList = new ArrayList<ImageView>();
    List<ImageView> leverImageArrayList = new ArrayList<ImageView>();
    List<IOSwitch> leverButtonArray = new ArrayList<IOSwitch>();    
    List<IOButton> pressButtonArray = new ArrayList<IOButton>();
    
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
        leverOff = new Image(getClass().getResource("/resources/leverOff.png").toExternalForm());
        leverOn = new Image(getClass().getResource("/resources/leverOn.png").toExternalForm()); 
          
        gameContainer = new HBox();
        ledContainer = new VBox();
        buttonContainer = new VBox();
                
        for(int i=0 ; i < 8 ; i++) { //creating 8 leds at the start of the view
        	IOLed led = simulator.newIOLed();
        	
        	AnchorPane newLedAddress = new AnchorPane();
            ImageView newLedImage = new ImageView();
            
            ledArray.add(led);
        	ledImageArrayList.add(newLedImage);
        	
        	if(led.isOn()) {
            	newLedImage.setImage(ledOn);
            }else {
            	newLedImage.setImage(ledOff);
            }       	

            newLedImage.setLayoutX(0);
            newLedImage.setLayoutY(0);
            Text newAddress = new Text();
            newAddress.setText("0x" + Long.toHexString(led.getPortAddress()) + " Bit N°" + led.shift);
            newAddress.setLayoutX(95);
            newAddress.setLayoutY(55);
            
            newLedAddress.getChildren().addAll(newLedImage, newAddress);
            ledContainer.getChildren().add(newLedAddress);
        }
        
        for(int i=0 ; i < 2 ; i++) { //creating 3 buttons at the start of the view
        	IOButton IOpressButton = simulator.newIOButton();
        	IOSwitch IOleverButton = simulator.newIOSwitch();
        	AnchorPane buttonAndTextAndLeverAndTextAnchorPane = new AnchorPane();
        	       	
        	Text leverText = new Text();
        	leverText.setText("0x" + Long.toHexString(IOpressButton.getPortAddress()) + " Bit N°" + IOpressButton.shift);
        	leverText.setLayoutX(30);
            leverText.setLayoutY(100);

            Text pushingText = new Text();
            pushingText.setText("0x" + Long.toHexString(IOleverButton.getPortAddress()) + " Bit N°" + IOleverButton.shift);
            pushingText.setLayoutX(30);
            pushingText.setLayoutY(190);
            
            ImageView lever = new ImageView(leverOff);            
        	Button leverButton =  new Button("", lever);
        	Button pushingButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/pushingButton.png").toExternalForm())));

        	leverButton.setOnAction(ActionEvent -> {
        		IOleverButton.flip();       
        		refresh();
            });
        	
        	pushingButton.setOnAction(ActionEvent -> {
        		IOpressButton.push();     
        		refresh();
            });


        	leverButton.setLayoutX(70);
            leverButton.setLayoutY(25);
        	pushingButton.setLayoutX(70);
            pushingButton.setLayoutY(115);        
            
        	leverButtonArray.add(IOleverButton);
        	pressButtonArray.add(IOpressButton);
        	leverImageArrayList.add(lever);
        	
        	buttonAndTextAndLeverAndTextAnchorPane.getChildren().addAll(leverButton, leverText, pushingButton, pushingText);
        	buttonContainer.getChildren().add(buttonAndTextAndLeverAndTextAnchorPane);
        }
        
        gameContainer.getChildren().addAll(ledContainer, buttonContainer);
        
        this.dockNode = new DockNode(mainPane, "Led Game", new ImageView(dockImage));
        
        dockNode.setPrefSize(300,666);
        
        this.mainPane.setContent(gameContainer);  
        this.mainPane.setFitToWidth(true);
        this.mainPane.setFitToHeight(true);  
        this.mainPane.setHmin(dockNode.getHeight());
    }

    /**
     * gets and display the new leds state
     */
    public void refresh() {    
    	for (int i = 0; i < leverButtonArray.size(); i++)	{
    		if (leverButtonArray.get(i).isOn()) {

    			leverImageArrayList.get(i).setImage(leverOn);
    		} else {
    			leverImageArrayList.get(i).setImage(leverOff);
    		}
    	}
    	for (int i = 0; i < ledArray.size(); i++)	{
    		if (ledArray.get(i).isOn()) {
    			ledImageArrayList.get(i).setImage(ledOn);
            } else {
               	ledImageArrayList.get(i).setImage(ledOff);
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

    /**
     * called when user pushes the close button on the dockfx node
     * closes the node and removes the components
     */
    public void close() {
        this.dockNode.close();
        for(IOLed led : ledArray){
            this.simulator.removeIOComponent(led);
        }
        for(IOSwitch lever : leverButtonArray){
            this.simulator.removeIOComponent(lever);
        }
        for(IOButton button : pressButtonArray){
            this.simulator.removeIOComponent(button);
        }
    }
}



