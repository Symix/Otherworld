package me.symix.otherworld.tiles;


import com.badlogic.gdx.audio.Sound;
import me.symix.otherworld.TileTier;

import java.awt.*;
import java.util.ArrayList;

public class Tile extends ParentTile{

    public boolean solid;

    public boolean opaque;

    public TileTier tier;

    public Sound mineSound;

    public boolean multiTile;

    public int multiWidth = -1;
    public int multiHeight = -1;

    public Tile parent = null;

    public int parentX;
    public int parentY;

    public boolean isPlatform = false;

    public Tile(int id, boolean solid, boolean opaque, boolean multiTile, TileTier tier, Sound mineSound, String texture){
        super(id,"tiles/" + texture + ".png");

        this.solid = solid;
        this.opaque = opaque;
        this.multiTile = multiTile;
        this.tier = tier;
        this.mineSound = mineSound;
    }
}
