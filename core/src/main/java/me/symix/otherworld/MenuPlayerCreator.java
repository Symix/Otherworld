package me.symix.otherworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import me.symix.otherworld.entities.player.PlayerRender;
import me.symix.otherworld.gui.FakePlayerRender;
import me.symix.otherworld.misc.AllTexture;

public class MenuPlayerCreator {

    private FakePlayerRender playerRender;
    public OrthographicCamera cam;
    public ExtendViewport viewport;

    public MenuPlayerCreator(){
        cam = new OrthographicCamera();
        viewport = new ExtendViewport(Game.WIDTH, Game.HEIGHT, cam);
        cam.position.set(0, 0, 0);
        cam.update();
        playerRender = new FakePlayerRender();
    }

    public void render(SpriteBatch batch){
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        Gdx.gl.glClearColor(0.454901961f, 0.568627451f, 0.968627451f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.setColor(Color.WHITE);
        Texture[] bg = AllTexture.forest;
        int bg0 = 0;
        int bg1 = 0;
        int yOffset1 = -500;
        int yOffset2 = -800;

        bg0 = 0;
        bg1 = 2;


        batch.draw(bg[bg0],0 - (Game.WIDTH / 2) - 50, 0 + yOffset1, 0, 0, bg[bg0].getWidth() * 2, bg[bg0].getHeight());
        batch.draw(bg[bg1],0 - (Game.WIDTH / 2) - 50, 0 + yOffset2, 0, 0, bg[bg1].getWidth() * 2, bg[bg1].getHeight());

        playerRender.render(0, 0, batch);
        batch.end();
    }
}
