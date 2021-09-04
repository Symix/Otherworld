package me.symix.otherworld.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import me.symix.otherworld.AllRecipes;
import me.symix.otherworld.ItemEntry;
import me.symix.otherworld.Recipe;
import me.symix.otherworld.items.Item;
import me.symix.otherworld.misc.AllTexture;
import me.symix.otherworld.tiles.Tile;

import java.util.ArrayList;

public class Inventory {

    private ItemEntry[][] items = new ItemEntry[4][9];

    private BitmapFont font;
    private BitmapFont greyFont;
    private BitmapFont hpFont;
    private BitmapFont hpGreyFont;

    private Player player;

    public Inventory(Player player){
        this.player = player;

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        greyFont = new BitmapFont();
        greyFont.setColor(Color.GRAY);

        hpFont = new BitmapFont();
        hpFont.setColor(Color.WHITE);
        hpFont.getData().setScale(2);
        hpGreyFont = new BitmapFont();
        hpGreyFont.setColor(Color.GRAY);
        hpGreyFont.getData().setScale(2);

        for(int row = 0; row < 4; row++){
            for(int slot = 0; slot < 9; slot++){
                items[row][slot] = null;
            }
        }
    }

    public void render(SpriteBatch batch){
        OrthographicCamera cam = player.world.cam;

        batch.setColor(Color.WHITE);

        //HOTBAR
        int defOffset = 70;
        int offsetX = 0;
        int offsetY = 0;

        for(int i = 0; i < 9; i++) {
            batch.draw(AllTexture.slot, cam.position.x - (cam.viewportWidth / 2) + 10 + offsetX, cam.position.y + (cam.viewportHeight / 2) - 74);
            if(i == player.selectedSlot){
                batch.draw(AllTexture.select, cam.position.x - (cam.viewportWidth / 2) + 10 + offsetX, cam.position.y + (cam.viewportHeight / 2) - 74);
            }
            ItemEntry itemEntry = player.inventory.getItem(0, i);
            if(itemEntry != null){
                batch.draw(itemEntry.item.texture, cam.position.x - (cam.viewportWidth / 2) + 10 + offsetX + 8, cam.position.y + (cam.viewportHeight / 2) - 74 + 8);
                int textOffsetX = 40 + getXOffset(itemEntry.amount);
                int textOffsetY = 12;
                greyFont.draw(batch, itemEntry.amount + "", cam.position.x - (cam.viewportWidth / 2) + 10 + offsetX + 8 + textOffsetX + 1, cam.position.y + (cam.viewportHeight / 2) - 74 + 8 + textOffsetY - 1);
                font.draw(batch, itemEntry.amount + "", cam.position.x - (cam.viewportWidth / 2) + 10 + offsetX + 8 + textOffsetX, cam.position.y + (cam.viewportHeight / 2) - 74 + 8 + textOffsetY);
            }
            offsetX += defOffset;
        }

        //INVENTORY
        if(player.isInventoryOpen){
            offsetY = defOffset;
            for(int row = 0; row < 3; row++){
                offsetX = 0;
                for(int slot = 0; slot < 9; slot++){
                    batch.draw(AllTexture.slot, cam.position.x - (cam.viewportWidth / 2) + 10 + offsetX, cam.position.y + (cam.viewportHeight / 2) - 74 - offsetY);
                    ItemEntry itemEntry = player.inventory.getItem(row + 1, slot);
                    if(itemEntry != null){
                        batch.draw(itemEntry.item.texture, cam.position.x - (cam.viewportWidth / 2) + 10 + offsetX + 8, cam.position.y + (cam.viewportHeight / 2) - 74 + 8 - offsetY);
                        int textOffsetX = 40 + getXOffset(itemEntry.amount);
                        int textOffsetY = 12;
                        greyFont.draw(batch, itemEntry.amount + "", cam.position.x - (cam.viewportWidth / 2) + 10 + offsetX + 8 + textOffsetX + 1, cam.position.y + (cam.viewportHeight / 2) - 74 + 8 + textOffsetY - 1 - offsetY);
                        font.draw(batch, itemEntry.amount + "", cam.position.x - (cam.viewportWidth / 2) + 10 + offsetX + 8 + textOffsetX, cam.position.y + (cam.viewportHeight / 2) - 74 + 8 + textOffsetY - offsetY);
                    }
                    offsetX += defOffset;

                }
                offsetY += defOffset;
            }
        }

        if(player.isInventoryOpen){
            ArrayList<Recipe> craftable = craftableRecipes();

            float drawX = cam.position.x - (cam.viewportWidth / 2) + 10;
            float drawY = cam.position.y - 100;
            offsetY = 74;

            if(player.selectedRecipe < 0) player.selectedRecipe = 0;
            if(player.selectedRecipe > craftable.size() - 1) player.selectedRecipe = craftable.size() - 1;

            int textOffsetX = 40;
            int textOffsetY = 12;

            for(int i = 0; i < craftable.size(); i++) {
                float realDrawY = drawY + (i * offsetY) - (player.selectedRecipe * offsetY);
                batch.setColor(Color.WHITE);
                if(realDrawY > drawY + (offsetY * 2) || realDrawY < drawY - (offsetY * 2)) {
                    batch.setColor(new Color(1f, 1f, 1f, 0.5f));
                }
                if(realDrawY > drawY + (offsetY * 3) == false && realDrawY < drawY - (offsetY * 3) == false) {
                    batch.draw(AllTexture.slot, drawX, realDrawY);

                    ItemEntry itemEntry = craftable.get(i).result;
                    if (itemEntry != null) {
                        batch.draw(itemEntry.item.texture, drawX + 8, realDrawY + 8);
                        textOffsetX = 40 + getXOffset(itemEntry.amount);
                        greyFont.draw(batch, itemEntry.amount + "", drawX + 8 + textOffsetX + 1, realDrawY + 8 + textOffsetY - 1);
                        font.draw(batch, itemEntry.amount + "", drawX + 8 + textOffsetX, realDrawY + 8 + textOffsetY);
                    }
                    Recipe r = craftable.get(i);

                    for (int j = 0; j < r.cost.size(); j++) {
                        if (i == player.selectedRecipe) {
                            ItemEntry entry = r.cost.get(j);
                            batch.draw(AllTexture.slot, drawX + (74 * j) + 74, realDrawY);
                            batch.draw(entry.item.texture, drawX + (74 * j) + 8 + 74, realDrawY + 8);
                            textOffsetX = 40 + getXOffset(itemEntry.amount);
                            greyFont.draw(batch, entry.amount + "", drawX + 8 + textOffsetX + 1 + 74 + (74 * j), realDrawY + 8 + textOffsetY - 1);
                            font.draw(batch, entry.amount + "", drawX + 8 + textOffsetX + 74 + (74 * j), realDrawY + 8 + textOffsetY);
                        }
                    }
                }
            }
        }

        //Draw item on mouse
        Vector3 mPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        mPos = cam.unproject(mPos);

        int mX = (int) mPos.x;
        int mY = (int) mPos.y;

        if(player.itemOnMouse != null){
            batch.draw(player.itemOnMouse.item.texture, mX - 24, mY - 24);
            greyFont.draw(batch, player.itemOnMouse.amount + "", mX + 1 + 16 + getXOffset(player.itemOnMouse.amount), mY - 12 - 1);
            font.draw(batch, player.itemOnMouse.amount + "", mX + 16 + getXOffset(player.itemOnMouse.amount), mY - 12);
        }

        hpGreyFont.draw(batch, player.hp + " / " + player.maxHp, cam.position.x + (cam.viewportWidth / 2) - 125 + 1, cam.position.y + (cam.viewportHeight / 2) - 10 - 1);
        hpFont.draw(batch, player.hp + " / " + player.maxHp, cam.position.x + (cam.viewportWidth / 2) - 125, cam.position.y + (cam.viewportHeight / 2) - 10);

    }

