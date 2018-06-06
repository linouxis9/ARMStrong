package simulator.ui.javafx;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public enum Language {
	ENGLISH(Collections.unmodifiableMap(initializeEnglish())), FRENCH(Collections.unmodifiableMap(initializeFrench()));
	
	private Map<String, String> language;

	private Language(Map<String,String> language) {
		this.language = language;
	}

	private static Map initializeEnglish() {
		Map<String,String> language = new HashMap<>();

		language.put("displayMode", "Display Mode");
		language.put("memoryViewTitleText", "Go to address");
		language.put("invalidAddress", "Invalid Address");

		language.put("File", "File");
		language.put("Edit", "Edit");
		language.put("Run", "Run");
		language.put("Help", "Help");

		language.put("New", "New");
		language.put("Open file", "Open file");
		language.put("Save", "Save");
		language.put("Save as", "Save as");
		language.put("Exit", "Exit");

		language.put("Enter in execution mode", "Enter in execution mode");
		language.put("Exit the execution mode", "Exit the execution mode");
		language.put("Run a single instruction", "Run a single instruction");
		language.put("Reload Program", "Reload Program");
		language.put("Stop Execution", "Stop Execution");
		language.put("Documentation", "Documentation");
		language.put("Preferences", "Preferences");

		language.put("Unsigned Decimal", "Unsigned Decimal");
		language.put("Signed Decimal", "Signed Decimal");
		language.put("Hexadecimal", "Hexadecimal");

		return language;
	}

    private static Map initializeFrench() {
        Map<String,String> language = new HashMap<>();

        language.put("displayMode", "Mode d'affichage");
        language.put("memoryViewTitleText", "Aller à l'adresse");
        language.put("invalidAddress", "Adresse Invalide");

        language.put("File", "Fichier");
        language.put("Edit", "Editer");
        language.put("Run", "Executer");
        language.put("Help", "Aide");

        language.put("New", "Nouveau");
        language.put("Open file", "Ouvrir");
        language.put("Save", "Enregistrer");
        language.put("Save as", "Enregistrer sous");
        language.put("Exit", "Quitter");

        language.put("Enter in execution mode", "Entrer en mode execution");
        language.put("Exit the execution mode", "Quitter le mode execution");
        language.put("Run a single instruction", "Executer une instruction");
        language.put("Reload Program", "Recharger le programme");
        language.put("Stop Execution", "Arrêter l'exécution");
        language.put("Documentation", "Documentation");
        language.put("Preferences", "Préférences");

        language.put("Unsigned Decimal", "Décimal non signé");
        language.put("Signed Decimal", "Décimal signé");
        language.put("Hexadecimal", "Hexadécimal");


        return language;
    }
	
	public String getTranslation(String name) {
		return this.language.get(name);
	}
}
