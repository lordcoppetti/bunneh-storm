package com.bunneh.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.InputAdapter;
import com.bunneh.game.handlers.LevelHandler;
import com.bunneh.game.objects.Floor;
import com.bunneh.game.objects.Hud;
import com.bunneh.game.objects.Player;
import com.bunneh.game.utils.RegionComparator;
import com.bunneh.game.utils.TextureAtlasChiches;

/*
 * This PlayScreen doesn't use box2d or any physics engine.
 * It doesn't even use a Stage to manage game objects.
 * Also checks collisions by rectangle overlap by using the CollisionHandler.
 */

public class PlayScreen implements Screen {
	
	private static boolean gameOver = false;

	private final float timestep = 1 / 60f;
	private float timeAccum = 0f;

	public static TextureAtlas atlas;
	public static TextureAtlas explosionAtlas;


	private BunnehStormGame game;
	private Color backColor = new Color(0f, 0f, 0f, 1f);
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	//private ObjectSpawnerContainer esc;
	private Hud hud;

	private ShapeRenderer debugRender;

	// custom game objects for this screen
	private Floor floor;

	private Texture background;
	private Texture moon;
	private Texture birdman;
	private float birdmanWidth;
	private float birdmanHeight;
	
	public PlayScreen(BunnehStormGame game) {
		gameOver = false;
		this.game = game;
	}
	

	@Override
	public void show() {
		// initialize common game objects
		game.goHandler.initialize();
		
		// load the game assets
		atlas = new TextureAtlas(Gdx.files.internal("assets.atlas"));
		Array<AtlasRegion> assetsRegions = atlas.getRegions();
		assetsRegions.sort(new RegionComparator());

		explosionAtlas = new TextureAtlas(Gdx.files.internal("explosion.atlas"));
		Array<AtlasRegion> explosionRegions = atlas.getRegions();
		explosionRegions.sort(new RegionComparator());
		
		Array<AtlasRegion> idleRegions = TextureAtlasChiches.getRegions(atlas, "idle", "-", 1);
		Array<AtlasRegion> shootingRegions = TextureAtlasChiches.getRegions(atlas, "shooting", "-", 0);
		Array<AtlasRegion> runningRegions = TextureAtlasChiches.getRegions(atlas, "running", "-", 0);
		Array<AtlasRegion> runningShootingRegions = TextureAtlasChiches.getRegions(atlas, "runningShooting", "-", 0);

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
		
		// the galaxy background
		background = new Texture(Gdx.files.internal("galaxy.png"));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// the bird :F
		birdman = new Texture(Gdx.files.internal("birdman.png"));
		birdman.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		birdmanWidth = birdman.getWidth()/3;
		birdmanHeight = birdman.getHeight()/3;
		
		
		// create the floor (custom game object)
		float floorHeight = BunnehStormGame.V_HEIGHT / 14f; // 1/14 of the screen cuz fuck u
		moon = new Texture(Gdx.files.internal("moon.png"));
		moon.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		floor = new Floor(new TextureRegion(moon), new Rectangle(
			-BunnehStormGame.V_WIDTH/2, -BunnehStormGame.V_HEIGHT/2,
			BunnehStormGame.V_WIDTH, floorHeight));
		
		// create the player (custom game object)
		float playerWidth = 15f;
		float playerHeight = 35f;
		float playerYoffset = floor.getRect().getHeight()/0.9f;
		Rectangle playerRect = new Rectangle(
				-playerWidth/2f, floor.getRect().y+playerYoffset, 
				playerWidth, playerHeight);
		float spriteWidth = playerRect.width*2.8f;
		float spriteHeight = playerRect.height*1.8f;
		float offsetX = playerRect.width/1.1f;
		float offsetY = playerRect.height/3.6f;
		Player player = new Player(idleRegions, runningRegions, runningShootingRegions, shootingRegions, 
				playerRect, offsetX, offsetY);
		player.setSpriteSize(spriteWidth, spriteHeight);
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
		//Gdx.gl.glClearColor(backColor.r, backColor.g, backColor.b, backColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);

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
		camera.update();
		//game.goHandler.render(batch, camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background, -(BunnehStormGame.V_WIDTH/2)-9, -BunnehStormGame.V_HEIGHT/2, 
				BunnehStormGame.V_WIDTH+18, BunnehStormGame.V_HEIGHT);
		batch.draw(birdman, -(BunnehStormGame.V_WIDTH/2)-8, -(BunnehStormGame.V_HEIGHT/2)+24,
				birdmanWidth, birdmanHeight);
		floor.render(batch);
		game.levelHandler.getPlayer().render(batch);
		game.goHandler.render(batch, camera);
		int currentLevel = game.levelHandler.getCurrentLevel().getLvlNumber();
		font.draw(batch, "Level : " + currentLevel, 
				-BunnehStormGame.V_WIDTH/2, BunnehStormGame.V_HEIGHT/2);
		hud.render(batch);
		batch.end();
		if(game.debugRender) {
			game.goHandler.debugRender(debugRender, camera, game.levelHandler.getPlayer(), floor);
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			//int totalObjects = game.goHandler.getObjectsSize();
			//totalObjects += player != null ? 1 : 0;
			//totalObjects += floor != null ? 1 : 0;
			batch.end();
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
		if(moon != null) moon.dispose();
		if(birdman != null) birdman.dispose();
		if(debugRender != null) debugRender.dispose();
		if(font != null) font.dispose();
		batch.dispose();
		atlas.dispose();
		explosionAtlas.dispose();
		Gdx.input.setInputProcessor(null);
	}
	
	public static void setGameOver(boolean over) {
		gameOver = over;
	}

}
