package me.symix.otherworld.entities.player;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import me.symix.otherworld.*;
import me.symix.otherworld.entities.EntityLiving;
import me.symix.otherworld.entities.enemy.EntityZombie;
import me.symix.otherworld.entities.misc.Arrow;
import me.symix.otherworld.entities.misc.GrapplingHookEntity;
import me.symix.otherworld.items.AllItems;
import me.symix.otherworld.items.Item;
import me.symix.otherworld.items.ItemPlaceable;
import me.symix.otherworld.items.tools.ItemHammer;
import me.symix.otherworld.items.tools.ItemPickaxe;
import me.symix.otherworld.items.tools.ItemTool;
import me.symix.otherworld.items.weapons.ItemBow;
import me.symix.otherworld.misc.AllSounds;
import me.symix.otherworld.misc.AllTexture;
import me.symix.otherworld.misc.Utils;
import me.symix.otherworld.tiles.AllTiles;
import me.symix.otherworld.tiles.Tile;
import me.symix.otherworld.tiles.Wall;
import me.symix.otherworld.world.World;

import java.awt.*;
import java.util.ArrayList;


public class Player {

    public float x, y, xVel, yVel;
    public float speed, maxSpeed, friction, jumpSpeed, gravity, maxFallspeed, flySpeed, maxKnockspeed;

    public int WIDTH = 30;
    public int HEIGHT = 48;

    public boolean flipX = false;

    public Rectangle rect;

    public World world;

    public int selectedSlot = 0;
    public int selectedRecipe = 0;

    public boolean isInventoryOpen = false;

    public ItemEntry itemOnMouse = null;

    public int maxHp = 100, hp = 100;

    private boolean hadTorch = false;

    private Sound soundJump = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.wav"));

    private int stepTimer = 0;
    private int itemTimer = 0;

    private float grapSpeed = 10;

    private PlayerRender renderer;

    public Inventory inventory;

    public ArrayList<Arrow> arrows;

    private Scanner scanner;

    private Point lastProblem = null;

    private boolean cancelUse = false;

    private boolean smartCursor = false;

    private PointLight handTorch;

    private int maxReach;
    private Biome currentBiome = AllBiomes.forest;

    public ArrayList<GrapplingHookEntity> grapplingHooks = new ArrayList<GrapplingHookEntity>();

    private int moveX;

    private ShapeRenderer sr = new ShapeRenderer();

    public boolean fakeplayer;
    public boolean isDead = false;
    public int damageTimer = 0, knockTimer = 0;

    public Player(int x, int y, boolean fakeplayer, World world){
        this.x = x;
        this.y = y;
        this.fakeplayer = fakeplayer;
        this.world = world;

        gravity = 0.22f;
        maxFallspeed = -5; //-5;
        speed = 0.5f;
        maxSpeed = 4.5f;//4.5f
        friction = 0.5f;
        jumpSpeed = 6.5f;
        flySpeed = 20f;
        maxReach = Game.TILESIZE * 8;
        maxKnockspeed = 10f;

        rect = new Rectangle(x, y, WIDTH - 4, HEIGHT);
        inventory = new Inventory(this);
        renderer = new PlayerRender(this);
        arrows = new ArrayList<Arrow>();

        handTorch = new PointLight(world.rayHandler,128, new Color(0.5f, 0.5f, 0.5f, 1f),350, x, y);
        handTorch.setActive(false);
        handTorch.setSoft(true);
        handTorch.setSoftnessLength(50);


        //giveItem(AllItems.GrapplingHook);

        giveItem(AllItems.IronBow);
        giveItem(AllItems.IronPickaxe);
        //giveItem(AllItems.SilverPickaxe);
        giveItem(AllItems.GoldPickaxe);
        //giveItem(AllItems.IronHammer);

        //setItem(2, 0, AllItems.DirtWall, 999);
        //setItem(2, 1, AllItems.StoneWall, 999);
        //setItem(2, 2, AllItems.WoodWall, 999);


        setItem(3, 0, AllItems.DirtTile, 999);
        //setItem(3, 1, AllItems.StoneTile, 999);
        //setItem(3, 2, AllItems.itemGrassTile, 999);
        //setItem(3, 3, AllItems.Door, 999);
        //setItem(3, 4, AllItems.Torch, 999);
    }

