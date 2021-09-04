package me.symix.otherworld.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class AllSounds {

    public static Sound ArrowCollide = Gdx.audio.newSound(Gdx.files.internal("sounds/arrow_collide.wav"));
    public static Sound Splat = Gdx.audio.newSound(Gdx.files.internal("sounds/Splat.wav"));
    public static Sound FireBow = Gdx.audio.newSound(Gdx.files.internal("sounds/fire_bow.wav"));

    public static Sound StepDirt = Gdx.audio.newSound(Gdx.files.internal("sounds/step_dirt.wav"));

    public static Sound MineDirt = Gdx.audio.newSound(Gdx.files.internal("sounds/mine_dirt.wav"));
    public static Sound MineStone = Gdx.audio.newSound(Gdx.files.internal("sounds/mine_stone.mp3"));
    public static Sound MineWood = Gdx.audio.newSound(Gdx.files.internal("sounds/mine_wood.wav"));

    public static Sound DoorOpen = Gdx.audio.newSound(Gdx.files.internal("sounds/door_open.ogg"));
    public static Sound DoorClose = Gdx.audio.newSound(Gdx.files.internal("sounds/door_close.ogg"));

    public static Sound Grab = Gdx.audio.newSound(Gdx.files.internal("sounds/grab.wav"));


}
