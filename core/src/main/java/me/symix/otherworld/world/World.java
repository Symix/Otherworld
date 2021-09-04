package me.symix.otherworld.world;

import box2dLight.DirectionalLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import me.symix.otherworld.AllBiomes;
import me.symix.otherworld.Biome;
import me.symix.otherworld.Game;
import me.symix.otherworld.entities.*;
import me.symix.otherworld.entities.misc.Arrow;
import me.symix.otherworld.entities.misc.Cloud;
import me.symix.otherworld.entities.misc.GrapplingHookEntity;
import me.symix.otherworld.entities.player.ItemEntity;
import me.symix.otherworld.entities.player.Player;
import me.symix.otherworld.misc.AllSounds;
import me.symix.otherworld.misc.AllTexture;
import me.symix.otherworld.misc.Utils;
import me.symix.otherworld.tiles.AllTiles;
import me.symix.otherworld.tiles.Tile;

import com.badlogic.gdx.math.Rectangle;
import me.symix.otherworld.tiles.Wall;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;


public class World {

    public int WIDTH = 1000;
    public int HEIGHT = 1200;

    public int OCEANLEVEL = 800;

    public static int TILESIZE = 16;

    private ArrayList<Cloud> clouds;
    private ArrayList<Entity> entities;
    public ArrayList<ItemEntity> items;

    public Game game;

    public Grid grid;

    ArrayList<Point> kaadettavat = new ArrayList<Point>();

    public RayHandler rayHandler;
    com.badlogic.gdx.physics.box2d.World boxWorld;

    public Player player;

    public OrthographicCamera cam;
    public ExtendViewport viewport;

    private int forestTimer = 0;

    private DirectionalLight light;

    private int kaatoTimer = 0;

    public boolean calculatingLights = false;
    public boolean generated = false;
    private boolean shouldUpdateView = false;

    public Spawner spawner;

    public ShapeRenderer sr = new ShapeRenderer();

