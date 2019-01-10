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

import org.dockfx.DockNode;
import org.dockfx.DockPane;

public class RegistersView {

    TabPane mainPane;
    DockNode dockNode;
    Image dockImage;
    ArrayList<Text> registersLabel;
    ArrayList<Text> registersValues;
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
        
        this.registersValues = new ArrayList<Text>();

        updateRegisters();
        
    }
    
    public void updateRegisters(){
    	for(int i = 0; i<this.registersValues.size(); i++){
    		this.hexPane.getChildren().remove(this.registersValues.get(i));
    		this.sigHexPane.getChildren().remove(this.registersValues.get(i));
    		this.decPane.getChildren().remove(this.registersValues.get(i));
        }
    	this.registersValues.clear();
    	
    	for (int i = 0; i < 16; i++) {
    		Text register = new Text(10,20*(i+1), String.format("R%d = 0x%x", i,simulator.getRegisterValue(i)));
  		   	this.registersValues.add(register);
  		}
         
         Text flagN = new Text(10,20*(17), String.format("N : %s", simulator.getN()));
         Text flagZ = new Text(10,20*(18), String.format("Z : %s", simulator.getZ()));
         Text flagC = new Text(10,20*(19), String.format("C : %s", simulator.getC()));
         Text flagV = new Text(10,20*(20), String.format("V : %s", simulator.getV()));
         Text flagQ = new Text(10,20*(21), String.format("Q : %s", simulator.getQ()));
         
         this.registersValues.add(flagN);
         this.registersValues.add(flagZ);
         this.registersValues.add(flagC);
         this.registersValues.add(flagV);
         this.registersValues.add(flagQ);
         
         this.hexPane.getChildren().addAll(this.registersValues);
         
         this.hexTab.setContent(this.hexPane);
    }

    DockNode getNode(){
        return dockNode;
    }

}
