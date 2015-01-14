package com.bunneh.game.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.InputAdapter;
import com.bunneh.game.tween.BitmapFontAccessor;

public class GameOverScreen implements Screen {
	
	private BitmapFont font;
	private SpriteBatch batch;
	private Color backColor = new Color(0f, 0f, 0f, 1f);
	private OrthographicCamera camera = new OrthographicCamera(BunnehStormGame.V_WIDTH, BunnehStormGame.V_HEIGHT);
	private boolean spaceNeedsRelease;
	private boolean ready = false;
	
	private TweenManager tweenM;
	
	@Override
	public void show() {
		font = new BitmapFont();
		batch = new SpriteBatch();

		tweenM = new TweenManager();
		Tween.registerAccessor(BitmapFont.class, new BitmapFontAccessor());

		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			spaceNeedsRelease = true;
		}
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				if(keycode == Keys.SPACE && !spaceNeedsRelease && ready) {
					BunnehStormGame.game.initializeGame();
				}
				return false;
			}
			@Override
			public boolean keyUp(int keycode) {
				if(keycode == Keys.SPACE) {
					spaceNeedsRelease = false;
				}
				return false;
			}
		});
		
		// create tween animations
		Tween.set(font, BitmapFontAccessor.ALPHA).target(0).start(tweenM);
		Tween.to(font, BitmapFontAccessor.ALPHA, 1f).target(1f).setCallback(new TweenCallback() {
			@Override
			public void onEvent(int arg0, BaseTween<?> arg1) {
				ready = true;
			}
		}).start(tweenM);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(backColor.r, backColor.g, backColor.b, backColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tweenM.update(delta);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		font.draw(batch, "Game Over", -BunnehStormGame.V_WIDTH/8, BunnehStormGame.V_HEIGHT/3.5f);
		font.draw(batch, "press space to restart level", -BunnehStormGame.V_WIDTH/3.6f, 0);
		batch.end();
		
		if(Gdx.input.isKeyPressed(Keys.SPACE)) {
			
		}
	}

	@Override
	public void resize(int width, int height) {
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

	@Override
	public void dispose() {
		font.dispose();
		batch.dispose();
	}

}
