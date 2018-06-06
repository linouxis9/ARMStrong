package simulator.ui.javafx;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIMenuBar {

	private List<MenuItem> diabledInExecMode;
	private List<MenuItem> enabledInExecMode;
	private MenuBar theMenuBar;

	private MenuItem newMenuItem;
	private MenuItem openMenuItem;
	private MenuItem saveMenuItem;
	private MenuItem saveAsMenuItem;
	private MenuItem exitMenuItem;
	private MenuItem enterExecutionModeMenuItem;
	private MenuItem exitExecutionModeMenuItem;
	private MenuItem runMenuItem;
	private MenuItem runSingleMenuItem;
	private MenuItem helpMenuItem;
	private MenuItem documentationMenuItem;
	private MenuItem reloadProgramMenuItem;
	private MenuItem preferencesMenuItem;
	private MenuItem stopMenuItem;

	private Menu fileMenu;
	private Menu editMenu;
	private Menu runMenu;
	private Menu helpMenu;
	
	private Map<String, Language> languages;
	private String language;
	
	public GUIMenuBar(MenuBar aMenuBar) {

		theMenuBar = aMenuBar;

		this.languages = GUI.getLanguagesData();
		
		this.language = GUI.getCurrentLanguage();
		
		diabledInExecMode = new ArrayList<MenuItem>();
		enabledInExecMode = new ArrayList<MenuItem>();

		changeLanguage();

		theMenuBar.getMenus().addAll(fileMenu, editMenu, runMenu, helpMenu);

		fileMenu.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem, preferencesMenuItem, new SeparatorMenuItem(), exitMenuItem);
		editMenu.getItems().addAll();
		runMenu.getItems().addAll(enterExecutionModeMenuItem, exitExecutionModeMenuItem, new SeparatorMenuItem(), runMenuItem, runSingleMenuItem, stopMenuItem, new SeparatorMenuItem(), reloadProgramMenuItem);
		helpMenu.getItems().addAll(helpMenuItem, documentationMenuItem);

		diabledInExecMode.add(newMenuItem);
		diabledInExecMode.add(openMenuItem);
		diabledInExecMode.add(saveMenuItem);
		diabledInExecMode.add(saveAsMenuItem);
		diabledInExecMode.add(enterExecutionModeMenuItem);

		enabledInExecMode.add(exitExecutionModeMenuItem);
		enabledInExecMode.add(runMenuItem);
		enabledInExecMode.add(runSingleMenuItem);
		enabledInExecMode.add(stopMenuItem);
		enabledInExecMode.add(reloadProgramMenuItem);

		exitMenuItem.setOnAction((ActionEvent actionEvent) -> {
			System.exit(0);
		});
		
		exitExecMode();

	}

	public void changeLanguage() {
		fileMenu.setText(this.languages.get(language).getTranslation("fileMenu"));
		editMenu.setText(this.languages.get(language).getTranslation("editMenu"));
		runMenu.setText(this.languages.get(language).getTranslation("runMenu"));
		helpMenu.setText(this.languages.get(language).getTranslation("helpMenu"));

		if(GUI.language.equals("French")) {
			newMenuItem.setText("Nouveau");
			openMenuItem.setText("Ouvrir");
			saveMenuItem.setText("Sauvegarder");
			saveAsMenuItem.setText("Sauvegarder sous");
			exitMenuItem.setText("Fermer");
			helpMenuItem.setText("Aide");
			
			enterExecutionModeMenuItem.setText("Passer en mode execution");
			exitExecutionModeMenuItem.setText("Sortir du mode execution");
			runMenuItem.setText("Lancer");
			runSingleMenuItem.setText("Lancer une instruction");
			reloadProgramMenuItem.setText("Recharger le Programme");
			stopMenuItem.setText("Stopper l'Execution");
			documentationMenuItem.setText("Documentation");
			preferencesMenuItem.setText("Pr�f�rences");
		}else{
			newMenuItem.setText("New");
			openMenuItem.setText("Open file");
			saveMenuItem.setText("Save");
			saveAsMenuItem.setText("Save as");
			exitMenuItem.setText("Exit");
			helpMenuItem.setText("Help");
			
			enterExecutionModeMenuItem.setText("Enter in execution mode");
			exitExecutionModeMenuItem.setText("Exit the execution mode");
			runMenuItem.setText("Run a single instruction");
			runSingleMenuItem.setText("Run a single instruction");
			reloadProgramMenuItem.setText("Reload Program");
			stopMenuItem.setText("Stop Execution");
			documentationMenuItem.setText("Documentation");
			preferencesMenuItem.setText("Preferences");
		}
		
	}
	
	public void setExecMode() {
		switchMenuState(false);
	}

	public void exitExecMode() {
		switchMenuState(true);
	}

	private void switchMenuState(boolean state) {
		for (int item = 0; item < diabledInExecMode.size(); item++) {
			diabledInExecMode.get(item).setDisable(!state);
		}
		for (int item=0; item < enabledInExecMode.size(); item++){
			enabledInExecMode.get(item).setDisable(state);
		}
	}

	public MenuItem getNewMenuItem() {
		return newMenuItem;
	}

	public MenuItem getOpenMenuItem() {
		return openMenuItem;
	}

	public MenuItem getSaveMenuItem() {
		return saveMenuItem;
	}

	public MenuItem getSaveAsMenuItem() {
		return saveAsMenuItem;
	}

	public MenuItem getEnterExecutionModeMenuItem() {
		return enterExecutionModeMenuItem;
	}

	public MenuItem getExitExecutionModeMenuItem() {
		return exitExecutionModeMenuItem;
	}

	public MenuItem getRunMenuItem() {
		return runMenuItem;
	}

	public MenuItem getRunSingleMenuItem() {
		return runSingleMenuItem;
	}

	public MenuItem getHelpMenuItem() {
		return helpMenuItem;
	}

	public MenuItem getReloadProgramMenuItem() {
		return reloadProgramMenuItem;
	}

	public MenuItem getDocumentationMenuItem() {
		return documentationMenuItem;
	}

	public MenuItem getPreferencesMenuItem() {
		return preferencesMenuItem;
	}

	public MenuItem getStopMenuItem() {
		return stopMenuItem;
	}
}
