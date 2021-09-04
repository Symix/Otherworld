package me.symix.otherworld.entities.misc;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import me.symix.otherworld.misc.AllSounds;
import me.symix.otherworld.misc.AllTexture;
import me.symix.otherworld.misc.Utils;
import me.symix.otherworld.tiles.Tile;
import me.symix.otherworld.world.World;

import java.awt.*;

public class GrapplingHookEntity {

    public float x, y, tX, tY, xVel, yVel;
    public float gravity, maxFallspeed, speed;

    private int WIDTH, HEIGHT;
    private World world;
    public boolean isDead = false;

    public Rectangle rect;

    private Sprite sprite;
    public boolean isStuck;

    public GrapplingHookEntity(int x, int y, int tX, int tY, World world){
        this.x = x;
        this.y = y;
        this.tX = tX;
        this.tY = tY;
        this.world = world;

        sprite = new Sprite(AllTexture.grappling_hook);

        WIDTH = AllTexture.grappling_hook.getWidth();
        HEIGHT = AllTexture.grappling_hook.getHeight();

        gravity = 0f;
        maxFallspeed = -10;
        speed = 15;

        double angle = Math.atan2(tY - y, tX - x);
        xVel = (float)(speed * Math.cos(angle));
        yVel = (float)(speed * Math.sin(angle));

        sprite.setRotation(getAngle(xVel, yVel));

        rect = new Rectangle(x, y, WIDTH, HEIGHT);

        isStuck = false;
    }

    public void tick(){
        Tile t = world.getTile(getX(), getY());

        isStuck = (t != null && t.solid);

        if(yVel > maxFallspeed){
            yVel -= gravity;
        }

        if(!isStuck) {
            x += xVel;
            y += yVel;
        }

        rect.setPosition(getX(), getY());
    }

    public int getX(){
        return (int)x;
    }

    public int getY(){
        return (int)y;
    }

    public void render(SpriteBatch batch){
        world.applyLight(getX(), getY(), batch);

        sprite.setPosition(x, y);
        sprite.draw(batch);
    }

    public static float getAngle(float vx, float vy) {
        return (float)Math.toDegrees(Math.atan2(vy, vx));
    }
}
