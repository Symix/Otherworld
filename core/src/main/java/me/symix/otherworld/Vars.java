package me.symix.otherworld;

public class Vars {

    public static boolean fullBright = false;
    public static boolean superSpeed = false;
    public static boolean unlimitedRange = false;
    public static boolean fastPlace = false;
    public static boolean fly = false;
    public static boolean noclip = false;

    public static int lightDistance = 20;

    public static String name = "John";

    public static float volume = 1f;

    public static void disableCheats() {
        fullBright = false;
        superSpeed = false;
        unlimitedRange = false;
        fastPlace = false;
        fly = false;
        noclip = false;
    }

    public static void enableCheats(){
        fullBright = true;
        superSpeed = true;
        unlimitedRange = true;
        fastPlace = true;
        fly = true;
        noclip = true;
    }

    public static void toggleCheats(){
        if(fullBright){
            disableCheats();
        } else {
            enableCheats();
        }
    }
}