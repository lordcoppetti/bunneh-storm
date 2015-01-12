package com.bunneh.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.InputAdapter;
import com.bunneh.game.handlers.LevelHandler;
import com.bunneh.game.objects.Floor;
import com.bunneh.game.objects.Hud;
import com.bunneh.game.objects.Player;

/*
 * This PlayScreen doesn't use box2d or any physics engine.
 * It doesn't even use a Stage to manage game objects.
 * Also checks collisions by rectangle overlap by using the CollisionHandler.
 */

public class PlayScreen implements Screen {
	
	private static boolean gameOver = false;

	private final float timestep = 1 / 60f;
	private float timeAccum = 0f;

	private BunnehStormGame game;
	private Color backColor = new Color(0f, 0f, 0f, 1f);
	private Texture background;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	//private ObjectSpawnerContainer esc;
	private Hud hud;

	private ShapeRenderer debugRender;

	// custom game objects for this screen
	private Floor floor;
	
	public PlayScreen(BunnehStormGame game) {
		gameOver = false;
		this.game = game;
	}

	@Override
	public void show() {
		// initialize common game objects
		game.goHandler.initialize();

		// initialize screen stuff
		camera = new OrthographicCamera(BunnehStormGame.V_WIDTH, BunnehStormGame.V_HEIGHT);
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setScale(0.8f);
		hud = new Hud(font);
		hud.setPlayerLivesPos(new Vector2(-BunnehStormGame.V_WIDTH/2.2f, -BunnehStormGame.V_HEIGHT/2.2f));
		hud.setPlayerAtkPowPos(new Vector2(BunnehStormGame.V_WIDTH/3.5f, hud.getPlayerLivesPos().y));
		
		// for debug rendering
		if(game.debugRender) debugRender = new ShapeRenderer();
		
		// create the floor (custom game object)
		float floorHeight = BunnehStormGame.V_HEIGHT / 4f; // 1/4 of the screen
		 floor = new Floor(new Rectangle(
			-BunnehStormGame.V_WIDTH/2, -BunnehStormGame.V_HEIGHT/2,
			BunnehStormGame.V_WIDTH, floorHeight));
		
		// create the player (custom game object)
		float playerWidth = 20f;
		float playerHeight = 25f;
		float playerYoffset = 3f;
		Rectangle playerRect = new Rectangle(
				-playerWidth/2f, -floorHeight-playerYoffset, 
				playerWidth, playerHeight);
		Player player = new Player(playerRect);
		player.setXboundaries(-BunnehStormGame.V_WIDTH/2, (BunnehStormGame.V_WIDTH/2)-playerRect.width);
		hud.setPlayer(player);

		// init level handler
		game.levelHandler = new LevelHandler(player);
		
		// create the first levels
		game.levelHandler.createLevels();
		

		// create input multiplexer
		createInput();
	}

	private void createInput() {
		Gdx.input.setInputProcessor(new InputMultiplexer(
				new InputAdapter() {
					@Override
					public boolean keyDown(int keycode) {
						if(keycode == Keys.A) game.levelHandler.getPlayer().moveLeft(true);
						if(keycode == Keys.D) game.levelHandler.getPlayer().moveRight(true);
						if(keycode == Keys.SPACE) game.levelHandler.getPlayer().requestFire();
						return true;
					}
					@Override
					public boolean keyUp(int keycode) {
						if(keycode == Keys.A) game.levelHandler.getPlayer().moveLeft(false);
						if(keycode == Keys.D) game.levelHandler.getPlayer().moveRight(false);
						return true;
					}
				}
		));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(backColor.r, backColor.g, backColor.b, backColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(gameOver) {
			gotoGameOverScreen();
			return;
		}
		
		//esc.update(delta);
		timeAccum += delta;
		while(timeAccum >= timestep) {
			// update level handler
			game.levelHandler.update(timestep);
			// check collisions as desired
			game.collisionHandler.checkCollision(game.levelHandler.getPlayer(), game.goHandler.getObstacles());
			game.collisionHandler.checkCollision(game.goHandler.getObstacles(), game.goHandler.getPlayerBullets(), floor);
			game.collisionHandler.checkCollision(game.goHandler.getEnemyBullets(), game.levelHandler.getPlayer(), floor);
			game.collisionHandler.checkCollision(game.goHandler.getEnemies(), game.goHandler.getPlayerBullets(), game.levelHandler.getPlayer(), floor);
			game.goHandler.update(timestep, game.levelHandler.getPlayer(), floor);
			timeAccum -= timestep;
		}
		
		// Interpolate game objects if physics engine is used!
		
		// render objects
		if(game.debugRender) {
			game.goHandler.debugRender(debugRender, camera, game.levelHandler.getPlayer(), floor);
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			//int totalObjects = game.goHandler.getObjectsSize();
			//totalObjects += player != null ? 1 : 0;
			//totalObjects += floor != null ? 1 : 0;
			int currentLevel = game.levelHandler.getCurrentLevel().getLvlNumber();
			font.draw(batch, "Level : " + currentLevel, 
					-BunnehStormGame.V_WIDTH/2, BunnehStormGame.V_HEIGHT/2);
			hud.render(batch);
			batch.end();
			
		} else {
			game.goHandler.render(batch, camera);
		}
		
	}


	@Override
	public void resize(int width, int height) {
		// Somehow keep aspect ratio...
		float aspectRatio = (float) width / (float) height;
		camera = new OrthographicCamera(BunnehStormGame.V_WIDTH * aspectRatio, BunnehStormGame.V_HEIGHT);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		dispose();
	}

	private void gotoGameOverScreen() {
		game.setScreen(game.getGameOverScreen());
	}

	@Override
	public void dispose() {
		game.goHandler.disposeAll();
		floor.dispose();
		game.levelHandler.dispose();
		if(background != null) background.dispose();
		if(debugRender != null) debugRender.dispose();
		if(font != null) font.dispose();
		batch.dispose();
		Gdx.input.setInputProcessor(null);
	}
	
	public static void setGameOver(boolean over) {
		gameOver = over;
	}

}
