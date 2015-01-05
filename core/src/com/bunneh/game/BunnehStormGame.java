package com.bunneh.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.bunneh.game.screens.MainMenuScreen;
import com.bunneh.game.screens.PlayScreen;

public class BunnehStormGame extends Game {
	
	public static final String TITLE = "Bunneh Storm", VERSION = "0.0.1-alpha";
	public static final float V_WIDTH = 320f;
	public static final float V_HEIGHT = 240f;
	
	public boolean debugRender = true;

	@Override
	public void create() {
		setScreen(getPlayScreen());
	}

	public Screen getMainMenuScreen() {
		return new MainMenuScreen(this);
	}

	public Screen getPlayScreen() {
		return new PlayScreen(this);
	}
	
}
