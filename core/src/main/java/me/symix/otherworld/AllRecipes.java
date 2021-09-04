package me.symix.otherworld;

import me.symix.otherworld.items.AllItems;
import me.symix.otherworld.tiles.AllTiles;

import java.util.ArrayList;

public class AllRecipes {

    public static ArrayList<Recipe> recipes = new ArrayList<Recipe>();

    static {
        recipes.add(new Recipe(AllTiles.CraftingTable, new ItemEntry(AllItems.Door), new ItemEntry(AllItems.Wood, 10)));
        recipes.add(new Recipe(null, new ItemEntry(AllItems.Torch, 4), new ItemEntry(AllItems.Wood, 2)));

        recipes.add(new Recipe(AllTiles.CraftingTable, new ItemEntry(AllItems.StoneWall, 4), new ItemEntry(AllItems.StoneTile)));
        recipes.add(new Recipe(AllTiles.CraftingTable, new ItemEntry(AllItems.StoneTile), new ItemEntry(AllItems.StoneWall, 4)));

        recipes.add(new Recipe(AllTiles.CraftingTable, new ItemEntry(AllItems.DirtWall, 4), new ItemEntry(AllItems.DirtTile)));
        recipes.add(new Recipe(AllTiles.CraftingTable, new ItemEntry(AllItems.DirtTile), new ItemEntry(AllItems.DirtWall, 4)));

        recipes.add(new Recipe(AllTiles.CraftingTable, new ItemEntry(AllItems.WoodWall, 4), new ItemEntry(AllItems.Wood)));
        recipes.add(new Recipe(AllTiles.CraftingTable, new ItemEntry(AllItems.Wood), new ItemEntry(AllItems.WoodWall, 4)));

        recipes.add(new Recipe(AllTiles.Anvil, new ItemEntry(AllItems.IronHammer), new ItemEntry(AllItems.Wood, 8), new ItemEntry(AllItems.IronBar, 10)));
        recipes.add(new Recipe(AllTiles.Anvil, new ItemEntry(AllItems.SilverPickaxe), new ItemEntry(AllItems.Wood, 8), new ItemEntry(AllItems.SilverBar, 12)));
        recipes.add(new Recipe(AllTiles.Anvil, new ItemEntry(AllItems.GoldPickaxe), new ItemEntry(AllItems.Wood, 8), new ItemEntry(AllItems.GoldBar, 12)));
        recipes.add(new Recipe(AllTiles.Anvil, new ItemEntry(AllItems.OrichalcumPickaxe), new ItemEntry(AllItems.Wood, 8), new ItemEntry(AllItems.OrichalcumBar, 12)));

        recipes.add(new Recipe(AllTiles.Anvil, new ItemEntry(AllItems.IronBow), new ItemEntry(AllItems.IronBar, 8)));

        recipes.add(new Recipe(AllTiles.Furnace, new ItemEntry(AllItems.IronBar, 3), new ItemEntry(AllItems.IronOre, 4)));
        recipes.add(new Recipe(AllTiles.Furnace, new ItemEntry(AllItems.SilverBar, 3), new ItemEntry(AllItems.SilverOre, 4)));
        recipes.add(new Recipe(AllTiles.Furnace, new ItemEntry(AllItems.GoldBar, 3), new ItemEntry(AllItems.GoldOre, 4)));
        recipes.add(new Recipe(AllTiles.Furnace, new ItemEntry(AllItems.CobaltBar, 1), new ItemEntry(AllItems.CobaltOre, 2)));
        recipes.add(new Recipe(AllTiles.Furnace, new ItemEntry(AllItems.OrichalcumBar, 1), new ItemEntry(AllItems.OrichalcumOre, 2)));

        recipes.add(new Recipe(null, new ItemEntry(AllItems.CraftingTable), new ItemEntry(AllItems.Wood, 15)));
        recipes.add(new Recipe(AllTiles.CraftingTable, new ItemEntry(AllItems.Furnace), new ItemEntry(AllItems.StoneTile, 15)));
        recipes.add(new Recipe(AllTiles.CraftingTable, new ItemEntry(AllItems.Anvil), new ItemEntry(AllItems.IronBar, 5)));

        recipes.add(new Recipe(null, new ItemEntry(AllItems.PlatformWood, 2), new ItemEntry(AllItems.Wood)));



    }
}
