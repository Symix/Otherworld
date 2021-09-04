package me.symix.otherworld.misc;

import com.badlogic.gdx.graphics.Texture;

public class AllTexture {

    public static Texture[] cloud = new Texture[3];
    public static EntityTexture zombie = new EntityTexture("entity/zombie/zombie", 3);

    public static Texture arrow = new Texture("entity/misc/arrow.png");
    public static Texture grappling_hook = new Texture("entity/misc/grappling_hook.png");

    public static Texture[] forest = new Texture[3];
    public static Texture[] desert = new Texture[2];
    public static Texture[] taiga = new Texture[2];

    public static Texture slot = new Texture("gui/slot.png");
    public static Texture select = new Texture("gui/select.png");

    public static Texture[] breakTex = new Texture[4];

    public static Texture scanner = new Texture("scanner.png");

    public static Texture smart = new Texture("tiles/smart.png");

    static{
        for(int i = 0; i < cloud.length; i++) {
            cloud[i] = new Texture("entity/cloud/cloud" + i + ".png");
        }

        loadBiome(forest, "forest");
        loadBiome(desert, "desert");
        loadBiome(taiga, "taiga");

        /*for(int i = 0; i < forest.length; i++) {
            forest[i] = new Texture("background/forest/layer" + i + ".png");
            forest[i].setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        }*/

        for(int i = 0; i < breakTex.length; i++){
            breakTex[i] = new Texture("tiles/break" + i + ".png");
        }
    }

    private static void loadBiome(Texture[] list, String name){
        for(int i = 0; i < list.length; i++) {
            list[i] = new Texture("background/" + name + "/layer" + i + ".png");
            list[i].setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        }
    }
}
