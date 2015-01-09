package com.bunneh.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.utils.MathChiches;

public class SniperEnemy extends Enemy {
	
	private Vector2 velocity = new Vector2(0, 0);
	private float speed;

	private float bulletTimer = 0f;
	private float bulletInterval = 2f;
	private float bulletSpeed = 1.5f;
	private int bulletPower = 1;

	private GameObject target;
	
	public SniperEnemy(float x, float y, float width, float height, float enemySpeed) {
		super(new Rectangle(x, y, width, height));
		this.speed = enemySpeed;
		this.health = 15;
	}

	@Override
	public void update(float delta) {
		updatePosition();
		fire(delta);
	}

	private void fire(float delta) {
		if(target == null) return;
		bulletTimer += delta;
		if(bulletTimer >= bulletInterval) {
			Rectangle targetRect = target.getRect();
			float angle = (float) MathChiches.getAngle(rect.x, rect.y, (targetRect.x+targetRect.width/2), (targetRect.y+targetRect.height/2));
			Bullet b = new Bullet(bulletPower, rect.x, rect.y, angle, bulletSpeed);
			b.setAllyBullet(false);
			BunnehStormGame game = (BunnehStormGame) Gdx.app.getApplicationListener();
			game.goHandler.addEnemyBullet(b);
			bulletTimer = 0f;
		}
	}

	private void updatePosition() {
		velocity.y = -speed;
		this.rect.y += velocity.y;
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	public void setBulletSpeed(float speed) {
		this.bulletSpeed = speed;
	}

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(SpriteBatch batch, float alphaModulation) {
		// TODO Auto-generated method stub
		
	}
	
	public void setSize(float width, float height) {
		this.rect.width = width;
		this.rect.height = height;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean collided(GameObject target) {
		if(target instanceof Floor) {
			destroy = true;
			return true;
		}
		if(target instanceof Bullet) {
			Bullet b = (Bullet) target;
			if(!b.isAllyBullet()) return false;
			health -= b.getAttackPower();
			if(health <= 0) {
				destroy = true;
			}
			return true;
		}
		return false;
	}
	
	public void setBulletInterval(float interval) {
		this.bulletInterval = interval;
	}

}
