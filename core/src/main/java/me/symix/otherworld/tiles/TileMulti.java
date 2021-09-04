package me.symix.otherworld.tiles;

public class TileMulti extends Tile{

    public TileMulti(int parentX, int parentY, Tile parent){
        super(-1, parent.solid, parent.opaque, false, parent.tier, parent.mineSound, null);

        this.parent = parent;
        this.parentX = parentX;
        this.parentY = parentY;
    }
}
