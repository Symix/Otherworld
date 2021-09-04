package me.symix.otherworld.entities.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import me.symix.otherworld.Game;
import me.symix.otherworld.misc.AllTexture;

import java.awt.*;

public class Scanner {

    private Game game;

    private float x, y, xVel, yVel;
    private int timer = 0;
    private int WIDTH = 24, HEIGHT = 36;

    public boolean isDead = false;

    public Rectangle rect;

    public Point collision;

    public Scanner(int x, int y, int tX, int tY, Game game){
        this.x = x;
        this.y = y;

        rect = new Rectangle(x, y, WIDTH, HEIGHT);

        this.game = game;

        double speed = 15.0;

        double angle = Math.atan2(tY - y, tX - x);
        xVel = (float)(speed * Math.cos(angle));
        yVel = (float)(speed * Math.sin(angle));
    }

    public void render(SpriteBatch batch){
        collision = game.world.place_meetingPoint(x, y, rect);
        timer++;
        if(timer > 10 || collision != null){
            isDead = true;
        }

        x += xVel;
        y += yVel;

        rect.setPosition(getX(), getY());

        //batch.setColor(Color.WHITE);
        //batch.draw(AllTexture.scanner, getX(), getY());
    }

    public int getX(){
        return (int)x;
    }

    public int getY(){
        return (int)y;
    }
}
