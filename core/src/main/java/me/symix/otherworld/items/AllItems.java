package me.symix.otherworld.items;

import me.symix.otherworld.items.tools.ItemHammer;
import me.symix.otherworld.items.tools.ItemPickaxe;
import me.symix.otherworld.items.weapons.ItemBow;
import me.symix.otherworld.tiles.AllTiles;
import me.symix.otherworld.tiles.AllWalls;

public class AllItems {

    public static ItemPlaceable DirtTile = new ItemPlaceable(AllTiles.TileDirt, "Dirt Tile", "dirt");
    public static ItemPlaceable StoneTile = new ItemPlaceable(AllTiles.TileStone, "Stone Tile", "stone");
    public static ItemPlaceable GrassTile = new ItemPlaceable(AllTiles.TileGrass, "Grass Tile", "grass_tile");
    public static ItemPlaceable SandTile = new ItemPlaceable(AllTiles.TileSand, "Sand Tile", "sand");
    public static ItemPlaceable SandStoneTile = new ItemPlaceable(AllTiles.TileSandStone, "Sandstone Tile", "sandstone");
    public static ItemPlaceable Snow = new ItemPlaceable(AllTiles.Snow, "Snow Tile", "snow");

    public static ItemPlaceable Torch = new ItemPlaceable(AllTiles.TileTorch, "Torch", "torch");

    public static ItemPlaceable Wood = new ItemPlaceable(AllTiles.TileWood, "Wood", "wood");
    public static ItemPlaceable WoodWall = new ItemPlaceable(AllWalls.Wood, "Wood Wall", "wood_wall");

    public static ItemPlaceable Cactus = new ItemPlaceable(AllTiles.TileCactusTrunk, "Cactus", "cactus");

    public static ItemPlaceable Door = new ItemPlaceable(AllTiles.TileDoorClosed, "Door", "door");
    public static ItemPlaceable CraftingTable = new ItemPlaceable(AllTiles.CraftingTable, "Crafting Table", "crafting_table");
    public static ItemPlaceable Furnace = new ItemPlaceable(AllTiles.Furnace, "Furnace", "furnace");
    public static ItemPlaceable Anvil = new ItemPlaceable(AllTiles.Anvil, "Anvil", "anvil");

    public static ItemPlaceable DirtWall = new ItemPlaceable(AllWalls.Dirt, "Dirt Wall", "dirt_wall");
    public static ItemPlaceable StoneWall = new ItemPlaceable(AllWalls.Stone, "Stone Wall", "stone_wall");

    public static ItemPlaceable PlatformWood = new ItemPlaceable(AllTiles.PlatformWood, "Wooden Platform", "platform_wood");

    public static ItemPickaxe IronPickaxe = new ItemPickaxe(40, 18,"Iron Pickaxe", "iron_pickaxe");
    public static ItemPickaxe SilverPickaxe = new ItemPickaxe(50, 22,"Silver Pickaxe", "silver_pickaxe");
    public static ItemPickaxe GoldPickaxe = new ItemPickaxe(60, 23,"Gold Pickaxe", "gold_pickaxe");
    public static ItemPickaxe OrichalcumPickaxe = new ItemPickaxe(100, 5,"Orichalcum Pickaxe", "orichalcum_pickaxe");

    public static ItemHammer IronHammer = new ItemHammer(25, "Iron Hammer", "iron_hammer");

    public static ItemBow IronBow = new ItemBow(7, 20, "Iron Bow", "iron_bow");

    public static Item GrapplingHook = new Item(30, 1, "Grappling Hook", "grappling_hook");


    public static Item IronOre = new ItemMaterial("Iron Ore", "iron_ore");
    public static Item IronBar = new ItemMaterial( "Iron Bar", "iron_bar");

    public static Item SilverOre = new ItemMaterial( "Silver Ore", "silver_ore");
    public static Item SilverBar = new ItemMaterial( "Silver Bar", "silver_bar");

    public static Item GoldOre = new ItemMaterial("Gold Ore", "gold_ore");
    public static Item GoldBar = new ItemMaterial( "Gold Bar", "gold_bar");

    public static Item CobaltOre = new ItemMaterial("Cobalt Ore", "cobalt_ore");
    public static Item CobaltBar = new ItemMaterial( "Cobalt Bar", "cobalt_bar");

    public static Item OrichalcumOre = new ItemMaterial("Orichalcum Ore", "orichalcum_ore");
    public static Item OrichalcumBar = new ItemMaterial( "Orichalcum Bar", "orichalcum_bar");


    static{
        AllWalls.init();
        AllTiles.init();
    }
}
