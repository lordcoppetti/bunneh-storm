package com.bunneh.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.screens.PlayScreen;
import com.bunneh.game.utils.MathChiches;

public class Player extends GameObject {
	
	// For now we assume the rect is the physical representation,
	// since we're not using any sprite yet.
	private Rectangle rect;

	private float rightBoundary;
	private float leftBoundary;

	private Vector2 velocity = new Vector2(0, 0);
	private float movSpeed = 3f;
	private float lerpAlpha = 40f;
	
	private int lives = 3;
	private int attackPower = 5;
	private float bulletSpeed = 4f;
	
	// Control stuff for firing
	private boolean fireRequested = false;
	private float fireDelayMs = 0.3f;
	private float fireCounter = fireDelayMs;
	
	private boolean movingLeft;
	private boolean movingRight;

	private boolean invulnerable;
	private float invulnerableTimer = 0f;
	private float invulnerableLength = 1f; // 1 second

	
	public Player(Rectangle rect) {
		this.rect = rect;
	}

	@Override
	public void update(float delta) {
		if(needsDestroy()) return;
		if(invulnerable) {
			invulnerableTimer += delta;
			if(invulnerableTimer >= invulnerableLength) {
				invulnerable = false;
				invulnerableTimer = 0f;
			}
		}
		updatePosition(delta);
		updateFireRequest(delta);
	}


	private void updatePosition(float delta) {
		Vector2 velGoal = new Vector2(0, 0);
		if(movingLeft) velGoal.x = -movSpeed;
		if(movingRight) velGoal.x = movSpeed;
		
		velocity.x = MathChiches.approach(velGoal.x, velocity.x, delta*lerpAlpha);
		velocity.y = MathChiches.approach(velGoal.y, velocity.y, delta*lerpAlpha);

		rect.x += velocity.x;

		if(rect.x <= leftBoundary) {
			rect.x = leftBoundary;
			velocity.x = 0;
		}
		if(rect.x >= rightBoundary) {
			rect.x = rightBoundary;
			velocity.x = 0;
		}
	}
	
	private void updateFireRequest(float delta) {
		fireCounter += delta;
		if(!fireRequested) return;
		if(fireCounter >= fireDelayMs) {
			fire();
			fireCounter = 0f;
		}
	}

	private void fire() {
		fireRequested = false;
		Bullet newBullet = new Bullet(attackPower, rect.x + (rect.width/2), rect.y + rect.height, 90f, bulletSpeed);
		BunnehStormGame game = (BunnehStormGame) Gdx.app.getApplicationListener();
		game.goHandler.addPlayerBullet(newBullet);
	}

	public void requestFire() {
		fireRequested = true;
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
		return rect;
	}

	public void setXboundaries(float leftBoundary, float rightBoundary) {
		this.leftBoundary = leftBoundary;
		this.rightBoundary = rightBoundary;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void setMovementSpeed(float speed) {
		this.movSpeed = speed;
	}

	public int getAtkPow() {
		return this.attackPower;
	}

	@Override
	public boolean collided(GameObject target) {
		if(invulnerable) return false;
		if(target instanceof Enemy) {
			lives -= 1;
			invulnerable = true;
			target.destroy = true;
			if(lives < 0) {
				// cap it
				lives = 0;
				PlayScreen.setGameOver(true);
			}
			return true;
		}
		if(target instanceof Bullet) {
			Bullet b = (Bullet) target;
			if(b.isAllyBullet()) return false;
			lives -= 1;
			invulnerable = true;
			target.destroy = true;
			if(lives < 0) {
				// cap it
				lives = 0;
				PlayScreen.setGameOver(true);
			}
			return true;
		}
		return false;
	}

	public void moveLeft(boolean move) {
		this.movingLeft = move;
		if(move) {
			this.movingRight = false;
		}
	}

	public void moveRight(boolean move) {
		this.movingRight = move;
		if(move) {
			this.movingLeft = false;
		}
	}


}
