package projetarm_v2.simulator.ui.javafx;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import projetarm_v2.simulator.boilerplate.ArmSimulator;

import java.util.ArrayList;
import java.util.Arrays;

import org.dockfx.DockNode;
import org.dockfx.DockPane;

public class RegistersView {

    TabPane mainPane;
    DockNode dockNode;
    Image dockImage;
    ArrayList<Text> registersHex;
    ArrayList<Text> registersSigHex;
    ArrayList<Text> registersDec;
    ArmSimulator simulator;
    Pane hexPane;
    Pane sigHexPane;
    Pane decPane;
    Tab hexTab;
    Tab sigHexTab;
    Tab decTab;
    
    public RegistersView(ArmSimulator simulator){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
    	this.simulator = simulator;
		
        this.hexPane = new Pane();
        this.sigHexPane = new Pane();
        this.decPane = new Pane();

        this.mainPane = new TabPane();
        this.hexTab = new Tab("Hexa", hexPane);
        this.sigHexTab = new Tab("Sig Hexa", sigHexPane);
        this.decTab = new Tab("Decimal", decPane);
        this.mainPane.getTabs().addAll(hexTab, sigHexTab, decTab);
        
        this.dockNode = new DockNode(mainPane, "Register View", new ImageView(dockImage));
        this.dockNode.setPrefSize(300, 100);
        
        this.registersHex = new ArrayList<Text>();
        this.registersSigHex = new ArrayList<Text>();
        this.registersDec = new ArrayList<Text>();

        updateRegisters();
        
    }
    
    public void updateRegisters(){
    	for(int i = 0; i<this.registersHex.size(); i++){
    		this.hexPane.getChildren().remove(this.registersHex.get(i));
    		this.sigHexPane.getChildren().remove(this.registersSigHex.get(i));
    		this.decPane.getChildren().remove(this.registersDec.get(i));
        }
    	this.registersHex.clear();
    	this.registersSigHex.clear();
    	this.registersDec.clear();
    	
    	for (int i = 0; i < 16; i++) {
    		Text registerHex = new Text(10,20*(i+1), String.format("R%d = 0x%x", i,simulator.getRegisterValue(i)));
  		   	this.registersHex.add(registerHex);
  		   	
		   	Text registerDec = new Text(10,20*(i+1), String.format("R%d = %d", i, simulator.getRegisterValue(i)));
  		   	this.registersDec.add(registerDec);
  		   	
  		   	int valueDec = simulator.getRegisterValue(i);
  		   	int contentSigHex =  (valueDec > 32767) ? valueDec - 65536 : valueDec;
  		  	Text registerSigHex = new Text(10,20*(i+1), String.format("R%d = 0x%x", i, contentSigHex));
  		  	this.registersSigHex.add(registerSigHex);
  		}
         
         Text flagnHex = new Text(10,20*(17), String.format("N : %s", simulator.getN()));
         Text flagzHex = new Text(10,20*(18), String.format("Z : %s", simulator.getZ()));
         Text flagcHex = new Text(10,20*(19), String.format("C : %s", simulator.getC()));
         Text flagvHex = new Text(10,20*(20), String.format("V : %s", simulator.getV()));
         Text flagqHex = new Text(10,20*(21), String.format("Q : %s", simulator.getQ()));
         
         ArrayList<Text> flagCPSRHex = new ArrayList<Text>();
         flagCPSRHex.addAll(Arrays.asList(flagnHex,flagzHex,flagcHex,flagvHex,flagqHex));
         
         this.registersHex.addAll(flagCPSRHex);
         
         Text flagnSigHex = new Text(10,20*(17), String.format("N : %s", simulator.getN()));
         Text flagzSigHex = new Text(10,20*(18), String.format("Z : %s", simulator.getZ()));
         Text flagcSigHex = new Text(10,20*(19), String.format("C : %s", simulator.getC()));
         Text flagvSigHex = new Text(10,20*(20), String.format("V : %s", simulator.getV()));
         Text flagqSigHex = new Text(10,20*(21), String.format("Q : %s", simulator.getQ()));
         
         ArrayList<Text> flagCPSRSigHex = new ArrayList<Text>();
         flagCPSRSigHex.addAll(Arrays.asList(flagnSigHex,flagzSigHex,flagcSigHex,flagvSigHex,flagqSigHex));

         this.registersSigHex.addAll(flagCPSRSigHex);
         
         Text flagnDec = new Text(10,20*(17), String.format("N : %s", simulator.getN()));
         Text flagzDec = new Text(10,20*(18), String.format("Z : %s", simulator.getZ()));
         Text flagcDec = new Text(10,20*(19), String.format("C : %s", simulator.getC()));
         Text flagvDec = new Text(10,20*(20), String.format("V : %s", simulator.getV()));
         Text flagqDec = new Text(10,20*(21), String.format("Q : %s", simulator.getQ()));
         
         ArrayList<Text> flagCPSRDec = new ArrayList<Text>();
         flagCPSRDec.addAll(Arrays.asList(flagnDec,flagzDec,flagcDec,flagvDec,flagqDec));

         this.registersDec.addAll(flagCPSRDec);
         
         this.hexPane.getChildren().addAll(this.registersHex);
         this.sigHexPane.getChildren().addAll(this.registersSigHex);
         this.decPane.getChildren().addAll(this.registersDec);
         
         this.hexTab.setContent(this.hexPane);
         this.sigHexTab.setContent(this.sigHexPane);
         this.decTab.setContent(this.decPane);
    }

    DockNode getNode(){
        return dockNode;
    }

}
