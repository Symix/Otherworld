package me.symix.otherworld.items.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import me.symix.otherworld.items.Item;
import me.symix.otherworld.tiles.ParentTile;

import java.util.ArrayList;

public class ItemTool extends Item {

    public ArrayList<ParentTile> harvestableTiles;

    public Texture textureHand;
    public Sprite spriteHand;

    public ItemTool(int itemSpeed, String name, String texture){
        super(itemSpeed, 1, name, texture);

        this.textureHand = new Texture("gui/items/" + texture + "_hand.png");
        spriteHand = new Sprite(textureHand);
        harvestableTiles = new ArrayList<ParentTile>();
    }
}
