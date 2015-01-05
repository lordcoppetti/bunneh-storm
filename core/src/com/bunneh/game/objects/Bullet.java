package com.bunneh.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bunneh.game.BunnehStormGame;

public class Bullet extends GameObject {
	
	private float x, y;
	private float bulletWidth = 2f;
	private float bulletHeight = 4f;
	
	private Rectangle rect;
	
	private Vector2 velocity = new Vector2(0, 0);
	private float bulletSpeed = 4f;

	public Bullet(float x, float y) {
		this.x = x;
		this.y = y;
		rect = new Rectangle(x-bulletWidth/2, y, bulletWidth, bulletHeight);
	}

	@Override
	public void update() {
		checkOffScreenDestroy();
		if(needsDestroy()) return;
		updatePosition();
	}

	private void updatePosition() {
		velocity.y = bulletSpeed;
		rect.y += velocity.y;
	}

	private void checkOffScreenDestroy() {
		if(rect.y > BunnehStormGame.V_HEIGHT/2) {
			destroy = true;
		}
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
	}

	@Override
	public Rectangle getRect() {
		return this.rect;
	}

}
