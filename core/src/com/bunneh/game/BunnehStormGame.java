package com.bunneh.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.bunneh.game.handlers.CollisionHandler;
import com.bunneh.game.handlers.GameObjectsHandler;
import com.bunneh.game.screens.GameOverScreen;
import com.bunneh.game.screens.MainMenuScreen;
import com.bunneh.game.screens.PlayScreen;

public class BunnehStormGame extends Game {
	
	public static final String TITLE = "Bunneh Storm", VERSION = "0.0.1-alpha";
	public static final float V_WIDTH = 300f;
	public static final float V_HEIGHT = 300f;
	
	public boolean debugRender = true;
	
	// handlers
	public GameObjectsHandler goHandler;
	public CollisionHandler collisionHandler;

	@Override
	public void create() {
		goHandler = new GameObjectsHandler();
		collisionHandler = new CollisionHandler();
		setScreen(getPlayScreen());
	}

	public Screen getMainMenuScreen() {
		return new MainMenuScreen();
	}

	public Screen getPlayScreen() {
		return new PlayScreen(this);
	}
	
	public Screen getGameOverScreen() {
		return new GameOverScreen();
	}
	
}
