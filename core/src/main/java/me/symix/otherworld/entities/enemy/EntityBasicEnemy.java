package me.symix.otherworld.entities.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import me.symix.otherworld.Game;
import me.symix.otherworld.Keys;
import me.symix.otherworld.Vars;
import me.symix.otherworld.entities.EntityLiving;
import me.symix.otherworld.entities.misc.GrapplingHookEntity;
import me.symix.otherworld.misc.EntityTexture;
import me.symix.otherworld.tiles.Tile;
import me.symix.otherworld.world.World;

import java.awt.*;

public class EntityBasicEnemy extends EntityLiving {

    public float speed, maxSpeed, friction, jumpSpeed, gravity, maxFallspeed;

    private boolean jump = false;
    private int animationTimer = 0;

    public EntityBasicEnemy(int x, int y, int damage, int armor, int hp, EntityTexture texture, World world){
        super(x, y, damage, armor, hp, texture, world);

        gravity = 0.22f;
        maxFallspeed = -5; //-5;
        speed = 0.5f;
        maxSpeed = 1.5f;//4.5f
        friction = 0.5f;
        jumpSpeed = 6.5f;
    }

    protected void tick(){
        move();
        handleCollisions();

        handleAnimation();

        x += xVel;
        y += yVel;
    }

    private void handleAnimation(){
        animationTimer++;
        if(animationTimer > 90){
            animationTimer = 0;
        }
        if(Math.abs(xVel) > 0){
            currentImage = animationTimer >= 10 ? 0 : 1;

            if(animationTimer >= 20){
                animationTimer = 0;
            }
        }

        if(!onGround()) {
            currentImage = 2;
        }
    }
    private void move(){
        int moveX = 0;
        if(world.player.getX() < getX()){
            moveX -= 1;
        }
        if(world.player.getX() > getX()){
            moveX += 1;
        }
        if((Math.abs(xVel) < maxSpeed) || moveX != Math.signum(xVel)) {
            xVel += moveX * speed;
        }

        if(flipX){
            if(moveX == 1){
                flipX = false;
            }
        } else {
            if(moveX == -1){
                flipX = true;
            }
        }
    }

    private void handleCollisions(){
        if(onGround()){
            yVel = 0;

            if(jump){
                yVel += jumpSpeed;
                jump = false;
            }
        } else {
            if(yVel > maxFallspeed && !Vars.fly){
                yVel -= gravity;
            }
        }

        if(world.place_meeting(x + xVel, y, rect)){
            Point p = world.place_meetingPoint(x + xVel, y, rect);
            if(p != null){
                Tile t = world.grid.getTile(p.x, p.y);
                if(t != null && !t.isPlatform) {
                    boolean emptyUp = (world.grid.getTile(p.x, p.y + 1) == null || !world.grid.getTile(p.x, p.y + 1).solid);
                    emptyUp = (world.grid.getTile(p.x, p.y + 2) == null || !world.grid.getTile(p.x, p.y + 2).solid) && emptyUp;
                    emptyUp = (world.grid.getTile(p.x, p.y + 3) == null || !world.grid.getTile(p.x, p.y + 3).solid) && emptyUp;

                    if (emptyUp) {
                        y += Game.TILESIZE;
                    } else {
                        while (!world.place_meeting(x + Math.signum(xVel), y, rect)) {
                            x += Math.signum(xVel);
                        }
                        xVel = 0;
                        jump = true;
                    }
                }
            }
        }

        if(world.place_meeting(x, y + yVel, rect)){
            Point p = world.place_meetingPoint(x, y + yVel, rect);
            if(p != null) {
                Tile t = world.grid.getTile(p.x, p.y);
                if (t != null && (!t.isPlatform || (yVel < 0 && !Gdx.input.isKeyPressed(Keys.down)))) {
                    while (!world.place_meeting(x, y + Math.signum(yVel), rect)) {
                        y += Math.signum(yVel);
                    }
                    yVel = 0;
                }
            }
        }
    }

    boolean onGround(){
        Point p = world.place_meetingPoint(x, y - 1, rect);
        if(p != null){
            Tile t = world.grid.getTile(p.x, p.y);
            if(t.solid){
                return true;
            }
            if(t.isPlatform){
                //if(Gdx.input.isKeyPressed(Keys.down)){
                    return false;
                //} else {
                    //return true;
                //}
            }
        }
        return false;
    }
}
