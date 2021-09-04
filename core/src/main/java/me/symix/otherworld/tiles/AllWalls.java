package me.symix.otherworld.tiles;

import me.symix.otherworld.items.AllItems;

public class AllWalls {

    public static Wall Stone = new Wall(1, "stone");
    public static Wall Dirt = new Wall(2, "dirt");
    public static Wall Wood = new Wall(3, "wood");
    public static Wall Sand = new Wall(4, "sand");
    public static Wall SandStone = new Wall(5, "sandstone");

    public static void init(){
        Dirt.dropItem = AllItems.DirtWall;
        addConnectBasic(Dirt);

        Stone.dropItem = AllItems.StoneWall;
        addConnectBasic(Stone);

        Wood.dropItem = AllItems.WoodWall;
        addConnectBasic(Wood);

        addConnectBasic(Sand);
        addConnectBasic(SandStone);
    }

    private static void addConnectBasic(Wall w){
        w.addConnect(Stone);
        w.addConnect(Dirt);
        w.addConnect(Wood);
        w.addConnect(Sand);
        w.addConnect(SandStone);
    }
}
