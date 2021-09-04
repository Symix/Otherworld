package me.symix.otherworld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import me.symix.otherworld.tiles.Tile;
import me.symix.otherworld.tiles.Wall;

public class Biome {

    public Color skyColor;
    public Texture[] background;
    public Tile layer1Tile;
    public Tile layer2Tile;
    public Tile layer3Tile;
    public Wall layer2Wall;
    public Wall layer3Wall;
    public String name;

    public int treeType;

    public int bgOff1;
    public int bgOff2;

    public Biome(Tile layer1Tile, Tile layer2Tile, Wall layer2Wall, Tile layer3Tile, Wall layer3Wall, Color skyColor, String name, int bgOff1, int bgOff2, Texture[] background){
        this(layer1Tile, layer2Tile, layer2Wall, layer3Tile, layer3Wall, -1, skyColor, name, bgOff1, bgOff2, background);
    }

    public Biome(Tile layer1Tile, Tile layer2Tile, Wall layer2Wall, Tile layer3Tile, Wall layer3Wall, int treeType, Color skyColor, String name, int bgOff1, int bgOff2, Texture[] background){
        this.layer1Tile = layer1Tile;
        this.layer2Tile = layer2Tile;
        this.layer2Wall = layer2Wall;
        this.layer3Tile = layer3Tile;
        this.layer3Wall = layer3Wall;

        this.treeType = treeType;

        this.skyColor = skyColor;
        this.name = name;

        this.bgOff1 = bgOff1;
        this.bgOff2 = bgOff2;
        this.background = background;
    }
}
