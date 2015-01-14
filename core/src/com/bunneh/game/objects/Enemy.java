package com.bunneh.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Enemy extends GameObject {

	protected static final float FRAME_DURATION = 1.0f / 30.0f;
	
	protected Rectangle rect;
	
	protected int health = 20;
	
	public Enemy(Rectangle rect) {
		this.rect = rect;
	}

	@Override
	public abstract void update(float delta);

	@Override
	public abstract void render(SpriteBatch batch);

	@Override
	public abstract void render(SpriteBatch batch, float alphaModulation);

	@Override
	public abstract void dispose();

	@Override
	public Rectangle getRect() {
		return this.rect;
	}

	@Override
	public abstract boolean collided(GameObject target);

}
