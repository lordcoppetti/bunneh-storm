package com.bunneh.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Floor extends GameObject {
	
	private Rectangle rect;

	public Floor(Rectangle floorRect) {
		this.rect = floorRect;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(SpriteBatch batch, float alphaModulation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Rectangle getRect() {
		return this.rect;
	}

}
