package me.symix.otherworld;

import com.badlogic.gdx.math.Vector2;

public class Constants {

    public static String[] qualities = { "None", "Low Quality", "Medium Quality", "Medium Quality+", "Shader (Pixel)", "Shader (Realistic)" };
    public static int[] resolutions = { 800, 1024, 1280, 1600, 1920 };
    public static float invof = 6;
    public static int treelimit = 10000000;
    public static int chestlimit = 20000000;
    public static int moblimit = 30000000;
    public static Vector2 leftVec = new Vector2(-1,0);
    public static Vector2 rightVec = new Vector2(1,0);
    public static Vector2 upVec = new Vector2(0,1);
    public static float upAngle = upVec.angle();
    public static float leftAngle = leftVec.angle();
    public static float rightAngle = rightVec.angle();
    public static float gravity = 1000f;

    public static int[] lightDistances = {2,6,12,20,30};
    public static String[] lightDistancesNames = { "Very Low", "Low", "Medium", "High", "Very High" };

}