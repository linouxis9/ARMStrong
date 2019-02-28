package projetarm_v2.simulator.core.save;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import projetarm_v2.simulator.core.io.IOComponent;

public class Save implements Serializable {
	private static final long serialVersionUID = -7923487851230607241L;
	private double version = 1.0;
	private String copyright = "(C) 2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi";
	private String program;
	private List<IOComponent> components;
	private static Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(new TypeToken<List<IOComponent>>(){}.getRawType(), new InterfaceAdapterArrayList<IOComponent>()).create();
	
	public Save() {
		this.program = "";
		this.components = new ArrayList<>();
	}
	
	public static Save fromPath(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		String json = Files.readString(path);

		return Save.deserialize(json);
	}
	
	public String serialize() {
		return gson.toJson(this);
	}
	
	public static Save deserialize(String json) {
		return gson.fromJson(json, Save.class);
	}
	
	
	public void setProgram(String program) {
		this.program = program;
	}

	public String getProgram() {
		return this.program;
	}

	public void saveToFile(String filePath) throws IOException {
		Files.writeString(Paths.get(filePath), this.serialize());
	}

	public void addComponent(IOComponent component) {
		this.components.add(component);
	}

	public void removeComponent(IOComponent component) {
		this.components.remove(component);
	}
	
	public List<IOComponent> getComponentsAndReset() {
		List<IOComponent> toReturn = Collections.unmodifiableList(this.components);
		
		this.components = new ArrayList<>();
		
		return toReturn;
	}
}
