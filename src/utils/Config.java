package utils;

import java.io.*;
import java.util.Properties;

public class Config {

    private static Properties config;
    private static InputStream in;
    private static FileOutputStream out;

    public static float CONFIG_VOLUME = 0.5f;
    public static int CONFIG_VSYNC = 1;
    public static int CONFIG_SAMPLES = 4;
    public static int CONFIG_RES_WIDTH = 800;
    public static int CONFIG_RES_HEIGHT = 600;

    public Config(){
    }

    public static void loadConfig(){

        // Load config file
        config = new Properties();
        try {
            in = new FileInputStream("src/config/config.cfg");
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        try {
            config.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add missing properties
        if(config.getProperty("volume") == null)
            config.setProperty("volume", Float.toString(CONFIG_VOLUME));
        if(config.getProperty("vsync") == null)
            config.setProperty("vsync", Integer.toString(CONFIG_VSYNC));
        if(config.getProperty("samples") == null)
            config.setProperty("samples", Integer.toString(CONFIG_SAMPLES));
        if(config.getProperty("width") == null)
            config.setProperty("width", Integer.toString(CONFIG_RES_WIDTH));
        if(config.getProperty("height") == null)
            config.setProperty("height", Integer.toString(CONFIG_RES_HEIGHT));

        try {
            out = new FileOutputStream("src/config/config.cfg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            config.store(out, null);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assign keys to variables.
        CONFIG_VOLUME = Float.parseFloat(config.getProperty("volume"));
        CONFIG_VSYNC = Integer.parseInt(config.getProperty("vsync"));
        CONFIG_SAMPLES = Integer.parseInt(config.getProperty("samples"));
        CONFIG_RES_WIDTH = Integer.parseInt(config.getProperty("width"));
        CONFIG_RES_HEIGHT = Integer.parseInt(config.getProperty("height"));
    }

    public static void saveConfig() {
        config.setProperty("volume", Float.toString(CONFIG_VOLUME));
        config.setProperty("vsync", Integer.toString(CONFIG_VSYNC));
        config.setProperty("samples", Integer.toString(CONFIG_SAMPLES));
        config.setProperty("width", Integer.toString(CONFIG_RES_WIDTH));
        config.setProperty("height", Integer.toString(CONFIG_RES_HEIGHT));
        try {
            out = new FileOutputStream("src/config/config.cfg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            config.store(out, null);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