    public void takeDamage(int damage, EntityLiving e){
        if(damageTimer == 0) {
            hp -= damage;
            if (hp <= 0) {
                isDead = true;
            }
            damageTimer = 30;
            knockTimer = 15;
            if(e.getX() > getX()) {
                xVel -= e.knockback;
                if(xVel < -maxKnockspeed) xVel = -maxKnockspeed;
            } else {
                xVel += e.knockback;
                if(xVel > maxKnockspeed) xVel = maxKnockspeed;
            }
            yVel += 2;
        }
    }

    private void setItem(int row, int slot, ItemEntry entry){
        inventory.setItem(row, slot, entry);
    }

    private void setItem(int row, int slot, Item item, int amount){
        setItem(row, slot, new ItemEntry(item, amount));
    }

    private void use(int x, int y, boolean left){
        Point touch = new Point(x, y);
        if(touch.distance(getPos()) < getMaxReach() || (getSelectedItem() != null && getSelectedItem().item instanceof ItemBow)) {
            if (!left) {
                if (Gdx.input.justTouched()) {
                    Tile t = world.getTile(x, y);
                    if (t != null) {
                        boolean door = (t == AllTiles.TileDoorClosed || t == AllTiles.TileDoorOpen);
                        if (!door && t.parent != null) {
                            door = (t.parent == AllTiles.TileDoorClosed || t.parent == AllTiles.TileDoorOpen);
                        }
                        if (door) {
                            world.grid.door(t, x / Game.TILESIZE, y / Game.TILESIZE);
                        }
                    }
                }
            }
            ItemEntry itemEntry = getSelectedItem();
            if (itemEntry != null) {
                if (itemTimer == 0) {
                    if (left) {
                        boolean used = false;
                        boolean consume = false;

                        if (itemEntry.item instanceof ItemPlaceable) {
                            ItemPlaceable placeable = (ItemPlaceable) itemEntry.item;
                            boolean isEmpty;
                            if (placeable.placeable instanceof Tile) {
                                isEmpty = world.getTile(x, y) == null;
                            } else {
                                isEmpty = world.getWall(x, y) == null;
                            }
                            if (isEmpty) {
                                boolean canPlace = false;

                                if (world.getTile(x - Game.TILESIZE, y) != null || world.getTile(x + Game.TILESIZE, y) != null || world.getTile(x, y - Game.TILESIZE) != null || world.getTile(x, y + Game.TILESIZE) != null) {
                                    canPlace = true;
                                }
                                if (placeable.placeable instanceof Tile) {
                                    if (world.getWall(x, y) != null) {
                                        canPlace = true;
                                    }
                                    if ((x / Game.TILESIZE) > (getX() / Game.TILESIZE) - 1 && x / Game.TILESIZE < (getX() / Game.TILESIZE) + 2 && y / Game.TILESIZE > (getY() / Game.TILESIZE) - 1 && y / Game.TILESIZE < (getY() / Game.TILESIZE) + 3) {
                                        canPlace = false;
                                    }
                                }
                                if (placeable.placeable instanceof Wall) {
                                    if (world.getWall(x - Game.TILESIZE, y) != null || world.getWall(x + Game.TILESIZE, y) != null || world.getWall(x, y - Game.TILESIZE) != null || world.getWall(x, y + Game.TILESIZE) != null) {
                                        canPlace = true;
                                    }
                                }
                                if (canPlace) {
                                    boolean placed;
                                    if (placeable.placeable instanceof Tile) {
                                        placed = world.setTile(x, y, (Tile) placeable.placeable);
                                    } else {
                                        placed = world.setWall(x, y, (Wall) placeable.placeable, true, true);
                                    }
                                    consume = placed;
                                    used = placed;
                                }
                            }
                        } else if (itemEntry.item instanceof ItemBow) {
                            ItemBow bow = (ItemBow) itemEntry.item;
                            arrows.add(new Arrow(getX(), getY() + 16, x, y, bow.damage, world));
                            AllSounds.FireBow.play(1);
                            used = true;
                        } else if (itemEntry.item instanceof ItemTool) {
                            ItemTool tool = (ItemTool) itemEntry.item;

                            if (itemEntry.item instanceof ItemPickaxe) {
                                Tile t = world.getTile(x, y);
                                if (t != null) {
                                    if (t.mineSound != null) {
                                        t.mineSound.play(1);
                                    }
                                    world.grid.addDamage(x / Game.TILESIZE, y / Game.TILESIZE, (ItemPickaxe) itemEntry.item);
                                }
                                used = true;
                            } else if (itemEntry.item instanceof ItemHammer) {
                                Wall w = world.getWall(x, y);
                                if (w != null) {
                                    if (tool.harvestableTiles.contains(w)) {
                                        used = world.setWall(x, y, null, true, false);
                                    }
                                }
                            }
                        }

                        if (used) {
                            itemTimer += itemEntry.item.itemSpeed;
                        }
                        if (consume) {
                            removeOneItem();
                        }
                    }
                }
            }

        }
    }

