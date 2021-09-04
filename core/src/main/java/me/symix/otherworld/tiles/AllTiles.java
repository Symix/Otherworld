package me.symix.otherworld.tiles;

import me.symix.otherworld.TileTier;
import me.symix.otherworld.Vars;
import me.symix.otherworld.items.AllItems;
import me.symix.otherworld.misc.AllSounds;


public class AllTiles {

    public static Tile TileStone = new Tile(1,true, false,false, null, AllSounds.MineStone, "stone");
    public static Tile TileDirt = new Tile(2,true, false,false, TileTier.TIER1, AllSounds.MineDirt,"dirt");
    public static Tile TileGrass = new Tile(3,true, false,false, TileTier.TIER1, AllSounds.StepDirt, "grass");

    public static Tile TileTreeStump[][] = new Tile[2][3];
    public static Tile TileTreeTrunk[] = new Tile[2];
    public static Tile TileTreeHead[] = new Tile[2];

    public static Tile TileWood = new Tile(9,true, false, false,null, AllSounds.MineWood,"wood");
    public static Tile TileTorch = new Tile(10,false, true, false, TileTier.INSTA, AllSounds.MineWood,"torch");

    public static Tile TileDoorClosed = new Tile(11, true, true, true,null, AllSounds.MineWood, "door_closed");
    public static Tile TileDoorOpen = new Tile(12, false, true, true,null, AllSounds.MineWood, "door_open");

    public static Tile TileSand = new Tile(13,true, false,false, TileTier.TIER1, AllSounds.MineDirt, "sand");
    public static Tile TileSandStone = new Tile(14, true, false, false, null, AllSounds.MineStone, "sandstone");

    public static Tile TileCactusStump = new Tile(14, false, false, false, null, AllSounds.MineWood, "cactus_stump");
    public static Tile TileCactusTrunk = new Tile(15, false, false, false, null, AllSounds.MineWood, "cactus_trunk");
    public static Tile TileCactusHead = new Tile(16, false, false, false, null, AllSounds.MineWood, "cactus_head");

    public static Tile IronOre = new Tile(17, true, false, false, null, AllSounds.MineStone, "iron_ore");
    public static Tile SilverOre = new Tile(18, true, false, false, null, AllSounds.MineStone, "silver_ore");
    public static Tile GoldOre = new Tile(19, true, false, false, null, AllSounds.MineStone, "gold_ore");
    public static Tile CobaltOre = new Tile(20, true, false, false, TileTier.TIER2, AllSounds.MineStone, "cobalt_ore");
    public static Tile OrichalcumOre = new Tile(21, true, false, false, TileTier.TIER3, AllSounds.MineStone, "pinkium_ore");

    public static Tile TileFlower[] = new Tile[6];
    public static Tile TileTallGrass = new Tile(22, false, true, false, TileTier.INSTA, AllSounds.MineDirt, "tall_grass");

    public static Tile Snow = new Tile(23, true, false, false, TileTier.TIER1, AllSounds.MineDirt, "snow");

    public static Tile CraftingTable = new Tile(99, false, true, true, null, AllSounds.MineWood, "crafting_table");
    public static Tile Furnace = new Tile(99, false, true, true, null, AllSounds.MineStone, "furnace");
    public static Tile Anvil = new Tile(99, false, true, true, null, AllSounds.MineStone, "anvil");


    public static Tile PlatformWood = new Tile(99, false, true, false, null, AllSounds.MineWood, "platform_wood");

