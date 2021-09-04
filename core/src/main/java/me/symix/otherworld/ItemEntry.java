package me.symix.otherworld;

import me.symix.otherworld.items.Item;

public class ItemEntry {

    public Item item;
    public int amount;

    public ItemEntry(Item item){
        this(item, 1);
    }

    public ItemEntry(Item item, int amount){
        this.item = item;
        this.amount = amount;
    }

    public ItemEntry clone(){
        return new ItemEntry(item, amount);
    }
}
