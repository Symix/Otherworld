package me.symix.otherworld.items.tools;

import me.symix.otherworld.tiles.AllWalls;

public class ItemHammer extends ItemTool{

    public ItemHammer(int itemSpeed, String name, String texture){
        super(itemSpeed, name, texture);

        harvestableTiles.add(AllWalls.Dirt);
        harvestableTiles.add(AllWalls.Stone);
    }
}
