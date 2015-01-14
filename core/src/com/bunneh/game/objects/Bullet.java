package com.bunneh.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
	private Sprite sprite;
	
	public Bullet(Sprite sprite, int attackPower, float x, float y, float angle, float bulletSpeed, float width, float height) {
		this.attackPower = attackPower;
		this.bulletWidth = width;
		this.bulletHeight = height;
		rect = new Rectangle(x-bulletWidth/2, y, bulletWidth, bulletHeight);
		this.angle = angle;
		this.velocity = MathChiches.getAngleBasedMovement(angle, bulletSpeed);
		this.sprite = sprite;
		sprite.setOrigin(rect.width/2, rect.height/2);
		sprite.setBounds(rect.x, rect.y, rect.width, rect.height);
		sprite.flip(false, true);
		float spriteAngle = angle*2;
		// TODO: Fix this fucking angle
		sprite.setRotation(spriteAngle);
	}

	@Override
	public void update(float delta) {
		if(needsDestroy()) return;
		if(checkOffScreenDestroy()) return;;
		updatePosition();
		sprite.setX(rect.x);
		sprite.setY(rect.y);
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
		if(sprite == null) return;
		sprite.draw(batch);

	}

	@Override
	public void render(SpriteBatch batch, float alphaModulation) {
		if(sprite == null) return;
		sprite.draw(batch, alphaModulation);

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
	
	public void setBulletSize(float width, float height) {
		this.bulletWidth = width;
		this.bulletHeight = height;
	}

}
