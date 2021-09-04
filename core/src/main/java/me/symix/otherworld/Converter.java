package me.symix.otherworld;

import com.badlogic.gdx.math.Vector2;
import me.symix.otherworld.world.World;

public class Converter {


    public static Vector2 screenToMapCoords(float x, float y)
    {
        return new Vector2(x / World.TILESIZE, y / World.TILESIZE);
    }
}