package me.symix.otherworld.entities.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import me.symix.otherworld.Game;
import me.symix.otherworld.ItemEntry;
import me.symix.otherworld.items.Item;
import me.symix.otherworld.world.World;

import java.awt.*;

public class ItemEntity {

    public float x, y, xVel, yVel;
    public float gravity, maxFallspeed, friction;

    public ItemEntry itemEntry;

    public World world;

    private int WIDTH = 8, HEIGHT = 8;
    public Rectangle rect;

    private int timer = 0;
    private int yOffset = 0;
    private int timerDir = 2;

    public ItemEntity(int x, int y, Item item, int amount, World world){
        this.x = x;
        this.y = y;
        itemEntry = new ItemEntry(item, amount);

        this.world = world;

        rect = new Rectangle(x + 4, y + 4, WIDTH, HEIGHT);

        gravity = 0.22f;
        maxFallspeed = -5;
        friction = 0.25f;

        xVel = Game.rand.nextInt(2) * (Game.rand.nextBoolean() ? 1 : -1);
        yVel = Game.rand.nextInt(2) + 1;

        this.y += yVel;
    }

    public void render(SpriteBatch batch){
        timer += timerDir;
        if(timer == 40 || timer == -40){
            timerDir *= -1;
        }
        yOffset = timer / 10;

        world.applyLight(getX(), getY(), batch);

        batch.draw(itemEntry.item.textureEntity, getX(), getY() + yOffset);

        rect.setPosition(getX() + 4, getY() + 4);

        if(!onGround()){
            if(yVel > maxFallspeed){
                yVel -= gravity;
            }
        } else {
            if(Math.abs(xVel) > 0){
                xVel -= Math.signum(xVel) * friction;
            }
        }
        if(world.place_meeting(x + xVel, y, rect)){
            while (!world.place_meeting(x + Math.signum(xVel), y, rect)) {
                x += Math.signum(xVel);
            }
            xVel = 0;
        }

        if(world.place_meeting(x, y + yVel, rect)){
            while(!world.place_meeting(x, y + Math.signum(yVel), rect)){
                y += Math.signum(yVel);
            }
            yVel = 0;
        }

        x += xVel;
        y += yVel;
    }

    public boolean onGround(){
        return (world.place_meeting(x, y - 1, rect));
    }

    public int getX(){
        return (int)x;
    }

    public int getY(){
        return (int)y;
    }
}
