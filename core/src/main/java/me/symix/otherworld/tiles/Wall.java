package me.symix.otherworld.tiles;

import me.symix.otherworld.items.Item;

public class Wall extends ParentTile{

    public boolean trasparent;

    public Wall(int id, String texture){
        super(id, "walls/" + texture + ".png");
        trasparent = false;
    }

}
