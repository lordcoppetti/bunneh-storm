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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.InputAdapter;
import com.bunneh.game.objects.EnemySpawnerContainer;
import com.bunneh.game.objects.Floor;
import com.bunneh.game.objects.GameObject;
import com.bunneh.game.objects.Hud;
import com.bunneh.game.objects.Player;

/*
 * This PlayScreen doesn't use box2d or any physics engine.
 * It doesn't even use a Stage to manage game objects.
 * Also does collisions by rectangle overlap.
 */

/*
 * TODO: Refactor RockSpawner into something easier and more flexible to maintain
 * TODO: HUD with basic data
 */
public class PlayScreen implements Screen {
	
	public static Array<GameObject> gameObjects;

	private static boolean gameOver = false;

	private final float timestep = 1 / 60f;
	private float timeAccum = 0f;

	private BunnehStormGame game;
	private Color backColor = new Color(0f, 0f, 0f, 1f);
	
	private Texture background;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer debugRender;
	private BitmapFont font; private Player player;
	private EnemySpawnerContainer esc;
	private Hud hud;
	
	public PlayScreen(BunnehStormGame game) {
		gameOver = false;
		this.game = game;
	}

	@Override
	public void show() {
		gameObjects = new Array<GameObject>();
		camera = new OrthographicCamera(game.V_WIDTH, game.V_HEIGHT);
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setScale(0.8f);
		hud = new Hud(font);
		hud.setPlayerLivesPos(new Vector2(-game.V_WIDTH/2.2f, -game.V_HEIGHT/2.2f));
		hud.setPlayerAtkPowPos(new Vector2(game.V_WIDTH/3.5f, hud.getPlayerLivesPos().y));
		
		if(game.debugRender) debugRender = new ShapeRenderer();
		
		// Create the floor
		float floorHeight = game.V_HEIGHT / 4f; // 1/4 of the screen
		Floor floor = new Floor(new Rectangle(
				-game.V_WIDTH/2, -game.V_HEIGHT/2,
				game.V_WIDTH, floorHeight));
		
		// Create the player
		float playerWidth = 10f;
		float playerHeight = 15f;
		float playerYoffset = 3f;
		Rectangle playerRect = new Rectangle(
				-playerWidth/2f, -floorHeight-playerYoffset, 
				playerWidth, playerHeight);
		player = new Player(playerRect);
		player.setXboundaries(-game.V_WIDTH/2, (game.V_WIDTH/2)-playerRect.width);
		hud.setPlayer(player);
		
		// Create the spawn points for enemies/obstacles
		 esc = new EnemySpawnerContainer();
		RockSpawner rs = new RockSpawner(-BunnehStormGame.V_WIDTH/2, (BunnehStormGame.V_WIDTH/2)-5f, 4f);
		rs.setFallSpeed(0.6f);
		rs.setSize(10f, 10f);
		rs.setIncreaseSpawnInterval(true);
		rs.setSpawnIntervalIncrement(0.08f);
		rs.setSpawnIntervalMin(0.5f);
		rs.setIncrementFallSpeed(true);
		rs.setFallSpeedIncrement(0.08f);
		rs.setFallSpeedMax(5f);
		esc.addEnemySpawner(rs);

		// Create input multiplexer
		createInput();

		gameObjects.add(floor);
		gameObjects.add(player);
	}

	private void createInput() {
		Gdx.input.setInputProcessor(new InputMultiplexer(
				new InputAdapter() {
					@Override
					public boolean keyDown(int keycode) {
						//if(keycode == Keys.A) player.setState(Player.MovementState.MovingLeft);
						//if(keycode == Keys.D) player.setState(Player.MovementState.MovingRight);
						if(keycode == Keys.A) player.moveLeft(true);
						if(keycode == Keys.D) player.moveRight(true);
						if(keycode == Keys.SPACE) player.requestFire();
						return true;
					}
					@Override
					public boolean keyUp(int keycode) {
						//if(keycode == Keys.A && !Gdx.input.isKeyPressed(Keys.D)) player.setState(Player.MovementState.Idle);
						//if(keycode == Keys.D && !Gdx.input.isKeyPressed(Keys.A)) player.setState(Player.MovementState.Idle);
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
			for(int i = 0 ; i < gameObjects.size ; i++) {
				GameObject go = gameObjects.get(i);
				if(go.needsDestroy()) {
					gameObjects.removeIndex(i);
					go.dispose();
					continue;
				}

				// TODO Check collisions
				checkCollisions(go, i);

				// Update
				go.update();
			}
			timeAccum -= timestep;
		}
		
		// Interpolate game objects if physics engine is used!
		
		// render objects
		if(game.debugRender) {
			debugRender.setProjectionMatrix(camera.combined);
			debugRender.begin(ShapeType.Line);
			debugRender.setColor(Color.GREEN);
			for(GameObject go : gameObjects) {
				Rectangle rect = go.getRect();
				debugRender.rect(rect.x, rect.y, rect.width, rect.height);
			}
			debugRender.end();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			font.draw(batch, "Screen objects: " + gameObjects.size, -game.V_WIDTH/2, game.V_HEIGHT/2);
			hud.render(batch);
			batch.end();
			
		} else {
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			for(GameObject go : gameObjects) {
				go.render(batch);
			}
			batch.end();
		}
		
	}



	private void checkCollisions(GameObject go, int index) {
		for(int j = 0 ; j < gameObjects.size ; j++) {
			if(j == index) continue;
			if(go instanceof Floor) continue;
			// Check rectangle overlap
			GameObject target = gameObjects.get(j);
			Rectangle targetRect = target.getRect();
			if(targetRect.overlaps(go.getRect())) {
				// Collision
				go.collided(target);
				target.collided(go);
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		// Somehow keep aspect ratio...
		float aspectRatio = (float) width / (float) height;
		camera = new OrthographicCamera(game.V_WIDTH * aspectRatio, game.V_HEIGHT);
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
		if(gameObjects.size > 0) {
			for(GameObject go : gameObjects) {
				go.dispose();
			}
		}
		if(background != null) background.dispose();
		if(debugRender != null) debugRender.dispose();
		if(font != null) font.dispose();
		gameObjects.clear();
		batch.dispose();
		Gdx.input.setInputProcessor(null);
	}

	
	public static void setGameOver(boolean over) {
		gameOver = over;
	}

}
