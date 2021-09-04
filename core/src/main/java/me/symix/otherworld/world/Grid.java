package me.symix.otherworld.world;

import box2dLight.Light;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import me.symix.otherworld.Game;
import me.symix.otherworld.TileTier;
import me.symix.otherworld.entities.player.ItemEntity;
import me.symix.otherworld.items.tools.ItemPickaxe;
import me.symix.otherworld.misc.AllSounds;
import me.symix.otherworld.misc.Utils;
import me.symix.otherworld.tiles.*;

import java.awt.*;

import com.badlogic.gdx.graphics.Color;


public class Grid {

    public World world;

    private Tile[][] tiles;
    private Wall[][] walls;
    private int[][] damage;
    public Point[][] srcs;
    public Body[][] bodies;
    public Light[][] lights;

    public int WIDTH;
    public int HEIGHT;

    public LightHandler lightHandler;

    private BodyDef tileDef;
    private FixtureDef tileFixDef;
    private PolygonShape polygonShape;

    public Grid(World world){
        this.world = world;

        WIDTH = world.WIDTH;
        HEIGHT = world.HEIGHT;
        lightHandler = new LightHandler(this);

        tiles = new Tile[WIDTH][HEIGHT];
        walls = new Wall[WIDTH][HEIGHT];
        damage = new int[WIDTH][HEIGHT];
        srcs = new Point[WIDTH][HEIGHT];
        bodies = new Body[WIDTH][HEIGHT];
        lights = new Light[WIDTH][HEIGHT];

        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y < HEIGHT; y++){
                damage[x][y] = 0;
            }
        }

        tileDef = new BodyDef();
        tileDef.type = BodyDef.BodyType.StaticBody;

        polygonShape = new PolygonShape();
        int size = 16;
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(0, 0);
        vertices[1] = new Vector2(size, 0);
        vertices[2] = new Vector2(0, size);
        vertices[3] = new Vector2(size, size);

        polygonShape.set(vertices);

        tileFixDef = new FixtureDef();
        tileFixDef.shape = polygonShape;
    }

    public void addDamage(int x, int y, ItemPickaxe pickaxe){
        Tile t = getTile(x, y);
        int power = pickaxe.power;
        if(t != null) {
            int damageIncrease = 0;
            if(t.tier == TileTier.INSTA){
                damageIncrease = 100;
            } else if(t.tier == TileTier.TIER1){
                damageIncrease = power * 2;
            } else if(t.tier == TileTier.TIER2){
                damageIncrease = power / 2;
            } else {
                damageIncrease = pickaxe.power;
            }

            damage[x][y] += damageIncrease;
            if(damage[x][y] >= 100){
                setTile(x, y, null, true, true);
                damage[x][y] = 0;
            }
        }
    }

    public int getDamage(int x, int y){
        return damage[x][y];
    }

    public boolean setTile(int x, int y, Tile t, boolean dropItem, boolean calcLight){
        boolean did = false;

        if(isInside(x, y)) {
            if(t == null) {
                boolean dropped = false;
                if(tiles[x][y] != null && tiles[x][y].dropItem != null && dropItem) {
                    world.items.add(new ItemEntity(x * Game.TILESIZE, y * Game.TILESIZE, tiles[x][y].dropItem, 1, world));
                    dropped = true;
                }
                if(tiles[x][y] != null && (tiles[x][y].parent != null || tiles[x][y].multiTile)) {
                    //Tile child = tiles[x][y];
                    Tile parent;
                    int parentX;
                    int parentY;

                    if (tiles[x][y].parent != null){
                        parent = tiles[x][y].parent;
                        parentX = tiles[x][y].parentX;
                        parentY = tiles[x][y].parentY;
                    } else {
                        parent = tiles[x][y];
                        parentX = x;
                        parentY = y;
                    }
                    if(parent.dropItem != null && dropItem && !dropped){
                        world.items.add(new ItemEntity(parentX * Game.TILESIZE, parentY * Game.TILESIZE, parent.dropItem, 1, world));
                    }
                    for(int xx = 0; xx < parent.multiWidth; xx++){
                        for(int yy = 0; yy < parent.multiHeight; yy++){
                            tiles[parentX + xx][parentY + yy] = null;
                        }
                    }
                }
                for(int i = 0; i < AllTiles.TileTreeTrunk.length; i++) {
                    if(tiles[x][y] == AllTiles.TileTreeStump[i][1]){
                        if(tiles[x - 1][y] == AllTiles.TileTreeStump[i][0]){
                            setTile(x - 1, y, null, true, true);
                        }
                        if(tiles[x + 1][y] == AllTiles.TileTreeStump[i][2]){
                            setTile(x + 1, y, null, true, true);
                        }
                    }
                    if (tiles[x][y] == AllTiles.TileTreeStump[i][1] || tiles[x][y] == AllTiles.TileTreeTrunk[i] || tiles[x][y] == AllTiles.TileCactusStump || tiles[x][y] == AllTiles.TileCactusTrunk) {
                        if (calcLight) {
                            int offY = 0;
                            while (tiles[x][y + offY] != null) {
                            /*if(tiles[x][y + offY].dropItem != null && dropItem) {
                                world.items.add(new ItemEntity(x * GAME.TILESIZE, (y + offY) * GAME.TILESIZE, tiles[x][y + offY].dropItem, 1, world));
                            }
                            tiles[x][y + offY] = null;*/
                                world.kaadettavat.add(new Point(x, y + offY));
                                offY++;
                            }
                        }
                        break;
                    }
                }
                if(tiles[x][y] != null) {
                    if (tiles[x][y].lightLevel > 0) {
                        //lightHandler.recalcLight(x, y, true);
                        did = true;
                    }
                }
            } else if(t.multiTile){
                for(int xx = 0; xx < t.multiWidth; xx++){
                    for(int yy = 0; yy < t.multiHeight; yy++){
                       if(xx != 0 || yy != 0) {
                            tiles[x + xx][y + yy] = new TileMulti(x, y, t);
                       }
                    }
                }
            }

            tiles[x][y] = t;
            did = true;
        }
        if(did) {
            if(t != null) {
                if(t.lightLevel > 0){
                    lightHandler.setBright(x, y, true);
                    lights[x][y] = new PointLight(world.rayHandler,512, new Color(0.5f, 0.5f, 0.5f, 1f),350, (x * World.TILESIZE) + 8, (y * World.TILESIZE) + 8);
                    lights[x][y].setSoft(true);
                }
                if(t.solid) {
                    //tileDef.position.set(x * GAME.TILESIZE, y * GAME.TILESIZE);
                    //bodies[x][y] = world.boxWorld.createBody(tileDef);
                    //bodies[x][y].createFixture(tileFixDef);
                    //System.out.println("BODY CREATED FAGGOT");
                }
            } else {
                if(lights[x][y] != null){
                    lights[x][y].remove(true);
                }
                if(bodies[x][y] != null){
                    world.boxWorld.destroyBody(bodies[x][y]);
                }
            }
            if(calcLight){
                recalcSrc(x, y);
                lightHandler.recalcLight(x, y, true);
            }
        }
        return did;
    }

    public void applyLight(int x, int y, SpriteBatch batch){
        if(!world.calculatingLights && !world.generated) {
            batch.setColor(Color.WHITE);
        } else {
            lightHandler.applyLight(x, y, batch);
        }
    }

    public boolean door(Tile t, int x, int y){
        if(t.parent != null){
            x = t.parentX;
            y = t.parentY;
            t = t.parent;
        }
        if(t == AllTiles.TileDoorClosed) {
            setTile(x, y, null, false, true);
            boolean closed = setTile(x, y, AllTiles.TileDoorOpen, false, true);
            if(closed){
                AllSounds.DoorClose.play(1);
            }
            return closed;
        } else if(t == AllTiles.TileDoorOpen){
            setTile(x, y, null, false, true);
            boolean opened = setTile(x, y, AllTiles.TileDoorClosed, false, true);
            if(opened){
                AllSounds.DoorOpen.play(1);
            }
            return opened;
        }
        return false;
    }

    public boolean setWall(int x, int y, Wall w, boolean dropItem, boolean reCalcLight){
        if(isInside(x, y)) {
            if(w == null && walls[x][y] != null) {
                if(walls[x][y].dropItem != null && dropItem) {
                    world.items.add(new ItemEntity(x * Game.TILESIZE, y * Game.TILESIZE, walls[x][y].dropItem, 1, world));
                }
            }
            walls[x][y] = w;
            if(w == null && reCalcLight){
                lightHandler.setBright(x, y, true);
            } else {
                if(reCalcLight) {
                    lightHandler.recalcLight(x, y, true);
                }
            }
            return true;
        }
        return false;
    }

    public Tile getTile(int x, int y){
        if(x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            return tiles[x][y];
        }
        return null;
    }

    public Wall getWall(int x, int y){
        if(x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            return walls[x][y];
        }
        return null;
    }

    public boolean isInside(int x, int y){
        return Utils.isInside(x, y, WIDTH, HEIGHT);
    }

    public Point getSrc(int x, int y, ParentTile pt){
        int srcX = 0;
        int srcY = 0;

        Tile t = null;
        Wall w = null;
        if(pt instanceof Tile){
            t = (Tile)pt;
        } else if(pt instanceof Wall){
            w = (Wall)pt;
        }

        if(x > 0 && y > 0 && x < WIDTH - 1 && y < HEIGHT - 1) {
            ParentTile left = null, right = null, bot = null, up = null;

            if (pt instanceof Tile) {
                left = getTile(x - 1, y);
                right = getTile(x + 1, y);
                bot = getTile(x, y - 1);
                up = getTile(x, y + 1);
            } else if (pt instanceof Wall) {
                left = getWall(x - 1, y);
                right = getWall(x + 1, y);
                bot = getWall(x, y - 1);
                up = getWall(x, y + 1);
            }
            boolean onLeft = pt.doesConnectTile(left);
            boolean onRight = pt.doesConnectTile(right);
            boolean onBot = pt.doesConnectTile(bot);
            boolean onUp = pt.doesConnectTile(up);

            if (onLeft && onRight && onBot && onUp) {
                //Kiinteä
                srcX = 16;
                srcY = 16;
            } else if (onLeft && onRight && !onUp && onBot) {
                //Keski ylä
                srcX = 16;
                srcY = 0;
            } else if (onLeft && onRight && onUp && !onBot) {
                //Keski ala
                srcX = 16;
                srcY = 32;
            } else if (!onLeft && onRight && onUp && onBot) {
                //Keski vasen
                srcX = 0;
                srcY = 16;
            } else if (onLeft && !onRight && onUp && onBot) {
                //Keski oikea
                srcX = 32;
                srcY = 16;
            } else if (!onLeft && !onRight && !onUp && !onBot) {
                //Yksittäinen
                srcX = 48;
                srcY = 0;
            } else if (!onLeft && onRight && !onUp && onBot) {
                //Vasen ylä kulma
                srcX = 0;
                srcY = 0;
            } else if (!onLeft && onRight && onUp && !onBot) {
                //Vasen ala kulma
                srcX = 0;
                srcY = 32;
            } else if (onLeft && !onRight && !onUp && onBot) {
                //Oikee ylä kulma
                srcX = 32;
                srcY = 0;
            } else if (onLeft && !onRight && onUp && !onBot) {
                //Oikee ala kulma
                srcX = 32;
                srcY = 32;
            } else if(!onLeft && !onRight && !onUp && onBot) {
                //Yksittäinen ylä
                srcX = 0;
                srcY = 48;
            } else if(onLeft && !onRight && !onUp && !onBot) {
                //Yksittäinen oikea
                srcX = 16;
                srcY = 48;
            }else if(!onLeft && !onRight && onUp && !onBot){
                //Yksittäinen ala
                srcX = 32;
                srcY = 48;
            } else if(!onLeft && onRight && !onUp && !onBot) {
                //Yksittäinen vasen
                srcX = 48;
                srcY = 48;
            } else if(!onLeft && !onRight && onUp && onBot) {
                //Putki ylös
                srcX = 48;
                srcY = 16;
            } else if(onLeft && onRight && !onUp && !onBot) {
                //Putki sivuun
                srcX = 48;
                srcY = 32;
            } else {
                srcX = 48;
                srcY = 16;
            }
        } else {
            srcX = 16;
            srcY = 16;
        }

        return new Point(srcX, srcY);
    }

    public void calcSrcs(){
        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y < WIDTH; y++){
                Tile t = getTile(x, y);

                if(t != null) {
                    if (t.isConnect()) {
                        Point p = getSrc(x, y, t);

                        srcs[x][y] = p;
                    }
                }
            }
        }
    }

    public void recalcSrc(int x, int y){
        for(int xx = -1; xx <= 1; xx++){
            for(int yy = -1; yy <= 1; yy++){
                Tile t = getTile(x + xx, y + yy);

                if(t != null) {
                    if (t.isConnect()) {
                        Point p = getSrc(x + xx, y + yy, t);

                        srcs[x + xx][y + yy] = p;
                    }
                }
            }
        }
    }

    public void calcLighting(){
        lightHandler.calcLighting();
    }

    public void setBright(int x, int y, boolean ambLight){
        lightHandler.setBright(x, y, ambLight);
    }

    public void recalcLight(int x, int y, boolean ambLight){
        lightHandler.recalcLight(x, y, ambLight);
    }
}
