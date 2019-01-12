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

    private TabPane mainPane;
    private DockNode dockNode;
    private Image dockImage;
    private ArrayList<Text> registersHex;
    private ArrayList<Text> registersSigDec;
    private ArrayList<Text> registersDec;
    private ArmSimulator simulator;
    private Pane hexPane;
    private Pane sigDecPane;
    private Pane decPane;
    private Tab hexTab;
    private Tab sigDecTab;
    private Tab decTab;
    
    private Text flagnHex;
    private Text flagzHex;
    private Text flagcHex;
    private Text flagvHex;
    private Text flagqHex;
    
    private Text flagnSigDec;
    private Text flagzSigDec;
    private Text flagcSigDec;
    private Text flagvSigDec;
    private Text flagqSigDec;
    
    private Text flagnDec;
    private Text flagzDec;
    private Text flagcDec;
    private Text flagvDec;
    private Text flagqDec;
    
    public RegistersView(ArmSimulator simulator){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
    	this.simulator = simulator;
    	
        this.hexPane = new Pane();
        this.sigDecPane = new Pane();
        this.decPane = new Pane();

        this.mainPane = new TabPane();
        this.hexTab = new Tab("Hexa", hexPane);
        this.sigDecTab = new Tab("Sig Hexa", sigDecPane);
        this.decTab = new Tab("Decimal", decPane);
        this.mainPane.getTabs().addAll(hexTab, sigDecTab, decTab);
        
        this.dockNode = new DockNode(mainPane, "Register View", new ImageView(dockImage));
        this.dockNode.setPrefSize(300, 100);
        
        this.registersHex = new ArrayList<Text>();
        this.registersSigDec = new ArrayList<Text>();
        this.registersDec = new ArrayList<Text>();
        
        for (int i = 0; i < 16; i++) {
    		String space = (i < 10) ? "  " : "";

        	Text registerHex = new Text(10,20*(i+1), String.format("R%d"+ space +" = 0x%x", i,simulator.getRegisterValue(i)));
  		   	this.registersHex.add(registerHex);
  		   	
		   	Text registerDec = new Text(10,20*(i+1), String.format("R%d"+ space +" = %s", i, Integer.toUnsignedString(simulator.getRegisterValue(i))));
  		   	this.registersDec.add(registerDec);
  		   	
  		   	Text registerSigDec = new Text(10,20*(i+1), String.format("R%d"+ space +" = %d", i, simulator.getRegisterValue(i)));
		  	this.registersSigDec.add(registerSigDec);
  		}
        
        this.flagnHex = new Text(10,20*(17), String.format("N : %s", simulator.getN()));
        this.flagzHex = new Text(10,20*(18), String.format("Z : %s", simulator.getZ()));
        this.flagcHex = new Text(10,20*(19), String.format("C : %s", simulator.getC()));
        this.flagvHex = new Text(10,20*(20), String.format("V : %s", simulator.getV()));
        this.flagqHex = new Text(10,20*(21), String.format("Q : %s", simulator.getQ()));
        
        ArrayList<Text> flagCPSRHex = new ArrayList<Text>();
        flagCPSRHex.addAll(Arrays.asList(flagnHex,flagzHex,flagcHex,flagvHex,flagqHex));
        
        this.registersHex.addAll(flagCPSRHex);
        
        this.flagnSigDec = new Text(10,20*(17), String.format("N : %s", simulator.getN()));
        this.flagzSigDec = new Text(10,20*(18), String.format("Z : %s", simulator.getZ()));
        this.flagcSigDec = new Text(10,20*(19), String.format("C : %s", simulator.getC()));
        this.flagvSigDec = new Text(10,20*(20), String.format("V : %s", simulator.getV()));
        this.flagqSigDec = new Text(10,20*(21), String.format("Q : %s", simulator.getQ()));
        
        ArrayList<Text> flagCPSRSigHex = new ArrayList<Text>();
        flagCPSRSigHex.addAll(Arrays.asList(flagnSigDec,flagzSigDec,flagcSigDec,flagvSigDec,flagqSigDec));

        this.registersSigDec.addAll(flagCPSRSigHex);
        
        this.flagnDec = new Text(10,20*(17), String.format("N : %s", simulator.getN()));
        this.flagzDec = new Text(10,20*(18), String.format("Z : %s", simulator.getZ()));
        this.flagcDec = new Text(10,20*(19), String.format("C : %s", simulator.getC()));
        this.flagvDec = new Text(10,20*(20), String.format("V : %s", simulator.getV()));
        this.flagqDec = new Text(10,20*(21), String.format("Q : %s", simulator.getQ()));
        
        ArrayList<Text> flagCPSRDec = new ArrayList<Text>();
        flagCPSRDec.addAll(Arrays.asList(flagnDec,flagzDec,flagcDec,flagvDec,flagqDec));

        this.registersDec.addAll(flagCPSRDec);
        
        this.hexPane.getChildren().addAll(this.registersHex);
        this.sigDecPane.getChildren().addAll(this.registersSigDec);
        this.decPane.getChildren().addAll(this.registersDec);
        
        this.hexTab.setContent(this.hexPane);
        this.sigDecTab.setContent(this.sigDecPane);
        this.decTab.setContent(this.decPane);
    }
    
    public void updateRegisters(){

    	for (int i = 0; i < 16; i++) {
    		String space = (i < 10) ? "  " : "";
    		this.registersHex.get(i).setText(String.format("R%d"+ space +" = 0x%x", i,simulator.getRegisterValue(i)));
  		   	this.registersDec.get(i).setText(String.format("R%d"+ space +" = %s", i, Integer.toUnsignedString(simulator.getRegisterValue(i))));
  		   	this.registersSigDec.get(i).setText(String.format("R%d"+ space +" = %d", i, simulator.getRegisterValue(i)));
  		}
         
    	 this.flagnHex.setText(String.format("N : %s", simulator.getN()));
    	 this.flagzHex.setText(String.format("Z : %s", simulator.getZ()));
    	 this.flagcHex.setText(String.format("C : %s", simulator.getC()));
    	 this.flagvHex.setText(String.format("V : %s", simulator.getV()));
    	 this.flagqHex.setText(String.format("Q : %s", simulator.getQ()));
         
         this.flagnSigDec.setText(String.format("N : %s", simulator.getN()));
         this.flagzSigDec.setText(String.format("Z : %s", simulator.getZ()));
         this.flagcSigDec.setText(String.format("C : %s", simulator.getC()));
         this.flagvSigDec.setText(String.format("V : %s", simulator.getV()));
         this.flagqSigDec.setText(String.format("Q : %s", simulator.getQ()));
 
         this.flagnDec.setText(String.format("N : %s", simulator.getN()));
         this.flagzDec.setText(String.format("Z : %s", simulator.getZ()));
         this.flagcDec.setText(String.format("C : %s", simulator.getC()));
         this.flagvDec.setText(String.format("V : %s", simulator.getV()));
         this.flagqDec.setText(String.format("Q : %s", simulator.getQ()));
    }

    DockNode getNode(){
        return dockNode;
    }
}
