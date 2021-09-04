package me.symix.otherworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

import me.symix.otherworld.entities.player.Player;
import me.symix.otherworld.gui.GameScreen;
import me.symix.otherworld.misc.InputPros;
import me.symix.otherworld.world.World;
import me.symix.otherworld.world.WorldGenerator;

import java.util.Random;

public class Game implements Screen {

	private BitmapFont font;

	public SpriteBatch batch;

	public World world;

	public static int TILESIZE = 16;

	public static int WIDTH = 1920;
	public static int HEIGHT = 1080;
	public static double zoom = 1;

	public float frameRate;
	private float sinceChange;
	long lastTimeCounted;

	public static Random rand = new Random();

	public int respawnTimer = 0;

	public GameScreen currentScreen;

	public MenuPlayerCreator menuPlayerCreator;

	public Game() {
		lastTimeCounted = TimeUtils.millis();
		sinceChange = 0;
		frameRate = Gdx.graphics.getFramesPerSecond();

		WIDTH *= zoom;
		HEIGHT *= zoom;

		batch = new SpriteBatch();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(2);

		//menuPlayerCreator = new MenuPlayerCreator();
		setCurrentScreen(GameScreen.GAME);
	}

	@Override
	public void show() {

	}

	public void tick() {
		if (currentScreen == GameScreen.GAME) {
			if (world != null && world.player != null && world.generated) {
				world.tick();
			}
			if (world != null && world.generated && world.player != null && world.player.fakeplayer) {
				int spawnX = (world.WIDTH / 2) * Game.TILESIZE;
				int spawnY = (world.getHighest(spawnX) + (1 * Game.TILESIZE));
				world.player = new Player(spawnX, spawnY, false, world);
				System.out.println("spawned player in " + spawnX + ", " + spawnY);
			}

			if (world != null && world.player != null && world.player.isDead) {
				world.player = null;
				respawnTimer = 60 * 5;
			}
			if (world != null && world.player == null && respawnTimer == 0) {
				int spawnX = (world.WIDTH / 2) * Game.TILESIZE;
				int spawnY = (world.getHighest(spawnX) + (1 * Game.TILESIZE));
				world.player = new Player(spawnX, spawnY, false, world);
			}

			if (respawnTimer > 0) {
				respawnTimer--;
			}
		}
	}

	@Override
	public void render(float delta) {
		lastTimeCounted = TimeUtils.millis();

		tick();

		if(currentScreen == GameScreen.GAME) {
			if (world != null && world.player != null && world.generated) {
				world.render(batch);
			} else {
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
				batch.begin();
				String text = "Loading ???";
				if (!world.generated) {
					if (world.calculatingLights) {
						text = "Calculating lights: " + (int) (100 * ((double) world.grid.lightHandler.curX / (double) world.WIDTH)) + "%";
					} else {
						text = "Generating world: " + (int) (100 * ((double) WorldGenerator.curX / (double) world.WIDTH)) + "%";
					}
				}
				font.draw(batch, text, Game.WIDTH / 2, Game.HEIGHT / 2);
				batch.end();
			}
		} else if(currentScreen == GameScreen.MENUPLAYERCREATOR){
			menuPlayerCreator.render(batch);
			if(Gdx.input.isKeyPressed(Input.Keys.Y)){
				setCurrentScreen(GameScreen.GAME);
			}
		}

		sinceChange += delta;
		if(sinceChange >= 1000) {
			sinceChange = 0;
			frameRate = Gdx.graphics.getFramesPerSecond();
		}
	}

	@Override
	public void resize(int width, int height) {
		if(world != null) {
			world.viewport.update(width, height);
		}
		if(menuPlayerCreator != null){
			menuPlayerCreator.viewport.update(width, height);
		}
	}

	public void setCurrentScreen(GameScreen screen){
		currentScreen = screen;
		if(screen.equals(GameScreen.GAME)){
			world = new World(this);
			Gdx.input.setInputProcessor(new InputPros(world));

			world.player = new Player((world.WIDTH / 2) * Game.TILESIZE, world.OCEANLEVEL * Game.TILESIZE, true, world);

			//KEEP IMPORTANT
			//AllBiomes.forest.name = AllBiomes.forest.name;
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println("Generating world");
					WorldGenerator.generate(world);
					System.out.println("World generated!");
					System.out.println("Calculating lighting...");
					world.calculatingLights = true;
					world.grid.calcLighting();
					world.calculatingLights = false;
					System.out.println("Lighting calculated!");
					world.finishGene();
				}
			});
			t.start();
		}
	}

	@Override
	public void pause() {
		// Invoked when your application is paused.
	}

	@Override
	public void resume() {
		// Invoked when your application is resumed after pause.
	}

	@Override
	public void hide() {
		// This method is called when another screen replaces this one.
	}

	@Override
	public void dispose() {
		// Destroy screen's assets here.
		font.dispose();
		batch.dispose();
	}
}