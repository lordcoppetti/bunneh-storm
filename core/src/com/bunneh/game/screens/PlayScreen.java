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
import com.bunneh.game.objects.Floor;
import com.bunneh.game.objects.Hud;
import com.bunneh.game.objects.Player;
import com.bunneh.game.spawners.CrusherSpawner;
import com.bunneh.game.spawners.ObjectSpawnerContainer;
import com.bunneh.game.spawners.SniperEnemySpawner;

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
	private ObjectSpawnerContainer esc;
	private Hud hud;

	private ShapeRenderer debugRender;

	// custom game objects for this screen
	private Player player;
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
		float playerWidth = 10f;
		float playerHeight = 15f;
		float playerYoffset = 3f;
		Rectangle playerRect = new Rectangle(
				-playerWidth/2f, -floorHeight-playerYoffset, 
				playerWidth, playerHeight);
		player = new Player(playerRect);
		player.setXboundaries(-BunnehStormGame.V_WIDTH/2, (BunnehStormGame.V_WIDTH/2)-playerRect.width);
		hud.setPlayer(player);
		
		// create the spawn points for enemies/obstacles
		esc = new ObjectSpawnerContainer();

		CrusherSpawner cs = new CrusherSpawner(-BunnehStormGame.V_WIDTH/2, (BunnehStormGame.V_WIDTH/2)-5f, 4f);
		cs.setFallSpeed(0.7f);
		cs.setTarget(player);
		cs.setFollowTarget(true);
		cs.setFollowTargetLimit(BunnehStormGame.V_WIDTH/3f);
		cs.setSize(20f, 20f);
		cs.setIncreaseSpawnInterval(true);
		cs.setSpawnIntervalIncrement(0.08f);
		cs.setSpawnIntervalMin(0.5f);
		cs.setIncrementFallSpeed(true);
		cs.setFallSpeedIncrement(0.08f);
		cs.setFallSpeedMax(5f);
		esc.addObjectSpawner(cs);

		SniperEnemySpawner ses = new SniperEnemySpawner(-BunnehStormGame.V_WIDTH/2, (BunnehStormGame.V_WIDTH/2)-6f);
		ses.setSpawnIntervalRange(5f, 10f);
		ses.setTarget(player);
		ses.setBulletInterval(0.8f);
		esc.addObjectSpawner(ses);

		// create input multiplexer
		createInput();
	}

	private void createInput() {
		Gdx.input.setInputProcessor(new InputMultiplexer(
				new InputAdapter() {
					@Override
					public boolean keyDown(int keycode) {
						if(keycode == Keys.A) player.moveLeft(true);
						if(keycode == Keys.D) player.moveRight(true);
						if(keycode == Keys.SPACE) player.requestFire();
						return true;
					}
					@Override
					public boolean keyUp(int keycode) {
						if(keycode == Keys.A) player.moveLeft(false);
						if(keycode == Keys.D) player.moveRight(false);
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
		
		esc.update(delta);
		timeAccum += delta;
		while(timeAccum >= timestep) {
			// check collisions as desired
			game.collisionHandler.checkCollision(player, game.goHandler.getObstacles());
			game.collisionHandler.checkCollision(game.goHandler.getObstacles(), game.goHandler.getPlayerBullets(), floor);
			game.collisionHandler.checkCollision(game.goHandler.getEnemyBullets(), player, floor);
			game.collisionHandler.checkCollision(game.goHandler.getEnemies(), game.goHandler.getPlayerBullets(), player, floor);
			game.goHandler.update(timestep, player, floor);
			timeAccum -= timestep;
		}
		
		// Interpolate game objects if physics engine is used!
		
		// render objects
		if(game.debugRender) {
			game.goHandler.debugRender(debugRender, camera, player, floor);
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			int totalObjects = game.goHandler.getObjectsSize();
			totalObjects += player != null ? 1 : 0;
			totalObjects += floor != null ? 1 : 0;
			font.draw(batch, "Screen objects: " + totalObjects, 
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
		player.dispose();
		floor.dispose();
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
