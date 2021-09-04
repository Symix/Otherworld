package me.symix.otherworld;

import com.badlogic.gdx.graphics.Color;
import me.symix.otherworld.misc.AllTexture;
import me.symix.otherworld.tiles.AllTiles;
import me.symix.otherworld.tiles.AllWalls;

import java.util.ArrayList;

public class AllBiomes {

    public static Biome forest = new Biome(AllTiles.TileGrass, AllTiles.TileDirt, AllWalls.Dirt, AllTiles.TileStone, AllWalls.Stone, 0, new Color(0.454901961f, 0.568627451f, 0.968627451f, 1), "Forest", -300, -800, AllTexture.forest);
    public static Biome desert = new Biome(AllTiles.TileSand, AllTiles.TileSand, AllWalls.Sand, AllTiles.TileSandStone, AllWalls.SandStone, new Color(1f, 247f / 255f, 142f / 255f, 1), "Desert", -500, -650, AllTexture.desert);
    public static Biome taiga = new Biome(AllTiles.Snow, AllTiles.Snow, AllWalls.Dirt, AllTiles.TileStone, AllWalls.Stone, 1, new Color (1, 1, 1, 1), "Taiga", -250, -800, AllTexture.taiga);

    public static ArrayList<Biome> biomes = new ArrayList<Biome>();

    static{
        biomes.add(forest);
        biomes.add(desert);
        biomes.add(taiga);
    }

}
