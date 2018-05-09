import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import simulator.*; //TODO on dois peut etre pas le laisser la

public class GUItests extends Application {

    Parent root;
    Scene scene;

    Stage stage;


    List<Text> hexadecimalRegisterText;
    List<Text> decimalRegisterText;
    List<Text> signedDdecimalRegisterText;

    List<Text> MemoryAddressViewList;
    List<Text> MemoryContentList;

    int memoryViewFirstAddress;
    int memoryDisplayMode;


    //testing
    Memory myMemory;

    public void startIHM() {
        launch(null);
    }

    @Override
    public void start(Stage stage) throws Exception {

        //setting "static" elements

        this.stage = stage;

        root = FXMLLoader.load(getClass().getResource("ihm_#@rm.fxml"));
        scene = new Scene(root, 700, 275);

        stage.setMaximized(true);
        stage.setMinHeight(800);
        stage.setMinWidth(800);
        stage.setTitle("#@RM");
        stage.setScene(scene);

        stage.show();

        //setting up "moving" elements

        //the buttons

        Button button8bitView = (Button) scene.lookup("#button8Bit");
        Button button16bitView = (Button) scene.lookup("#button16Bit");
        Button button32bitView = (Button) scene.lookup("#button32Bit");

        button8bitView.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                memoryDisplayMode=8;
                updateMemoryView();
            }
        });
        button16bitView.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                memoryDisplayMode = 16;
                updateMemoryView();
            }
        });
        button32bitView.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                memoryDisplayMode = 32;
                updateMemoryView();
            }
        });

        Button memoryButtonUp  = (Button) scene.lookup("#memoryButtonUp");
        Button memoryButtonDown  = (Button) scene.lookup("#memoryButtonDown");

        memoryButtonUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                switch(memoryDisplayMode){
                    case 8:
                        memoryViewFirstAddress--;
                    case 16:
                        memoryViewFirstAddress-=2;
                    case 32:
                        memoryViewFirstAddress-=4;
                }
                updateMemoryView();
            }
        });
        memoryButtonDown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                switch(memoryDisplayMode){
                    case 8:
                        memoryViewFirstAddress++;
                    case 16:
                        memoryViewFirstAddress+=2;
                    case 32:
                        memoryViewFirstAddress+=4;
                }
                updateMemoryView();
            }
        });

        //the console output

        OutputStream consoleOut = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                Platform.runLater(() ->{
                    TextFlow consoleTextFlow = (TextFlow) scene.lookup("#consoleTextFlow");
                    consoleTextFlow.getChildren().add(new Text((String.valueOf((char) b))));
                });
            }
        };
        System.setOut(new PrintStream(consoleOut, true));


        //the register view

        hexadecimalRegisterText = new ArrayList<Text>();
        decimalRegisterText = new ArrayList<Text>();
        signedDdecimalRegisterText = new ArrayList<Text>();

        for(int register=0; register<16; register++){
            hexadecimalRegisterText.add((Text) scene.lookup("#register"+register+"Hex"));
            decimalRegisterText.add((Text) scene.lookup("#register"+register+"Dec"));
            signedDdecimalRegisterText.add((Text) scene.lookup("#register"+register+"SigDec"));
        }

        //the memory view

        myMemory = new Ram(1000);//testing!!
        Address anAddress = new Address(4);
        myMemory.setByte(anAddress, (byte)14);


        MemoryAddressViewList = new ArrayList<Text>();
        MemoryContentList = new ArrayList<Text>();

        for(int address=1; address<9; address++){
            MemoryAddressViewList.add((Text) scene.lookup("#addresslMemoryView"+address));
            MemoryContentList.add((Text) scene.lookup("#contentMemoryView"+address));
        }

        memoryViewFirstAddress = 0;
        memoryDisplayMode = 8;

        updateMemoryView();

        stage.show();

    }

    /**
     * Update the displayed registers
     * @param registersValues
     * 		the int values of the registers
     */
    public void updateRegisters(int[] registersValues){
        for(int register=0; register<16; register++){
            hexadecimalRegisterText.get(register).setText(Integer.toHexString(registersValues[register]));
            decimalRegisterText.get(register).setText(""+Integer.toUnsignedLong(registersValues[register]));
            signedDdecimalRegisterText.get(register).setText(""+registersValues[register]);
        }
        stage.show();
    }


    private void updateMemoryView(){
        int displayableMemoryRows = 8; //temporaire!!!

        int displayedMemoryAddress = memoryViewFirstAddress;


        switch(this.memoryDisplayMode){
            case 8:
                for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++){
                    MemoryAddressViewList.get(labelNumber).setText(Integer.toHexString(displayedMemoryAddress));
                    MemoryContentList.get(labelNumber).setText(""+myMemory.getByte(new Address(displayedMemoryAddress)));
                    displayedMemoryAddress++;
                }
            case 16:
                for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++){
                    MemoryAddressViewList.get(labelNumber).setText(Integer.toHexString(displayedMemoryAddress));
                    MemoryContentList.get(labelNumber).setText(""+myMemory.getHWord(new Address(displayedMemoryAddress)));
                    displayedMemoryAddress+=2;
                }
            case 32:
                for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++) {
                    MemoryAddressViewList.get(labelNumber).setText(Integer.toHexString(displayedMemoryAddress));
                    MemoryContentList.get(labelNumber).setText("" + myMemory.getValue(new Address(displayedMemoryAddress)));
                    displayedMemoryAddress += 4;
                }
            default:
                this.memoryDisplayMode=8;
        }


    }

}
