package me.symix.otherworld.misc;

import com.badlogic.gdx.graphics.Texture;

public class EntityTexture {

    public Texture[] texture;
    public Texture[] chunkTexture;

    public EntityTexture(String texture, int frames){
        this.texture = new Texture[frames];
        this.chunkTexture = new Texture[4];
        for(int i = 0; i < frames; i++) {
            this.texture[i] = new Texture(texture + "_" + i + ".png");
        }
        for(int i = 0; i < 4; i++) {
            this.chunkTexture[i] = new Texture(texture + "_chunk_" + i + ".png");
        }
    }
}
