package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static Properties config;
    private static InputStream in;

    public static float CONFIG_VOLUME = 0.5f;
    public static int CONFIG_VSYNC = 1;
    public static int CONFIG_SAMPLES = 4;
    public static int CONFIG_RES_WIDTH = 800;
    public static int CONFIG_RES_HEIGHT = 600;

    public Config(){

    }

    public static void loadConfig(){
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
        // Assign keys to variables.
        CONFIG_VOLUME = Float.parseFloat(config.getProperty("volume"));
        CONFIG_VSYNC = Integer.parseInt(config.getProperty("vsync"));
        CONFIG_SAMPLES = Integer.parseInt(config.getProperty("samples"));
        CONFIG_RES_WIDTH = Integer.parseInt(config.getProperty("width"));
        CONFIG_RES_HEIGHT = Integer.parseInt(config.getProperty("height"));
    }
}