    public void tick(){
        if(!fakeplayer) {
            if (itemTimer > 0) {
                if (Vars.fastPlace) {
                    itemTimer = 0;
                } else {
                    itemTimer--;
                }
            }
            if(damageTimer > 0){
                damageTimer--;
            }
            if(knockTimer > 0){
                knockTimer--;
            }

            Vector3 mPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            mPos = world.cam.unproject(mPos);

            int mx = (int) mPos.x;
            int my = (int) mPos.y;

            if (smartCursor) {
                if (scanner == null || scanner.isDead) {
                    if (scanner != null && scanner.collision != null) {
                        lastProblem = scanner.collision;
                    }
                    scanner = new Scanner(getX() + (WIDTH / 2) - 12, getY() + (HEIGHT / 2), mx, my, world.game);
                }
                if (lastProblem != null) {
                    mx = lastProblem.x * Game.TILESIZE;
                    my = lastProblem.y * Game.TILESIZE;

                    if (world.grid.getTile(lastProblem.x, lastProblem.y) == null) {
                        lastProblem = null;
                    }
                }
            }

            if (itemOnMouse != null) {
                if (itemOnMouse.amount <= 0) {
                    itemOnMouse = null;
                }
            }

            if (Gdx.input.justTouched() && cancelUse) {
                cancelUse = false;
            }

            handleInv();

            if (cancelUse == false) {
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    use(mx, my, true);
                }
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    use(mx, my, false);
                }
            }

            handleInput();
            boolean blockGrav = handleGrapple();
            handleCollisions();
            debug();

            handleFly();

            if (blockGrav && isHooked()) {
                xVel = 0;
                yVel = 0;
            }

            x += xVel;
            y += yVel;

            rect.setPosition(getX(), getY());

            stepTimer++;


            if (stepTimer == 18) {
                if (Math.abs(xVel) > 0 && onGround()) {
                    //soundStepDirt.play(1.0f);
                }
                stepTimer = 0;
            }

            //IF HOLDING TORCH
        /*if(getSelectedItem() != null && getSelectedItem().item == AllItems.Torch) {
            hadTorch = true;
            handTorch.setPosition(x + 15, y + 24);
            handTorch.setActive(true);
        } else if(hadTorch){
            hadTorch = false;
            handTorch.setActive(false);
        }*/

