package com.bunneh.game.screens;

import static com.bunneh.game.BunnehStormGame.V_HEIGHT;
import static com.bunneh.game.BunnehStormGame.V_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bunneh.game.BunnehStormGame;

public class MainMenuScreen implements Screen {
	
	private BunnehStormGame game;
	private Color backColor = new Color(0f, 0f, 0f, 1f);
	
	private SpriteBatch batch;

	private BitmapFont font;
	private Texture gameLogoTexture;
	
	// Vectors and positions
	private Vector2 center = new Vector2(V_WIDTH, V_HEIGHT);
	
	public MainMenuScreen(BunnehStormGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		font = new BitmapFont();
		font.scale(2f);
		gameLogoTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
		batch = new SpriteBatch();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(backColor.r, backColor.g, backColor.b, backColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(gameLogoTexture, center.x-gameLogoTexture.getWidth()/2,
				center.y-gameLogoTexture.getHeight()/2);
		batch.end();
	}
		

	@Override
	public void resize(int width, int height) {
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

	@Override
	public void dispose() {
		font.dispose();
		gameLogoTexture.dispose();
	}

}
