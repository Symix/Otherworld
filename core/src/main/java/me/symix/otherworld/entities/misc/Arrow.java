package me.symix.otherworld.entities.misc;

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


public class Arrow {

    public float x, y, tX, tY, xVel, yVel;
    public float gravity, maxFallspeed, speed;

    public World world;

    private Sprite arrowSprite;

    public boolean isDead = false;

    private int WIDTH, HEIGHT;

    public Rectangle rect;

    public int damage;

    public int timer = 0;

    public Arrow(int x, int y, int tX, int tY, int damage, World world){
        this.x = x;
        this.y = y;
        this.tX = tX;
        this.tY = tY;
        this.damage = damage;
        this.world = world;
        arrowSprite = new Sprite(AllTexture.arrow);

        WIDTH = AllTexture.arrow.getWidth();
        HEIGHT = AllTexture.arrow.getHeight();

        gravity = 0.05f;
        maxFallspeed = -10;
        speed = 7;

        double angle = Math.atan2(tY - y, tX - x);
        xVel = (float)(speed * Math.cos(angle));
        yVel = (float)(speed * Math.sin(angle));

        rect = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void tick(){
        Tile t = world.getTile(getX(), getY());

        if(t != null && t.solid){
            Point p = new Point(getX(), getY());

            AllSounds.ArrowCollide.play(Utils.getVolume(p, world.player.getPos(), 1200));
            isDead = true;
        }

        if(yVel > maxFallspeed){
            yVel -= gravity;
        }

        x += xVel;
        y += yVel;

        timer++;
        if(timer > 5 * 60){
            isDead = true;
            timer = -10000;
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
        batch.setColor(Color.WHITE);
        //batch.draw(AllTexture.arrow, x, y);

        arrowSprite.setRotation(getAngle(xVel, yVel));
        arrowSprite.setPosition(x, y);
        arrowSprite.draw(batch);
    }

    public static float getAngle(float vx, float vy) {
        return (float)Math.toDegrees(Math.atan2(vy, vx));
    }
}