            if (getSelectedItem() != null && getSelectedItem().item == AllItems.Torch) {
                world.grid.recalcLight((getX() / Game.TILESIZE) + 1, (getY() / Game.TILESIZE) + 1, false);
                world.grid.setBright((getX() / Game.TILESIZE) + 1, (getY() / Game.TILESIZE) + 1, false);
                hadTorch = true;
            } else if (hadTorch) {
                hadTorch = false;
                world.grid.recalcLight((getX() / Game.TILESIZE) + 1, (getY() / Game.TILESIZE) + 1, false);
            }

            for (GrapplingHookEntity hook : grapplingHooks) {
                hook.tick();
            }

            ArrayList<Arrow> deletables = new ArrayList<Arrow>();

            for (Arrow a : arrows) {
                a.tick();
                if (a.isDead) {
                    deletables.add(a);
                }
            }

            for (Arrow a : deletables) {
                arrows.remove(a);
            }
        }
    }

    private boolean handleGrapple(){
        boolean blockGrav = false;
        if (!grapplingHooks.isEmpty()) {
            if (grapplingHooks.size() > 4) {
                grapplingHooks.remove(0);
            }
            float hookCount = 0;
            float gX = 0;
            float gY = 0;

            for (GrapplingHookEntity hook : grapplingHooks) {
                if (hook.isStuck) {
                    gX += hook.x;
                    gY += hook.y;
                    hookCount++;
                }
            }

            gX /= hookCount;
            gY /= hookCount;

            if (hookCount > 0) {
                xVel = 0;
                yVel = 0;
                blockGrav = true;
            }
            if (getPos().distance(gX, gY) > 20) {
                double angle = Math.atan2(gY - y, gX - x);
                xVel += (float) (grapSpeed * Math.cos(angle));
                yVel += (float) (grapSpeed * Math.sin(angle));
                blockGrav = false;
            }
        }
        return blockGrav;
    }

    private void handleInv(){
        if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) return;

        if(!Gdx.input.justTouched()) return;

        OrthographicCamera cam = world.cam;
        Vector3 mPos = world.getMPos();

        int mX = (int) mPos.x;
        int mY = (int) mPos.y;

        int defOffset = 70;

        int offsetX = 0;
        int offsetY = 0;

        offsetY = defOffset;
        int maxRow = isInventoryOpen ? 4 : 1;
        for (int row = 0; row < maxRow; row++) {
            offsetX = 0;
            for (int slot = 0; slot < 9; slot++) {
                int slotX = (int) (cam.position.x - (cam.viewportWidth / 2) + 10 + offsetX);
                int slotY = (int) (cam.position.y + (cam.viewportHeight / 2) - 6 - offsetY);

                if (mX >= slotX && mX <= slotX + 64 && mY >= slotY && mY <= slotY + 64) {
                    cancelUse = true;
                    ItemEntry itemEntry = inventory.getItem(row, slot);
                    if (itemOnMouse == null) {
                        if (itemEntry != null) {
                            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) || itemEntry.amount == 1) {
                                itemOnMouse = new ItemEntry(itemEntry.item, itemEntry.amount);
                                inventory.setItem(row, slot, null);
                                AllSounds.Grab.play(1);
                            } else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
                                int orgAmount = itemEntry.amount;
                                int newAmount = (int) Math.floor(orgAmount / 2);
                                itemOnMouse = new ItemEntry(itemEntry.item, orgAmount - newAmount);
                                inventory.setItem(row, slot, new ItemEntry(itemEntry.item, newAmount));
                                AllSounds.Grab.play(1);
                            }
                        }
                    } else {
                        if (itemEntry == null) {
                            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) || itemOnMouse.amount == 1) {
                                inventory.setItem(row, slot, itemOnMouse);
                                itemOnMouse = null;
                                AllSounds.Grab.play(1);
                            } else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
                                inventory.setItem(row, slot, new ItemEntry(itemOnMouse.item, 1));
                                itemOnMouse.amount -= 1;
                                AllSounds.Grab.play(1);
                            }
                        } else {
                            Item item = itemEntry.item;
                            if(itemOnMouse.item == item){
                                if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) || itemOnMouse.amount == 1) {
                                    int amount = itemOnMouse.amount;
                                    int oldAmount = itemEntry.amount;
                                    amount = amount + oldAmount;
                                    int extra = 0;

                                    if (amount > item.maxStackSize) {
                                        extra = amount - item.maxStackSize;
                                        amount = item.maxStackSize;
                                    }

                                    inventory.setItem(row, slot, new ItemEntry(item, amount));
                                    AllSounds.Grab.play(1);

                                    if (extra > 0) {
                                        itemOnMouse.amount = extra;
                                    } else {
                                        itemOnMouse = null;
                                    }
                                } else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
                                    if(itemEntry.amount + 1 < item.maxStackSize) {
                                        inventory.add(row, slot, 1);
                                        itemOnMouse.amount -= 1;
                                        AllSounds.Grab.play(1);
                                    }
                                }
                            } else {
                                ItemEntry tempItemEntry = itemOnMouse;
                                itemOnMouse = inventory.getItem(row, slot);
                                inventory.setItem(row, slot, tempItemEntry);
                                AllSounds.Grab.play(1);
                            }
                        }
                    }
                }
                offsetX += defOffset;
            }
            offsetY += defOffset;
        }

        float slotX = cam.position.x - (cam.viewportWidth / 2) + 10;
        float slotY = cam.position.y - 100;

        if(isInventoryOpen && mX >= slotX && mX <= slotX + 64 && mY >= slotY && mY <= slotY + 64){
            if(selectedRecipe > -1) {
                Recipe rec = inventory.craftableRecipes().get(selectedRecipe);
                if (itemOnMouse == null) {
                    itemOnMouse = rec.result.clone();
                    inventory.removeItems(rec.cost);
                    AllSounds.Grab.play(1);
                    cancelUse = true;
                } else if (itemOnMouse.item == rec.result.item) {
                    itemOnMouse.amount += rec.result.amount;
                    inventory.removeItems(rec.cost);
                    AllSounds.Grab.play(1);
                    cancelUse = true;
                }
            }
        }
    }

    public boolean giveItem(Item item){
        return giveItem(new ItemEntry(item, 1));
    }

    public boolean giveItem(ItemEntry item){
        for(int row = 0; row < 4; row++){
            for(int slot = 0; slot < 9; slot++){
                ItemEntry item2 = inventory.getItem(row, slot);
                if(item2 != null) {
                    if (item2.item == item.item) {
                        int oldAmount = item2.amount;
                        if (oldAmount + item.amount < 999) {
                            inventory.add(row, slot, item.amount);
                            return true;
                        }
                    }
                }
            }
        }
        for(int row = 0; row < 4; row++){
            for(int slot = 0; slot < 9; slot++){
                ItemEntry item2 = inventory.getItem(row, slot);
                if(item2 == null){
                    inventory.setItem(row, slot, item);
                    return true;
                }
                if(item2.item == item.item){
                    int oldAmount = item2.amount;
                    if(oldAmount + item.amount < 999){
                        inventory.add(row, slot, item.amount);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void debug(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
            world.spawnEntity(new EntityZombie(getX(), getY(), world));
        }
    }

    public ItemEntry getSelectedItem(){
        if(itemOnMouse != null) return itemOnMouse;

        return inventory.getItem(0, selectedSlot);
    }

    public void removeOneItem(){
        if(itemOnMouse == null){
            inventory.subtract(0, selectedSlot, 1);
        } else {
            itemOnMouse.amount -= 1;
            if(itemOnMouse.amount <= 0){
                itemOnMouse = null;
            }
        }
    }

    private void handleInput(){
        moveX = 0;
        if(Gdx.input.isKeyPressed(Keys.left)){
            moveX -= 1;
        }
        if(Gdx.input.isKeyPressed(Keys.right)){
            moveX += 1;
        }

        if((Math.abs(xVel) < getMaxSpeed() || moveX != Math.signum(xVel)) && knockTimer == 0) {
            xVel += moveX * speed;
        }
        if(Gdx.input.isKeyJustPressed(Keys.cheats)){
            Vars.toggleCheats();
        }
        if(Gdx.input.isKeyJustPressed(Keys.grappling_hook)){
            Vector3 mPos = world.getMPos();
            int mX = (int)mPos.x;
            int mY = (int)mPos.y;
            grapplingHooks.add(new GrapplingHookEntity(getX(), getY() + 16, mX, mY, world));
        }

        if(flipX){
            if(moveX == 1){
                flipX = false;
            }
        } else {
            if(moveX == -1){
                flipX = true;
            }
        }
        if((moveX == 0 || Math.abs(xVel) > getMaxSpeed()) && knockTimer == 0){
            if(Math.abs(xVel) > 0){
                xVel -= Math.signum(xVel) * friction;
            }
            if(Math.abs(xVel) > 0 && Math.abs(xVel) < 0.5){
               xVel = 0;
            }
        }
        if(Gdx.input.isKeyJustPressed(Keys.inventory)){
            isInventoryOpen = !isInventoryOpen;
        }
        if(Gdx.input.isKeyJustPressed(Keys.smart_cursor)){
            smartCursor = !smartCursor;
        }
    }

    private boolean isHooked(){
        for(GrapplingHookEntity hook : grapplingHooks){
            if(hook.isStuck){
                return true;
            }
        }
        return false;
    }

    private void handleCollisions(){
        if(onGround() || isHooked()){
            if(!isHooked() && !Vars.fly && yVel < 0) {
                yVel = 0;
            }

            if(Gdx.input.isKeyPressed(Keys.jump)){
                yVel += jumpSpeed;
                soundJump.play(1.0f);
                if(!grapplingHooks.isEmpty()){
                    grapplingHooks.clear();
                }
            }
        } else {
            if(yVel > maxFallspeed && !Vars.fly){
                yVel -= gravity;
            }
        }

        if(world.place_meeting(x + xVel, y, rect) && !Vars.noclip){
            Point p = world.place_meetingPoint(x + xVel, y, rect);
            if(p != null){
                Tile t = world.grid.getTile(p.x, p.y);
                if(t != null && !t.isPlatform) {
                    boolean emptyUp = (world.grid.getTile(p.x, p.y + 1) == null || !world.grid.getTile(p.x, p.y + 1).solid);
                    emptyUp = (world.grid.getTile(p.x, p.y + 2) == null || !world.grid.getTile(p.x, p.y + 2).solid) && emptyUp;
                    emptyUp = (world.grid.getTile(p.x, p.y + 3) == null || !world.grid.getTile(p.x, p.y + 3).solid) && emptyUp;

                    if (emptyUp && (moveX != 0 || xVel != 0)) {
                        y += Game.TILESIZE;
                    } else {
                        while (!world.place_meeting(x + Math.signum(xVel), y, rect)) {
                            x += Math.signum(xVel);
                        }
                    xVel = 0;
                    }
                }
            }
        }

        if(world.place_meeting(x, y + yVel, rect) && !Vars.noclip){
            Point p = world.place_meetingPoint(x, y + yVel, rect);
            if(p != null) {
                Tile t = world.grid.getTile(p.x, p.y);
                if (t != null && (!t.isPlatform || (yVel < 0 && !Gdx.input.isKeyPressed(Keys.down)))) {
                    while (!world.place_meeting(x, y + Math.signum(yVel), rect)) {
                        y += Math.signum(yVel);
                    }
                    yVel = 0;
                }
            }
        }
    }

    boolean onGround(){
        Point p = world.place_meetingPoint(x, y - 1, rect);
        if(p != null){
            Tile t = world.grid.getTile(p.x, p.y);
            if(t.solid){
                return true;
            }
            if(t.isPlatform){
                if(Gdx.input.isKeyPressed(Keys.down)){
                    return false;
                } else {
                    return true;
                }
            }
        }
        //return (world.place_meeting(x, y - 1, rect));
        return false;
    }

    private void handleFly(){
        if(Vars.fly) {
            boolean change = false;
            if (Gdx.input.isKeyPressed(Keys.down)) {
                if (yVel > -25) {
                    yVel -= 1;
                }
                change = true;
            }
            if (Gdx.input.isKeyPressed(Keys.jump)) {
                if (yVel < 25) {
                    yVel += 1;
                }
                change = true;
            }
            if(!change){
                yVel = 0;
            }
        }
    }

    public int getX(){
        return (int)x;
    }

    public int getY(){
        return (int)y;
    }

    public Point getPos(){
        return new Point(getX(), getY());
    }

    public void render(SpriteBatch batch){
        if(!grapplingHooks.isEmpty()) {
            for(GrapplingHookEntity hook : grapplingHooks) {
                hook.render(batch);

                sr.setColor(Color.BROWN);
                sr.setProjectionMatrix(batch.getProjectionMatrix());

                batch.end();

                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.rectLine(x + (rect.getWidth() / 2), y + (rect.getHeight() / 2), hook.x + (hook.rect.getWidth() / 2), hook.y + (hook.rect.getHeight() / 2), 5);
                sr.end();

                batch.begin();
            }
        }

        ItemEntry curItem = getSelectedItem();

        Vector3 mPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        mPos = world.cam.unproject(mPos);

        int mX = (int) mPos.x;
        int mY = (int) mPos.y;

        if(curItem != null && itemTimer != 0){
            if(curItem.item instanceof ItemBow){
                flipX = mX < x;
            }
        }
        renderer.render(getX(), getY() - 1, batch);

        if(curItem != null && itemTimer != 0){
            Sprite sprite = curItem.item.sprite;
            int offsetX = 0;
            if(flipX){
                offsetX = -16;
            }
            if(curItem.item instanceof ItemBow) {
                sprite.setRotation(Utils.getAngle(getX(), getY(), mX, mY));
                sprite.setPosition(getX() + offsetX, getY());
                sprite.draw(batch);
            } else if(curItem.item instanceof ItemTool){
                ItemTool tool = (ItemTool)curItem.item;

                sprite = tool.spriteHand;

                float maxAngle = 180;
                int angle = 90 - (int)(maxAngle * (((float)curItem.item.itemSpeed - (float)itemTimer) / (float)curItem.item.itemSpeed));

                int offsetY;

                if(flipX) {
                    sprite.setOrigin(44 + 16, 3);
                    offsetX = 21;
                    offsetY = 12;
                    angle = 360 - angle;
                } else {
                    sprite.setOrigin(3, 3);
                    offsetX = 9;
                    offsetY = 13;
                }

                sprite.setRotation(angle);

                sprite.setFlip(flipX, false);
                sprite.setOriginBasedPosition(getX() + offsetX, getY() + offsetY);
                sprite.setScale(1);

                sprite.setColor(world.getLight(getX(), getY() + 1));
                sprite.draw(batch);

            }
        }

        if(scanner != null) {
            scanner.render(batch);
        }

        for(Arrow a : arrows){
            a.render(batch);
        }

        if(smartCursor) {
            if (lastProblem != null) {
                batch.setColor(Color.WHITE);
                batch.draw(AllTexture.smart, lastProblem.x * Game.TILESIZE, lastProblem.y * Game.TILESIZE);
            }
        }

        inventory.render(batch);
    }

    public Biome getBiome(){
        return currentBiome;
    }

    public int getMaxReach(){
        if(Vars.unlimitedRange){
            return Integer.MAX_VALUE;
        } else {
            return maxReach;
        }
    }

    public float getMaxSpeed(){
        if(Vars.superSpeed){
            return 25;
        } else {
            return maxSpeed;
        }
    }

    public int getTileX(){
        return getX() / World.TILESIZE;
    }

    public int getTileY(){
        return getY() / World.TILESIZE;
    }

    public void setBiome(Biome biome) {
        this.currentBiome = biome;
    }
}