    public static void init(){
        TileStone.dropItem = AllItems.StoneTile;
        addConnectBasic(TileStone);

        TileDirt.dropItem = AllItems.DirtTile;
        addConnectBasic(TileDirt);

        TileGrass.dropItem = AllItems.DirtTile;
        addConnectBasic(TileGrass);

        TileWood.dropItem = AllItems.Wood;
        addConnectBasic(TileWood);

        TileTorch.dropItem = AllItems.Torch;
        TileTorch.lightLevel = Vars.lightDistance;

        TileTreeHead[0] = new Tile(8,false,true,false,null,AllSounds.MineWood, "tree_0_head");;
        TileTreeHead[1] = new Tile(8,false,true,false,null,AllSounds.MineWood, "tree_1_head");;

        TileTreeTrunk[0] = new Tile(7, false,true,false,null,AllSounds.MineWood,"tree_0_trunk");
        TileTreeTrunk[1] = new Tile(7, false,true,false,null,AllSounds.MineWood,"tree_1_trunk");

        TileTreeStump[0][0] = new Tile(5,false,true,false, null, AllSounds.MineWood,"tree_0_stump_left");
        TileTreeStump[0][1] = new Tile(5,false,true,false, null, AllSounds.MineWood,"tree_0_stump_middle");
        TileTreeStump[0][2] = new Tile(5,false,true,false, null, AllSounds.MineWood,"tree_0_stump_right");
        TileTreeStump[1][0] = new Tile(5,false,true,false, null, AllSounds.MineWood,"tree_1_stump_left");
        TileTreeStump[1][1] = new Tile(5,false,true,false, null, AllSounds.MineWood,"tree_1_stump_middle");
        TileTreeStump[1][2] = new Tile(5,false,true,false, null, AllSounds.MineWood,"tree_1_stump_right");

        for(int j = 0; j < TileTreeTrunk.length; j++) {
            for (int i = 0; i < 3; i++) {
                TileTreeStump[j][i].dropItem = AllItems.Wood;
            }
            TileTreeTrunk[j].dropItem = AllItems.Wood;
            TileTreeHead[j].dropItem = AllItems.Wood;
        }

        TileDoorClosed.dropItem = AllItems.Door;
        TileDoorClosed.multiWidth = 1;
        TileDoorClosed.multiHeight = 3;

        TileDoorOpen.dropItem = AllItems.Door;
        TileDoorOpen.multiWidth = 2;
        TileDoorOpen.multiHeight = 3;

        CraftingTable.dropItem = AllItems.CraftingTable;
        CraftingTable.multiWidth = 2;
        CraftingTable.multiHeight = 1;

        Furnace.dropItem = AllItems.Furnace;
        Furnace.multiWidth = 2;
        Furnace.multiHeight = 2;

        Anvil.dropItem = AllItems.Anvil;
        Anvil.multiWidth = 2;
        Anvil.multiHeight = 1;

        TileSand.dropItem = AllItems.SandTile;
        TileSand.addConnect(TileCactusStump);
        addConnectBasic(TileSand);

        TileSandStone.dropItem = AllItems.SandStoneTile;
        addConnectBasic(TileSandStone);

        IronOre.dropItem = AllItems.IronOre;
        addConnectBasic(IronOre);

        SilverOre.dropItem = AllItems.SilverOre;
        addConnectBasic(SilverOre);

        GoldOre.dropItem = AllItems.GoldOre;
        addConnectBasic(GoldOre);

        CobaltOre.dropItem = AllItems.CobaltOre;
        addConnectBasic(CobaltOre);

        OrichalcumOre.dropItem = AllItems.OrichalcumOre;
        addConnectBasic(OrichalcumOre);

        for(int i = 0; i < TileFlower.length; i++){
            TileFlower[i] = new Tile(99 + i, false, true, false, TileTier.INSTA, AllSounds.MineDirt, "flower" + i);
        }

        Snow.dropItem = AllItems.Snow;
        addConnectBasic(Snow);
        Snow.addConnect(TileTreeStump[1][0]);
        Snow.addConnect(TileTreeStump[1][1]);
        Snow.addConnect(TileTreeStump[1][2]);

        PlatformWood.dropItem = AllItems.PlatformWood;
        PlatformWood.addConnect(PlatformWood);
        PlatformWood.isPlatform = true;

        TileCactusStump.dropItem = AllItems.Cactus;
        TileCactusTrunk.dropItem = AllItems.Cactus;
        TileCactusHead.dropItem = AllItems.Cactus;
    }

    private static void addConnectBasic(Tile t){
        t.addConnect(TileDirt);
        t.addConnect(TileGrass);
        t.addConnect(TileStone);
        t.addConnect(TileSand);
        t.addConnect(TileWood);
        t.addConnect(TileSandStone);
        t.addConnect(IronOre);
        t.addConnect(SilverOre);
        t.addConnect(GoldOre);
        t.addConnect(CobaltOre);
        t.addConnect(OrichalcumOre);
        t.addConnect(Snow);
    }
}
