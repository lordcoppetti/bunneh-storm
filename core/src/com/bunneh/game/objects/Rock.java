package com.bunneh.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Rock extends Enemy {
	
	private Vector2 velocity;

	public Rock(Rectangle rect, Vector2 velocity) {
		super(rect);
		this.health = 20;
		this.velocity = velocity;
	}

	@Override
	public void update() {
		updatePosition();
	}

	private void updatePosition() {
		rect.y += velocity.y;
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
	public boolean collided(GameObject target) {
		if(target instanceof Bullet) {
			health -= ((Bullet) target).getAttackPower();
			if(health <= 0) {
				destroy = true;
			}
			return true;
		}
		if(target instanceof Floor) {
			destroy = true;
			return true;
		}
		return false;
	}

}
