package me.symix.otherworld.world;

import me.symix.otherworld.AllBiomes;
import me.symix.otherworld.Biome;
import me.symix.otherworld.Game;
import me.symix.otherworld.OpenSimplexNoise;
import me.symix.otherworld.tiles.AllTiles;
import me.symix.otherworld.tiles.Tile;
import me.symix.otherworld.tiles.Wall;
import me.symix.otherworld.world.Noise;
import me.symix.otherworld.world.World;

public class WorldGenerator {

    public static int curX;

    public static void generate(World world){
        double defHeight = world.OCEANLEVEL;
        double heightOff = 0;

        Biome biome;

        OpenSimplexNoise openNoise = new OpenSimplexNoise(Game.rand.nextInt(10000000));

        for(int x = 0; x < world.WIDTH; x++) {
            curX = x;
            Double d = Noise.noise(x / 10.0);
            heightOff += (d * 3);
            int height = (int) defHeight - (int) heightOff;

            /*if(x > (world.WIDTH / 2) + 250){
                biome = AllBiomes.desert;
            } else if(x < (world.WIDTH / 2) - 250){
                biome = AllBiomes.taiga;
            } else {
                biome = AllBiomes.forest;
            }*/
            for(int y = 0; y < world.HEIGHT; y++) {
                Tile t = null;
                Wall w = null;

                Double biomeD = openNoise.eval(x / 200, 0);
                if(biomeD < -0.3){
                    biome = AllBiomes.desert;
                } else if (biomeD > 0.3){
                    biome = AllBiomes.taiga;
                } else {
                    biome = AllBiomes.forest;
                }
                //System.out.println(biomeD);

                if (y < height - 5) {
                    if(y < height - 20) {
                        boolean cave = false;
                        Double d2;
                        for(double i = 0; i < 4; i++) {
                            d2 = openNoise.eval(x / 20.0, y / 20.0, i);
                            if (d2 > 0.4) {
                                t = null;
                                w = biome.layer3Wall;
                                cave = true;
                            }
                        }

                        if(!cave){
                            t = biome.layer3Tile;
                            w = biome.layer3Wall;
                            d2 = Noise.noise(x / 5.0, y / 5.0, 0.0);
                            if(d2 > 0.5){
                                t = AllTiles.OrichalcumOre;
                            }
                            d2 = Noise.noise(x / 10.0, y / 10.0, 30.0);
                            if(d2 > 0.4){
                                t = AllTiles.IronOre;
                            }
                            d2 = Noise.noise(x / 10.0, y / 10.0, 60.0);
                            if(d2 > 0.4){
                                t = AllTiles.SilverOre;
                            }
                            d2 = Noise.noise(x / 10.0, y / 10.0, 90.0);
                            if(d2 > 0.4){
                                t = AllTiles.GoldOre;
                            }

                        }
                    } else {
                        t = biome.layer3Tile;
                        w = biome.layer3Wall;
                    }
                } else if(y < height){
                    t = biome.layer2Tile;
                    w = biome.layer2Wall;
                } else if(y == height){
                    t = biome.layer1Tile;
                    w = null;

                    Double d3 = Noise.noise(x / 2.0, y / 2.0);
                    if(d3 > 0.15){
                        if(biome == AllBiomes.desert){
                            //SPAWN CACTUSSS
                            int cactusHeight = Game.rand.nextInt(3) + 7;
                            world.grid.setTile(x, y + 1, AllTiles.TileCactusStump, false, false);
                            for (int cactusY = 2; cactusY < cactusHeight + 2; cactusY++) {
                                Tile cactusTile = AllTiles.TileCactusTrunk;
                                if (cactusY == cactusHeight + 1) {
                                    cactusTile = AllTiles.TileCactusHead;
                                }
                                world.grid.setTile(x, y + cactusY, cactusTile, false, false);
                            }
                        } else if(biome.treeType != -1){
                            if (world.grid.getTile(x - 1, y + 1) == null) {
                                //SPAWN TREEEE!!!!
                                int treeHeight = Game.rand.nextInt(16) + 5;
                                world.grid.setTile(x - 1, y + 1, AllTiles.TileTreeStump[biome.treeType][0], false, false);
                                world.grid.setTile(x, y + 1, AllTiles.TileTreeStump[biome.treeType][1], false, false);
                                world.grid.setTile(x + 1, y + 1, AllTiles.TileTreeStump[biome.treeType][2], false, false);

                                world.grid.setTile(x - 1, y, biome.layer1Tile, false, false);
                                world.grid.setTile(x, y, biome.layer1Tile, false, false);
                                world.grid.setTile(x + 1, y, biome.layer1Tile, false, false);

                                for (int treeY = 2; treeY < treeHeight + 2; treeY++) {
                                    Tile treeTile = AllTiles.TileTreeTrunk[biome.treeType];
                                    if (treeY == treeHeight + 1) {
                                        treeTile = AllTiles.TileTreeHead[biome.treeType];
                                    }
                                    world.grid.setTile(x, y + treeY, treeTile, false, false);
                                }
                            }
                        }
                    }
                    d3 = Noise.noise(x / 2.0, y / 2.0, 30);
                    if(d3 > 0.10){
                        if(biome == AllBiomes.forest){
                            if(world.grid.getTile(x, y + 1) == null) {
                                world.grid.setTile(x, y + 1, AllTiles.TileTallGrass, false, false);
                            }
                        }
                    }
                    d3 = Noise.noise(x / 2.0, y / 2.0, 90);
                    if(d3 > 0.15){
                        if(biome == AllBiomes.forest){
                            if(world.grid.getTile(x, y + 1) == null) {
                                world.grid.setTile(x, y + 1, AllTiles.TileFlower[Game.rand.nextInt(AllTiles.TileFlower.length)], false, false);
                            }
                        }
                    }
                }
                if(t != null) {
                    if(world.grid.getTile(x, y) == null) {
                        world.grid.setTile(x, y, t, false, false);
                    }
                }
                world.grid.setWall(x, y, w, false, false);
            }
        }
    }
}
