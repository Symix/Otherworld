package me.symix.otherworld.misc;

import com.badlogic.gdx.InputProcessor;
import me.symix.otherworld.entities.player.Player;
import me.symix.otherworld.world.World;

public class InputPros implements InputProcessor {

    private World world;

    public InputPros(World world){
        this.world = world;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        Player player = world.player;
        if(player.isInventoryOpen){
            player.selectedRecipe += Math.signum(amountY);
            return true;
        } else {
            player.selectedSlot += Math.signum(amountY);
            if (player.selectedSlot > 8) player.selectedSlot = 0;
            if (player.selectedSlot < 0) player.selectedSlot = 8;
            return true;
        }
    }
}
