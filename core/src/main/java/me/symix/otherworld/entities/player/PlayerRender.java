package me.symix.otherworld.entities.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.symix.otherworld.entities.misc.GrapplingHookEntity;

public class PlayerRender {

    private int hairType = 0;
    private int faceType = 0;
    private int pupilType = 0;
    private int eyeType = 0;
    private int shirtType = 0;
    private int pantsType = 0;
    private int shoesType = 0;
    private int handsType = 0;

    private Player player;

    private Texture[][] hairL = new Texture[2][3];
    private Texture[][] faceL = new Texture[1][3];
    private Texture[][] pupilL = new Texture[1][3];
    private Texture[][] eyeL = new Texture[1][3];
    private Texture[][] shirtL = new Texture[1][3];
    private Texture[][] pantsL = new Texture[1][3];
    private Texture[][] shoesL = new Texture[1][3];
    private Texture[][] handsL = new Texture[1][3];

    private Color hairColor;
    private Color skinColor;
    private Color shirtColor;
    private Color pantsColor;
    private Color shoesColor;
    private Color pupilColor;

    private int animationTimer = 0;

    public PlayerRender(Player player){
        this.player = player;

        loadTextures(hairL, "hair");
        loadTextures(faceL, "face");
        loadTextures(pupilL, "pupil");
        loadTextures(eyeL, "eye");
        loadTextures(shirtL, "shirt");
        loadTextures(pantsL, "pants");
        loadTextures(shoesL, "shoes");
        loadTextures(handsL, "hands");

        hairColor = new Color(171 / 255f, 68 / 255f, 40 / 255f, 255 / 255f);
        skinColor = new Color(255 / 255f, 170 / 255f, 103 / 255f, 255 / 255f);

        pupilColor = new Color(10 / 255f, 100 / 255f, 255 / 255f, 255 / 255f);
        shirtColor = new Color(81 / 255f, 165 / 255f, 193 / 255f, 255 / 255f);

        pantsColor = new Color(227 / 255f, 205 / 255f, 156 / 255f, 255 / 255f);
        shoesColor = new Color(154 / 255f, 101 / 255f, 58 / 255f, 255 / 255f);
    }

    public void render(int x, int y, SpriteBatch batch){
        Texture tex;

        animationTimer++;

        int img;

        img = 1;

        if(animationTimer > 90){
            animationTimer = 0;
        }

        boolean hookStuck = false;
        for(GrapplingHookEntity hook : player.grapplingHooks){
            if(hook.isStuck){
                hookStuck = true;
            }
        }
        if(Math.abs(player.xVel) > 0 && !hookStuck){
            img = animationTimer >= 10 ? 1 : 2;

            if(animationTimer >= 20){
                animationTimer = 0;
            }
        }

        if(!player.onGround()) {
            img = 0;
        }

        tex = shirtL[shirtType][img];
        rend(x, y, tex, shirtColor, batch);

        tex = handsL[handsType][img];
        rend(x, y, tex, skinColor, batch);

        tex = pantsL[pantsType][img];
        rend(x, y, tex, pantsColor, batch);

        tex = shoesL[shoesType][img];
        rend(x, y, tex, shoesColor, batch);

        tex = faceL[faceType][img];
        rend(x, y, tex, skinColor, batch);

        tex = pupilL[pupilType][img];
        rend(x, y, tex, pupilColor, batch);

        tex = eyeL[eyeType][img];
        rend(x, y, tex, Color.WHITE, batch);

        tex = hairL[hairType][img];
        rend(x, y, tex, hairColor, batch);
    }

    private void rend(int x, int y, Texture tex, Color color, SpriteBatch batch){
        Color c = player.world.getLight(x + 4, y + 8);
        batch.setColor(new Color(c.r * color.r, c.g * color.g, c.b * color.b, 1));
        batch.draw(tex, x, y, tex.getWidth(), tex.getHeight(), 0, 0, tex.getWidth(), tex.getHeight(), player.flipX, false);
    }

    private void loadTextures(Texture[][] texList, String name){
        for(int i = 0; i < texList.length; i++){
            for(int j = 0; j < texList[i].length; j++) {
                texList[i][j] = loadTexture(name + i + j);
            }
        }
    }

    private Texture loadTexture(String texture){
        return new Texture("entity/player/" + texture + ".png");
    }
}
