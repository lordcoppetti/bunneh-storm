package com.bunneh.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.utils.MathChiches;

public class Bullet extends GameObject {
	
	private float bulletWidth = 2f;
	private float bulletHeight = 4f;
	private int attackPower = 5;
	private float angle;
	private boolean isAllyBullet = true;
	
	private Rectangle rect;
	
	private Vector2 velocity = new Vector2(0, 0);
	private float bulletSpeed;
	
	public Bullet(int attackPower, float x, float y, float angle, float bulletSpeed) {
		this.attackPower = attackPower;
		rect = new Rectangle(x-bulletWidth/2, y, bulletWidth, bulletHeight);
		this.angle = angle;
		this.velocity = MathChiches.getAngleBasedMovement(angle, bulletSpeed);
	}

	@Override
	public void update(float delta) {
		if(needsDestroy()) return;
		if(checkOffScreenDestroy()) return;;
		updatePosition();
	}

	private void updatePosition() {
		rect.x += velocity.x;
		rect.y += velocity.y;
	}

	private boolean checkOffScreenDestroy() {
		if(rect.y > BunnehStormGame.V_HEIGHT/2) {
			destroy = true;
			return true;
		} else if(rect.y < -BunnehStormGame.V_HEIGHT/2) {
			destroy = true;
			return true;
		} else if(rect.x > BunnehStormGame.V_HEIGHT/2) {
			destroy = true;
			return true;
		} else if(rect.x < -BunnehStormGame.V_HEIGHT/2) {
			destroy = true;
			return true;
		}
		return false;
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

	@Override
	public boolean collided(GameObject target) {
		if(target instanceof Enemy) {
			destroy = true;
			return true;
		}
		if(target instanceof Floor) {
			destroy = true;
			return true;
		}
		return false;
	}

	public int getAttackPower() {
		return 5;
	}

	public void setAttackPower(int attackPower) {
		this.attackPower = attackPower;
	}

	public boolean isAllyBullet() {
		return isAllyBullet;
	}

	public void setAllyBullet(boolean isAllyBullet) {
		this.isAllyBullet = isAllyBullet;
	}

}
