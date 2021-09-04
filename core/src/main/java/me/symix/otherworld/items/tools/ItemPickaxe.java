package me.symix.otherworld.items.tools;

import me.symix.otherworld.tiles.AllTiles;

public class ItemPickaxe extends ItemTool {

    public int power;

    public ItemPickaxe(int power, int itemSpeed, String name, String texture){
        super(itemSpeed, name, texture);
        this.power = power;

        harvestableTiles.add(AllTiles.TileStone);
        harvestableTiles.add(AllTiles.TileDirt);
        harvestableTiles.add(AllTiles.TileGrass);
        harvestableTiles.add(AllTiles.TileTorch);
    }
}
