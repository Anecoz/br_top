package logic.menu;

import utils.Config;

public class OptionsItem extends MenuItem {

    public OptionsItem() {

    }

    public static void swapVsync(){
        if(Config.CONFIG_VSYNC == 1)
            Config.CONFIG_VSYNC = 0;
        else
            Config.CONFIG_VSYNC = 1;
    }

    public static void swapMultiSample(){
        if(Config.CONFIG_SAMPLES == 2)
            Config.CONFIG_SAMPLES = 4;
        else if(Config.CONFIG_SAMPLES == 4)
            Config.CONFIG_SAMPLES = 8;
        else if(Config.CONFIG_SAMPLES == 8)
            Config.CONFIG_SAMPLES = 16;
        else if(Config.CONFIG_SAMPLES == 16)
            Config.CONFIG_SAMPLES = 32;
        else
            Config.CONFIG_SAMPLES = 2;
    }

    public static void swapResolution(){
        if(Config.CONFIG_RES_HEIGHT == 576) {
            Config.CONFIG_RES_WIDTH = 1920;
            Config.CONFIG_RES_HEIGHT = 1080;
        } else if(Config.CONFIG_RES_HEIGHT == 1080) {
            Config.CONFIG_RES_WIDTH = 1280;
            Config.CONFIG_RES_HEIGHT = 720;
        } else {
            Config.CONFIG_RES_WIDTH = 1024;
            Config.CONFIG_RES_HEIGHT = 576;
        }
    }
}
