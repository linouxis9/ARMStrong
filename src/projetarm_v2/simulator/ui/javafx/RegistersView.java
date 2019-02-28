package projetarm_v2.simulator.ui.javafx;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import projetarm_v2.simulator.boilerplate.ArmSimulator;

import java.util.ArrayList;

import org.dockfx.DockNode;

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
    
    private Text flagHex;
    private Text flagSigDec;
    
    private Text flagDec;
    
    public RegistersView(ArmSimulator simulator){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
    	this.simulator = simulator;
    	
        this.hexPane = new Pane();
        this.sigDecPane = new Pane();
        this.decPane = new Pane();

        this.mainPane = new TabPane();
        this.hexTab = new Tab("Hexa", hexPane);
        this.sigDecTab = new Tab("Sig Dec", sigDecPane);
        this.decTab = new Tab("Decimal", decPane);
        this.mainPane.getTabs().addAll(hexTab, sigDecTab, decTab);
        this.mainPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        this.dockNode = new DockNode(mainPane, "Register View", new ImageView(dockImage));
        this.dockNode.setPrefSize(300, 100);
        this.dockNode.getStylesheets().add("/resources/style.css");
        
        this.registersHex = new ArrayList<>();
        this.registersSigDec = new ArrayList<>();
        this.registersDec = new ArrayList<>();
        
        for (int i = 0; i < 16; i++) {
    		String space = (i < 10) ? "  " : "";

        	Text registerHex = new Text(10,20*(i+1), String.format("R%d"+ space +" : 0x%x", i,simulator.getRegisterValue(i)));
  		   	this.registersHex.add(registerHex);
  		   	
		   	Text registerDec = new Text(10,20*(i+1), String.format("R%d"+ space +" : %s", i, Integer.toUnsignedString(simulator.getRegisterValue(i))));
  		   	this.registersDec.add(registerDec);
  		   	
  		   	Text registerSigDec = new Text(10,20*(i+1), String.format("R%d"+ space +" : %d", i, simulator.getRegisterValue(i)));
		  	this.registersSigDec.add(registerSigDec);
  		}
        
        this.flagHex = new Text(10,20*(17), String.format("[FLAGS] : %s", simulator.getCpu().getCPSR().toString()));
        this.flagSigDec = new Text(10,20*(17), String.format("[FLAGS] : %s", simulator.getCpu().getCPSR().toString()));
        this.flagDec = new Text(10,20*(17), String.format("[FLAGS] : %s", simulator.getCpu().getCPSR().toString()));

        this.registersHex.add(flagHex);
        this.registersSigDec.add(flagSigDec);
        this.registersDec.add(flagDec);
        
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
    		this.registersHex.get(i).setText(String.format("R%d"+ space +" : 0x%x", i,simulator.getRegisterValue(i)));
  		   	this.registersDec.get(i).setText(String.format("R%d"+ space +" : %s", i, Integer.toUnsignedString(simulator.getRegisterValue(i))));
  		   	this.registersSigDec.get(i).setText(String.format("R%d"+ space +" : %d", i, simulator.getRegisterValue(i)));
  		}
         
    	 this.flagHex.setText(String.format("[FLAGS] : %s", simulator.getCpu().getCPSR().toString()));
    	 this.flagDec.setText(String.format("[FLAGS] : %s", simulator.getCpu().getCPSR().toString()));
    	 this.flagSigDec.setText(String.format("[FLAGS] : %s", simulator.getCpu().getCPSR().toString()));

    }

    DockNode getNode(){
        return dockNode;
    }
}
