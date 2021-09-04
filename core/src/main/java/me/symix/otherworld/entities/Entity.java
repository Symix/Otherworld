package me.symix.otherworld.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import me.symix.otherworld.misc.EntityTexture;
import me.symix.otherworld.world.World;

import java.awt.*;

public class Entity {

    public EntityTexture texture;
    public Rectangle rect;
    public boolean isDead = false;
    protected float x, y, xVel, yVel;
    protected World world;
    protected int currentImage = 0;
    protected boolean flipX = false;

    public Entity(int x, int y, EntityTexture texture, World world){
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.world = world;

        rect = new Rectangle(x, y, getTexture().getWidth(), getTexture().getHeight());
    }

    public Texture getTexture(){
        return texture.texture[currentImage];
    }

    public void preTick(){
        tick();

        rect.setPosition(getX(), getY());
    }

    protected void tick(){

    }

    public void render(SpriteBatch batch){
        world.applyLight(getX(), getY(), batch);

        batch.draw(getTexture(), getX(), getY(), getTexture().getWidth(), getTexture().getHeight(), 0, 0, getTexture().getWidth(), getTexture().getHeight(), flipX, false);
    }

    public int getX(){
        return (int)x;
    }

    public int getY(){
        return (int)y;
    }

    public Point getPos(){
        return new Point(getX(), getY());
    }
}
