package simulator;

public class About {
	public static String name = "#@rm Simulator";
	public static String version = "v1.0";
	public static String copyright = "Copyright (C) 2018 Project #@rm Simulator";
	public static String developers = "Valentin D'Emmanuele, Gilles Mertens, Nicolas Gervasi, Hugo Chemarin, Dylan Fraisse";
	
	public static String info() {
		return name + " " + version + " by " + developers + System.lineSeparator() + copyright;
	}
}
