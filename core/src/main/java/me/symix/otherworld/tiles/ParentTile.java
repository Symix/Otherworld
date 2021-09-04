package me.symix.otherworld.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import me.symix.otherworld.items.Item;

import java.util.ArrayList;

public class ParentTile {

    protected int id;
    protected Texture texture;
    private String textureString;

    private ArrayList<ParentTile> connectables;
    private boolean isConnect;

    public int lightLevel;
    public float[] lightColor = new float[4];
    public boolean lightFull = false;

    public Item dropItem = null;

    public boolean collide = true;
    public ParentTile(int id, String texture){
        this.id = id;
        this.textureString = texture;
        try {
            //this.texture = new Texture(texture);
        }catch(GdxRuntimeException e){
            if(id != -1)
                System.out.println("Could not load " + texture + " for id: " + id);
        }
        connectables = new ArrayList<ParentTile>();
        isConnect = false;
        lightLevel = 0;

        lightColor[0] = 1;
        lightColor[1] = 1;
        lightColor[2] = 1;
        lightColor[3] = 1;

    }

    public Texture getTexture(){
        if(texture == null){
            texture = new Texture(textureString);
        }
        return texture;
    }

    public boolean doesConnectTile(ParentTile t){
        if(t == null) return false;
        return connectables.contains(t);
    }

    public void addConnect(ParentTile t){
        connectables.add(t);
        isConnect = true;
    }

    public boolean isConnect(){
        return isConnect;
    }
}
