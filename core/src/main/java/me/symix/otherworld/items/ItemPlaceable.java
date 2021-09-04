package me.symix.otherworld.items;

import me.symix.otherworld.tiles.ParentTile;

public class ItemPlaceable extends Item {

    public ParentTile placeable;

    public ItemPlaceable(ParentTile placeable, String name, String texture){
        super(6, 999, name, texture);
        this.placeable = placeable;
    }
}
