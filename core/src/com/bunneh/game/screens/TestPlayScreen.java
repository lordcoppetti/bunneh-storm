package com.bunneh.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.handlers.LevelHandler;

public class TestPlayScreen implements Screen {
	
	private LevelHandler levelHandler;

	@Override
	public void show() {
		BunnehStormGame game = (BunnehStormGame) Gdx.app.getApplicationListener();
		this.levelHandler = game.levelHandler;
		levelHandler.createLevels();
		levelHandler.createLevels();
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
