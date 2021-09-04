package me.symix.otherworld.items.weapons;

import me.symix.otherworld.items.Item;

public class ItemWeapon extends Item {

    public int damage;

    public ItemWeapon(int damage, int itemSpeed, String name, String texture){
        super(itemSpeed, 1, name, texture);
        this.damage = damage;
    }

}