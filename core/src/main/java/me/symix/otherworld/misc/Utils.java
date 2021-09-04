package me.symix.otherworld.misc;

import java.awt.*;

public class Utils {

    public static boolean isInside(int x, int y, int maxWidth, int maxHeight){
        return (x > 0 && x < maxWidth && y > 0 && y < maxHeight);
    }

    public static float getAngle(int x, int y, int tX, int tY) {
        float angle = (float) Math.toDegrees(Math.atan2(tY - y, tX - x));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }

    public static float getVolume(Point p1, Point p2, float maxDistance){
        float distance = (float)p1.distance(p2);

        float volume = (maxDistance - distance) / maxDistance;
        if(volume < 0) volume = 0;
        return volume;
    }
}