    public World(Game game){
        this.game = game;

        cam = new OrthographicCamera();
        //viewport = new ExtendViewport((WIDTH * TILESIZE) / 4, (OCEANLEVEL + 20) * TILESIZE, cam);
        viewport = new ExtendViewport(Game.WIDTH, Game.HEIGHT, cam);

        cam.position.set(0, 0, 0);
        cam.update();

        clouds = new ArrayList<Cloud>();
        entities = new ArrayList<Entity>();
        items = new ArrayList<ItemEntity>();

        boxWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0,0),false);

        grid = new Grid(this);

        rayHandler = new RayHandler(boxWorld);
        rayHandler.setShadows(true);
        rayHandler.setAmbientLight(0f, 0f, 0f, 0.3f);

        this.spawner = new Spawner(this);
    }

    private int timer = 0;

    public void spawnEntity(Entity e){
        entities.add(e);
    }

    public void tick(){
        if(shouldUpdateView){
            //viewport.update(GAME.WIDTH, GAME.HEIGHT);
            //viewport.setMinWorldWidth(GAME.WIDTH);
           // viewport.setMinWorldHeight(GAME.HEIGHT);
            //viewport.setMaxWorldWidth(GAME.WIDTH);
            //viewport.setMaxWorldHeight(GAME.HEIGHT);
            //viewport.setScreenSize(GAME.WIDTH, GAME.HEIGHT);
            //cam = new OrthographicCamera();
            //viewport = new ExtendViewport(GAME.WIDTH, GAME.HEIGHT, cam);
            shouldUpdateView = false;
        }
        if(!kaadettavat.isEmpty()){
            Point p = kaadettavat.get(0);
            grid.setTile(p.x, p.y, null, true, false);
            AllSounds.MineWood.play(1);
            kaadettavat.remove(0);
        }
        if(player != null){
            player.tick();
        }

        for(Cloud c : clouds){
            c.tick();
        }
        for(Entity e : entities){
            e.preTick();
        }
        timer++;
        if(timer == 60) {
            if (Game.rand.nextInt(3) == 0) {
                clouds.add(new Cloud(player.getX() + 1200, (OCEANLEVEL + 20 + Game.rand.nextInt(15)) * Game.TILESIZE));
            }
            int dirX = Game.rand.nextBoolean() ? 1 : -1;
            int xDir = dirX == 1 ? -1 : 1;
            boolean spawnBunny = false;
            if(entities.size() <= 1){
                spawnBunny = true;
            } else if(entities.size() < 5){
                if(Game.rand.nextInt(5) == 0){
                    spawnBunny = true;
                }
            } else if(entities.size() < 15){
                if(Game.rand.nextInt(15) == 0){
                    spawnBunny = true;
                }
            }
            if(spawnBunny){
                spawner.spawnBunny(player.getX() + (1200 * dirX), getHighest(player.getX() + (1200 * dirX)) + 20, xDir);
            }
            timer = 0;
        }

        for(Entity e : entities){
            if(e instanceof EntityLiving) {
                for(GrapplingHookEntity hook : player.grapplingHooks) {
                    if (!hook.isStuck && e.rect.overlaps(hook.rect)) {
                        EntityLiving eL = (EntityLiving) e;
                        eL.takeDamage(10);
                    }
                }
                for (Arrow a : player.arrows) {
                    if(!a.isDead) {
                        if (e.rect.overlaps(a.rect)) {
                            EntityLiving eL = (EntityLiving) e;
                            eL.takeDamage(a.damage);
                            a.isDead = true;
                        }
                    }
                }
                if(e.rect.overlaps(player.rect)){
                    player.takeDamage(((EntityLiving) e).damage, (EntityLiving)e);
                }
            }
        }

        ArrayList<Entity> deletables = new ArrayList<Entity>();

        for(Entity e : entities){
            if(e.isDead){
                deletables.add(e);
            }
        }

        for(Entity e : deletables){
            if(e instanceof EntityLiving) {
                AllSounds.Splat.play(Utils.getVolume(e.getPos(), player.getPos(), 1200));
                for (int i = 1; i < 5; i++) {
                    //entities.add(new EntityChunk(e.getX(), e.getY(), i, e.texture, this));
                }
            }
            entities.remove(e);
        }

        ArrayList<ItemEntity> deletables2 = new ArrayList<ItemEntity>();

        for(ItemEntity eI : items){
            Rectangle rect = eI.rect;
            rect = new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            rect.setPosition(rect.x - 8, rect.y - 8);
            rect.setSize(rect.getWidth() + 16, rect.getHeight() + 16);
            if (rect.overlaps(player.rect)) {
                if(player.giveItem(eI.itemEntry)){
                    AllSounds.Grab.play(0.75f);
                    deletables2.add(eI);
                }
            }
        }

        for(ItemEntity eI : deletables2){
            items.remove(eI);
        }
    }

    public int getHighest(int x){
        int highest = 0;
        for(int y = 0; y < HEIGHT; y++){
            Tile t = grid.getTile(x / Game.TILESIZE, y);
            if(t != null){
                highest = y;
            }
        }
        return highest * Game.TILESIZE;
    }

    public void render(SpriteBatch batch){
        if(light == null){
            //light = new DirectionalLight(rayHandler, 2000, new Color(1, 1, 1, 0.75f), -90);
        }

        float conv = 100;

        //cam.setToOrtho(false);
        //rayHandler.setCombinedMatrix(cam.combined.scale(conv, conv, conv), cam.position.x * conv, cam.position.y * conv, cam.viewportWidth * conv, cam.viewportHeight * conv);
        //cam.combined.scale(1f / conv, 1f / conv, 1f / conv);

        if(generated) {
            cam.position.set(player.getX(), player.getY(), 0);
        } else {
            cam.position.set(player.getX(), (OCEANLEVEL - 50) * TILESIZE, 0);
        }
        cam.update();

        batch.setProjectionMatrix(cam.combined);

        Color col = player.getBiome().skyColor;
        Gdx.gl.glClearColor(col.r, col.g, col.b, col.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.setColor(Color.WHITE);

        Texture[] bg = player.getBiome().background;
        int bg0 = 0;
        int bg1 = 0;
        int yOffset1 = player.getBiome().bgOff1;
        int yOffset2 = player.getBiome().bgOff2;
        if(player.getBiome() == AllBiomes.forest) {
            forestTimer++;
            if (forestTimer == 120) {
                forestTimer = 0;
            }
            bg0 = forestTimer > 60 ? 0 : 1;
            bg1 = 2;
        } else {
            bg0 = 0;
            bg1 = 1;
        }

        batch.draw(bg[bg0],player.getX() - (game.WIDTH / 2) - 50, (OCEANLEVEL * TILESIZE) + yOffset1, (int)(player.getX() / 1.5f), player.getY() / 2, bg[bg0].getWidth() * 2, bg[bg0].getHeight());
        batch.draw(bg[bg1],player.getX() - (game.WIDTH / 2) - 50, (OCEANLEVEL * TILESIZE) + yOffset2, (int)(player.getX() / 1.25f), player.getY() / 2, bg[bg1].getWidth() * 2, bg[bg1].getHeight());

        //
        //rayHandler.setCombinedMatrix(cam);

        int areaX = 75;
        int areaY = 40;
        int startX = (player.getX() / Game.TILESIZE) - areaX;
        int startY = (player.getY() / Game.TILESIZE) - areaY;
        int longestX = (player.getX() / Game.TILESIZE) + areaX;
        int longestY = (player.getY() / Game.TILESIZE) + areaY;

        if(startX < 0) startX = 0;
        if(startY < 0) startY = 0;
        if(longestX > WIDTH) longestX = WIDTH;
        if(longestY > HEIGHT) longestY = HEIGHT;

        HashMap<Biome, Integer> biomes = new HashMap<Biome, Integer>();

        if(!generated){
            startX = 0;
            startY = 0;
            longestX = WIDTH;
            longestY = HEIGHT;
        }
        for(int x = longestX - 1; x >= startX; x--) {
            for (int y = longestY - 1; y >= startY; y--) {
                //if (x > (player.getX() / GAME.TILESIZE) - areaX && x < (player.getX() / GAME.TILESIZE) + areaX && y > (player.getY() / GAME.TILESIZE) - areaY && y < (player.getY() / GAME.TILESIZE) + areaY) {
                    grid.applyLight(x, y, batch);
                    Tile t = grid.getTile(x, y);
                    Wall w = grid.getWall(x, y);
                    if(t != null) {
                        for(Biome b : AllBiomes.biomes) {
                            if(b.layer1Tile == t || b.layer2Tile == t || b.layer3Tile == t) {
                                if (!biomes.containsKey(b)) {
                                    biomes.put(b, 1);
                                } else {
                                    int amountTemp = biomes.get(b);
                                    biomes.remove(b);
                                    biomes.put(b, amountTemp + 1);
                                }
                            }
                        }
                    }
                    if (t == null || t.opaque) {
                        if (w != null) {
                            renderWall(x, y, w, batch);
                        }
                    }
                    if (t != null) {
                        int offsetX = 0;
                        int offsetY = 0;

                        if (t == AllTiles.TileTreeHead[0] || t == AllTiles.TileTreeHead[1]) {
                            offsetY = 0;
                            offsetX = -23;
                        }
                        if (t.isConnect()) {
                            Point p = grid.srcs[x][y];
                            int srcX, srcY;
                            if(p != null) {
                                srcX = p.x;
                                srcY = p.y;
                            } else {
                                srcX = 16;
                                srcY = 16;
                                grid.calcSrcs();
                            }

                            if (srcX != 16 || srcY != 16) {
                                if (w != null) {
                                    renderWall(x, y, w, batch);
                                }
                            }

                            batch.draw(t.getTexture(), (x * Game.TILESIZE) + offsetX, (y * Game.TILESIZE) + offsetY, Game.TILESIZE, Game.TILESIZE, srcX, srcY, Game.TILESIZE, Game.TILESIZE, false, false);
                        } else {
                            if (t.parent == null) {
                                batch.draw(t.getTexture(), (x * Game.TILESIZE) + offsetX, (y * Game.TILESIZE) + offsetY);
                            }
                        }
                        int damage = grid.getDamage(x, y);
                        if (damage > 0) {
                            int progress = (int) Math.ceil(damage / (100f / AllTexture.breakTex.length)) - 1;
                            if (progress > 3) {
                                progress = 3;
                            }
                            batch.draw(AllTexture.breakTex[progress], x * Game.TILESIZE, y * Game.TILESIZE);
                        }
                    }
                //}
            }
        }
        Biome bestBiome = null;
        int bestAmount = 0;
        for(Biome b : biomes.keySet()){
            if(biomes.get(b) > bestAmount){
                bestBiome = b;
                bestAmount = biomes.get(b);
            }
        }
        if(bestBiome != null) {
            player.setBiome(bestBiome);
        }

        for(Cloud c : clouds){
            c.render(batch);
        }
        for(Entity e : entities){
            e.render(batch);
        }
        for(ItemEntity e : items){
            e.render(batch);
        }

        player.render(batch);

        //font.draw(batch, "FPS: " + game.frameRate, cam.position.x - (cam.viewportWidth / 2), cam.position.y - (cam.viewportHeight / 2) + 15);
        //rayHandler.updateAndRender();
        batch.end();
    }

    private void renderWall(int x, int y, Wall w, SpriteBatch batch){
        if(w.isConnect()){
            Point p = grid.getSrc(x, y, w);
            int srcX = p.x;
            int srcY = p.y;

            batch.draw(w.getTexture(), x * Game.TILESIZE, y * Game.TILESIZE, Game.TILESIZE, Game.TILESIZE, srcX, srcY, Game.TILESIZE, Game.TILESIZE, false, false);
        } else {
            batch.draw(w.getTexture(), x * Game.TILESIZE, y * Game.TILESIZE);
        }
    }

    public boolean place_meeting(double x2, double y2, Rectangle rect){
        return place_meetingPoint(x2, y2, rect) != null;
    }

    public Point place_meetingPoint(double x2, double y2, Rectangle rect){
        Rectangle newRect = new Rectangle((int)x2, (int)y2, rect.width, rect.height);
        int x = (int)x2;
        int y = (int)y2;
        int tileX = x / Game.TILESIZE;
        int tileY = y / Game.TILESIZE;

        Rectangle rect2 = new Rectangle(0, 0, Game.TILESIZE, Game.TILESIZE);
        for(int xx = -3; xx < 3; xx++) {
            for(int yy = -3; yy < 3; yy++) {
                Tile t = grid.getTile(tileX + xx, tileY + yy);

                if (t == null) {
                    continue;
                }
                if (t.solid || t.isPlatform) {
                    rect2.setPosition((tileX + xx) * Game.TILESIZE, (tileY + yy) * Game.TILESIZE);
                    if(rect2.overlaps(newRect)) {
                        return new Point(tileX + xx, tileY + yy);
                    }
                }
            }
        }
        return null;
    }

    public boolean setTile(int x, int y, Tile t){
        return grid.setTile(x / Game.TILESIZE, y / Game.TILESIZE, t, true, true);
    }

    public Tile getTile(int x, int y){
        return grid.getTile(x / Game.TILESIZE, y / Game.TILESIZE);
    }

    public boolean setWall(int x, int y, Wall w, boolean dropItem, boolean recalc){
        return grid.setWall(x / Game.TILESIZE, y / Game.TILESIZE, w, dropItem, recalc);
    }

    public Wall getWall(int x, int y){
        return grid.getWall(x / Game.TILESIZE, y / Game.TILESIZE);
    }

    public Color getLight(int x, int y){
        return grid.lightHandler.getLightColor(x / Game.TILESIZE, y / Game.TILESIZE);
    }

    public void applyLight(int x, int y, SpriteBatch batch){
        grid.applyLight(x / Game.TILESIZE, y / Game.TILESIZE, batch);
    }

    public Vector3 getMPos(){
        return cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    public void finishGene(){
        shouldUpdateView = true;
        generated = true;
    }
}
