package me.symix.otherworld;

import me.symix.otherworld.tiles.Tile;

import java.util.ArrayList;

public class Recipe {

    public ItemEntry result;
    public ArrayList<ItemEntry> cost;
    public Tile craftingStation;

    public Recipe(Tile craftingStation, ItemEntry result, ItemEntry... cost){
        this.craftingStation = craftingStation;
        this.result = result;
        this.cost = new ArrayList<ItemEntry>();
        for(ItemEntry e : cost){
            this.cost.add(e);
        }
    }
}
