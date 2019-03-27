/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

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

/**
 * The preferences window
 */
public class Preferences {
	
	private Stage preferencesStage;
	/**
	 * Creates a new instance of a preferences window
	 * @param simulator
	 */
    public Preferences(ArmSimulator simulator, Gui gui){
    	preferencesStage = new Stage();
    	
        preferencesStage.setTitle("Preferences");

        preferencesStage.initModality(Modality.APPLICATION_MODAL);

        try {
            Pane main = FXMLLoader.load(Gui.class.getResource("/resources/preferences.fxml"));
            preferencesStage.setScene(new Scene(main, 500, 280));
            preferencesStage.setResizable(false);

            TextField programAt = (TextField) main.lookup("#programAt");
            programAt.setText(String.format("0x%x",simulator.getStartingAddress()));
            
            Button cleanRdm = (Button) main.lookup("#cleanRdm");
            cleanRdm.setOnAction(ActionEvent -> {
                simulator.removeRandomPattern();
                System.out.println("[INFO] Random RAM pattern removed !");
                gui.getArmMenuBar().getReloadMenuItem().fire();
            });

            
            Button buttonRdm = (Button) main.lookup("#buttonRdm");
            buttonRdm.setOnAction(ActionEvent -> {
                simulator.setRandomPattern();
                System.out.println("[INFO] Random RAM pattern set !");
                simulator.setProgram(simulator.getProgramFromSave());
                gui.getArmMenuBar().getReloadMenuItem().fire();
            });

            Button applyAndCloseButton = (Button) main.lookup("#applyAndCloseButton");
            applyAndCloseButton.setOnAction(e -> {
            	try {
	            	int startingAddress = Gui.parseUserAdress(programAt.getText());
	            	if ((startingAddress % 4) != 0) {
	            		Gui.warningPopup("The startingAddress should be aligned on a word bound.", (_e) -> {});
	            		return;
	            	}
	                preferencesStage.close();
            	} catch (FormatException exception) {}
            });
            preferencesStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