    public ArrayList<Recipe> craftableRecipes(){
        ArrayList<Recipe> craftable = new ArrayList<Recipe>();
        for(Recipe r : AllRecipes.recipes){
            boolean canCraft = true;
            for(ItemEntry e : r.cost){
                if(getAmount(e.item) < e.amount){
                    canCraft = false;
                }
            }
            if(canCraft){
                if(r.craftingStation != null) {
                    int minX = Math.max(player.getTileX() - 5, 0);
                    int minY = Math.max(player.getTileY() - 5, 0);
                    int maxX = Math.min(player.getTileX() + 5, player.world.WIDTH);
                    int maxY = Math.min(player.getTileY() + 5, player.world.HEIGHT);

                    for (int xx = minX; xx < maxX; xx++) {
                        for (int yy = minY; yy < maxY; yy++) {
                            Tile t = player.world.grid.getTile(xx, yy);
                            if(t == r.craftingStation){
                                craftable.add(r);
                                break;
                            }
                        }
                    }
                } else {
                    craftable.add(r);
                }
            }
        }
        return craftable;
    }

    public void removeItems(ArrayList<ItemEntry> delItems){
        for(ItemEntry e : delItems){
            int toRemove = e.amount;
            for(int row = 0; row < 4; row++){
                for(int slot = 0; slot < 9; slot++){
                    ItemEntry entry = getItem(row, slot);
                    if(entry != null) {
                        if (entry.item == e.item) {
                            if (entry.amount > toRemove) {
                                entry.amount -= toRemove;
                                toRemove = 0;
                            } else {
                                toRemove -= entry.amount;
                                entry.amount = 0;
                            }
                            setItem(row, slot, entry);
                        }
                    }
                }
            }
        }
    }

    public int getAmount(Item item){
        int amount = 0;
        for(int row = 0; row < 4; row++){
            for(int slot = 0; slot < 9; slot++){
                ItemEntry entry = getItem(row, slot);
                if(entry != null){
                    if(entry.item == item){
                        amount += entry.amount;
                    }
                }
            }
        }
        return amount;
    }

    public ItemEntry getItem(int row, int slot){
        return items[row][slot];
    }

    public void setItem(int row, int slot, ItemEntry entry){
        if (entry == null || entry.amount > 0) {
            items[row][slot] = entry;
        } else {
            items[row][slot] = null;
        }
    }

    public void removeOneItem(int row, int slot){
        subtract(row, slot, 1);
    }

    public void add(int row, int slot, int amount){
        this.items[row][slot].amount += amount;
    }

    public void subtract(int row, int slot, int amount){
        this.items[row][slot].amount -= amount;

        if(this.items[row][slot].amount <= 0){
            items[row][slot] = null;
        }
    }

    private int getXOffset(int amount){
        String text = amount + "";
        if(text.length() == 1){
            return 0;
        } else if(text.length() == 2){
            return -10;
        } else if(text.length() == 3){
            return -20;
        } else if(text.length() == 4){
            return -30;
        }
        return 0;
    }
}
