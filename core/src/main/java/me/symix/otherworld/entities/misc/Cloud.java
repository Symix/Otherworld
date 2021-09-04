package me.symix.otherworld.entities.misc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.symix.otherworld.misc.AllTexture;
import me.symix.otherworld.Game;

public class Cloud {

    public float x, y, speed;
    public int type;

    public Cloud(int x, int y){
        this.x = x;
        this.y = y;
        speed = (Game.rand.nextFloat() + 0.2f) * (Game.rand.nextInt(2) + 1);
        type = Game.rand.nextInt(3);
    }

    public void tick(){
        x -= speed;
    }

    public void render(SpriteBatch batch){
        batch.setColor(Color.WHITE);
        batch.draw(AllTexture.cloud[type], x, y);
    }
}
