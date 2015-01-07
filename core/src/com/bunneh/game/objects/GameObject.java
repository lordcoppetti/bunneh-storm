package com.bunneh.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject {
	
	protected boolean destroy = false;

	public abstract void update(float delta);
	
	public abstract void render(SpriteBatch batch);

	public abstract void render(SpriteBatch batch, float alphaModulation);
	
	public boolean needsDestroy() {
		return this.destroy;
	}

	public abstract void dispose();
	
	public abstract Rectangle getRect();
	
	public abstract boolean collided(GameObject target);
	
}
