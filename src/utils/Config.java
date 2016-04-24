package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static Properties config;
    private static InputStream in;

    public static float CONFIG_VOLUME;
    public static int CONFIG_VSYNC;

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
        // Display all the values in the form of key value
        for (String key : config.stringPropertyNames()) {
            String value = config.getProperty(key);
            System.out.println("Key: " + key + " Value: " + value);
        }
    }

    /*public static <T> getProperty(){

    }*/
}
