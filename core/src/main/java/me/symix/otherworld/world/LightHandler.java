package me.symix.otherworld.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.symix.otherworld.Vars;
import me.symix.otherworld.tiles.Tile;
import me.symix.otherworld.tiles.Wall;
import me.symix.otherworld.world.Grid;

public class LightHandler {

    public Grid grid;

    private int[][] ambLights;
    private int[][] dynLights;

    private int WIDTH, HEIGHT;

    public int curX = 0;

    public LightHandler(Grid grid){
        this.grid = grid;

        WIDTH = grid.WIDTH;
        HEIGHT = grid.HEIGHT;

        ambLights = new int[WIDTH][HEIGHT];
        dynLights = new int[WIDTH][HEIGHT];

        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y < HEIGHT; y++){
                ambLights[x][y] = 0;
                dynLights[x][y] = 0;
            }
        }
    }

    public void calcLighting(){
        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y < HEIGHT; y++){
                Wall w = grid.getWall(x, y);
                if(w != null) {
                    ambLights[x][y] = 0;
                }
            }
        }
        for(int x = 0; x < WIDTH; x++){
            curX = x;
            for(int y = 0; y < HEIGHT; y++){
                Wall w = grid.getWall(x, y);
                Tile t = grid.getTile(x, y);
                if(w == null && (t == null || !t.solid)) {
                    setBright(x, y, true);
                }

                if(t != null){
                    if(t.lightLevel > 0){
                        setLightLevel(x, y, t.lightLevel, true);
                        System.out.println("FOUND GAY TORCH");
                    }
                }
            }
        }
    }

    public void setBright(int x, int y, boolean ambLight){
        setLightLevel(x, y, Vars.lightDistance, ambLight);
    }

    public void setLightLevel(int x, int y, int lightLevel, boolean ambLight){
        for(int yy = -lightLevel; yy < lightLevel; yy++){
            for(int xx = -lightLevel; xx < lightLevel; xx++){
                int realX = x + xx;
                int realY = y + yy;

                Tile t = grid.getTile(realX, realY);
                int decrease = Math.abs(xx) + Math.abs(yy);
                if(t != null && t.solid){
                    decrease *= 2;
                }
                int level = lightLevel - decrease;
                if(level < 0) level = 0;

                if(grid.isInside(realX, realY)){
                    if(ambLight) {
                        if (ambLights[realX][realY] > level) continue;
                        ambLights[realX][realY] = level;
                    } else {
                        if (dynLights[realX][realY] > level) continue;
                        dynLights[realX][realY] = level;
                    }
                }
            }
        }
    }

    public void recalcLight(int x, int y, boolean ambLight){
        for(int yy = -Vars.lightDistance; yy < Vars.lightDistance; yy++){
            for(int xx = -Vars.lightDistance; xx < Vars.lightDistance; xx++) {
                int realX = x + xx;
                int realY = y + yy;

                if (grid.isInside(realX, realY)) {
                    if (ambLight) {
                        ambLights[realX][realY] = 0;
                    } else {
                        dynLights[realX][realY] = 0;
                    }
                }
            }
        }

        if(ambLight) {
            for (int yy = -Vars.lightDistance * 2; yy < Vars.lightDistance * 2; yy++) {
                for (int xx = -Vars.lightDistance * 2; xx < Vars.lightDistance * 2; xx++) {
                    int realX = x + xx;
                    int realY = y + yy;

                    if (grid.isInside(realX, realY)) {
                        Wall w = grid.getWall(realX, realY);
                        Tile t = grid.getTile(realX, realY);
                        if (w == null && (t == null || !t.solid)) {
                            setBright(realX, realY, true);
                        }
                        if(t != null){
                            if(t.lightLevel > 0){
                                setLightLevel(realX, realY, t.lightLevel, true);
                                //System.out.println("FOUND GAY TORCH");
                            }
                        }
                    }
                }
            }
        }
    }

    public int getLight(int x, int y){
        if(grid.isInside(x, y)) {
            int ambLight = ambLights[x][y];
            int dynLight = dynLights[x][y];
            int light = ambLight + dynLight;
            if(light > Vars.lightDistance) light = Vars.lightDistance;
            return light;
        }
        return Vars.lightDistance;
    }

    public void applyLight(int x, int y, SpriteBatch batch){
        batch.setColor(getLightColor(x, y));
    }

    public Color getLightColor(int x, int y){
        if(Vars.fullBright){
            return Color.WHITE;
        }
        int level = getLight(x, y);

        if(level > 0) {
            float light = level / (float)(Vars.lightDistance - 1);
            if(light > 1) light = 1;
            return new Color(light, light, light, 1);
        } else {
            return new Color(0, 0, 0, 1);
        }
    }
}
