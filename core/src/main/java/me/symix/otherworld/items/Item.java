package me.symix.otherworld.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Item {

    public Texture texture, textureEntity;
    public int itemSpeed, maxStackSize;
    public Sprite sprite;
    public String name;

    public Item(int itemSpeed, int maxStackSize, String name, String texture){
        this.itemSpeed = itemSpeed;
        this.maxStackSize = maxStackSize;
        this.name = name;

        this.texture = new Texture("gui/items/" + texture + ".png");
        sprite = new Sprite(this.texture);
        try {
            this.textureEntity = new Texture("gui/items/" + texture + "_entity.png");
        }catch(GdxRuntimeException e){
            this.textureEntity = this.texture;
            System.out.println("Could not load gui/items/" + texture + "_entity.png, overriding entity texture.");
        }
    }
}
