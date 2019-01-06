package projetarm_v2.simulator.utils;

public class OSValidator {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isLinux() {
        return OS.contains("nux");
    }
}