package com.bunneh.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Floor extends GameObject {
	
	private Rectangle rect;
	private TextureRegion textureRegion;

	public Floor(TextureRegion region, Rectangle floorRect) {
		this.textureRegion = region;
		this.rect = floorRect;
	}
	public Floor(Rectangle floorRect) {
		this.rect = floorRect;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, rect.x-9, rect.y, rect.width+18, rect.height+14);
	}

	@Override
	public void render(SpriteBatch batch, float alphaModulation) {
		batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, alphaModulation);
		batch.draw(textureRegion, rect.x-9, rect.y, rect.width+18, rect.height+14);

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Rectangle getRect() {
		return this.rect;
	}

	@Override
	public boolean collided(GameObject target) {
		// TODO Auto-generated method stub
		return false;
	}

}
