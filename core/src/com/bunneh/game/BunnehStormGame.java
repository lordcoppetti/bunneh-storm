package com.bunneh.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.bunneh.game.handlers.CollisionHandler;
import com.bunneh.game.handlers.GameObjectsHandler;
import com.bunneh.game.handlers.LevelHandler;
import com.bunneh.game.screens.GameOverScreen;
import com.bunneh.game.screens.MainMenuScreen;
import com.bunneh.game.screens.PlayScreen;

/*
 * DONE: GameObjectsHandler, CollisionHandler
 * DONE: EnemySpawner (real enemies that fire towards the player)
 * TODO: TextObject: GameObject with a bitmap font that has flexibility to move, fade, change color, etc (tween)
 * TODO: Refactor Spawners in general do something easier and more flexible to maintain, also with better options
 * Spent total hours: 10
 */
public class BunnehStormGame extends Game {
	
	public static BunnehStormGame game;
	
	public static final String TITLE = "Bunneh Storm", VERSION = "0.0.1-alpha";
	public static final float V_WIDTH = 300f;
	public static final float V_HEIGHT = 300f;
	
	public boolean debugRender = false;
	
	// handlers
	public GameObjectsHandler goHandler;
	public CollisionHandler collisionHandler;
	public LevelHandler levelHandler;

	@Override
	public void create() {
		initializeGame();
	}
	
	public void initializeGame() {
		game = this;
		goHandler = new GameObjectsHandler();
		collisionHandler = new CollisionHandler();
		levelHandler = new LevelHandler();
		setScreen(getNewPlayScreen());
	}

	public Screen getMainMenuScreen() {
		return new MainMenuScreen();
	}

	public Screen getNewPlayScreen() {
		return new PlayScreen(game);
	}
	
	public Screen getGameOverScreen() {
		return new GameOverScreen();
	}
	
}
