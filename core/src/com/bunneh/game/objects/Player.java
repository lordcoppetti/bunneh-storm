package com.bunneh.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bunneh.game.screens.PlayScreen;

public class Player extends GameObject {
	
	// For now we assume the rect is the physical representation,
	// since we're not using any sprite yet.
	private Rectangle rect;

	private float rightBoundary;
	private float leftBoundary;

	private Vector2 velocity = new Vector2(0, 0);
	private float movSpeed = 2f;
	
	private int lives = 3;
	private int attackPower = 5;
	
	// Control stuff for firing
	private boolean fireRequested = false;
	private float fireDelayMs = 0.3f;
	private float fireCounter = fireDelayMs;
	
	private boolean movingLeft;
	private boolean movingRight;

	
	public Player(Rectangle rect) {
		this.rect = rect;
	}

	@Override
	public void update() {
		if(needsDestroy()) return;
		updatePosition();
		updateFireRequest();
	}


	private void updatePosition() {
		velocity.x = 0;
		if(movingLeft) velocity.x = -movSpeed;
		if(movingRight) velocity.x = movSpeed;

		float newX = rect.x += velocity.x;

		if(newX <= leftBoundary) {
			newX = leftBoundary;
		}
		if(newX >= rightBoundary) {
			newX = rightBoundary;
		}

		rect.x = newX;
	}

	private void updateFireRequest() {
		fireCounter += Gdx.graphics.getDeltaTime();
		if(!fireRequested) return;
		if(fireCounter >= fireDelayMs) {
			fire();
			fireCounter = 0f;
		}
	}

	private void fire() {
		fireRequested = false;
		// TODO Fire fucking bullets!
		Bullet newBullet = new Bullet(attackPower, rect.x + (rect.width/2), rect.y + rect.height);
		PlayScreen.gameObjects.add(newBullet);
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
		if(target instanceof Rock) {
			lives -= 1;
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
