package projetarm_v2.simulator.ui.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import projetarm_v2.simulator.boilerplate.ArmSimulator;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Preferences {

    public Preferences(ArmSimulator simulator){

        AtomicInteger programStartAddress = new AtomicInteger(simulator.getStartingAddress());
        AtomicInteger initRamValue = new AtomicInteger();
        AtomicBoolean initRamValueChanged = new AtomicBoolean(false);

        final Stage preferencesStage = new Stage();
        preferencesStage.setTitle("Preferences");

        preferencesStage.initModality(Modality.APPLICATION_MODAL);

        try {
            Pane main = FXMLLoader.load(Gui.class.getResource("/resources/preferences.fxml"));
            preferencesStage.setScene(new Scene(main, 500, 280));

            TextField programAt = (TextField) main.lookup("#programAt");
            programAt.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                if (!newPropertyValue)
                    try {
                        programStartAddress.set(Gui.parseUserAdress(programAt.getText()));
                    } catch (FormatExeption formatExeption) {
                        return;
                    }
                });

            TextField randomRam = (TextField) main.lookup("#randomRam");
            randomRam.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                if (!newPropertyValue)
                    try {
                        initRamValue.set(Gui.parseUserAdress(programAt.getText()));
                        if (initRamValue.get()>128){
                            Gui.warningPopup("ram can only be filled by bytes\ntry a value between 0x00 and 0xff", a->{});
                            return;
                        }
                        initRamValueChanged.set(true);
                    } catch (FormatExeption formatExeption) {
                        return;
                    }
            });


            Button applyAndCloseButton = (Button) main.lookup("#applyAndCloseButton");
            applyAndCloseButton.setOnAction(e -> {
                simulator.setStartingAddress(programStartAddress.get());
                if (initRamValueChanged.get()) simulator.setRandomPattern((byte)initRamValue.get());
                preferencesStage.close();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        preferencesStage.show();
    }
}
