package com.bunneh.game.screens;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.objects.EnemySpawner;
import com.bunneh.game.objects.Rock;

public class RockSpawner implements EnemySpawner {
	
	private float leftBoundary;
	private float rightBoundary;
	private float timer = 0f;
	private float originalSpawnInterval;
	private float spawnInterval; // In seconds example: 2f = 2 secs
	private float fallSpeed = 1f;
	private float width = 10f;
	private float height = 10f;

	// Modifier values
	private boolean increaseSpawnInterval = false;
	private float spawnIntervalIncrement = 0f;
	private float spawnIntervalMin = 0.1f;

	private boolean incrementFallSpeed = false;
	private float fallSpeedIncrement = 0f;
	private float fallSpeedMax = 10f;

	Random rand;

	public RockSpawner(float leftBoundary, float rightBoundary, float spawnInterval) {
		this.leftBoundary = leftBoundary;
		this.rightBoundary = rightBoundary;
		this.spawnInterval = spawnInterval;
		this.timer = spawnInterval;
		this.originalSpawnInterval = spawnInterval;
		rand = new Random();
	}

	public void setFallSpeed(float fallVelocity) {
		this.fallSpeed = fallVelocity;
	}
	
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void update(float delta) {
		timer += delta;
		if(timer >= spawnInterval) {
			spawnRock();
		}
	}

	private void spawnRock() {
		timer = 0;
		float x = MathUtils.random(leftBoundary, rightBoundary);
		float y = BunnehStormGame.V_HEIGHT/2;
		Rock rock = new Rock(new Rectangle(x, y, width, height), new Vector2(0, -fallSpeed));
		PlayScreen.gameObjects.add(rock);
		if(increaseSpawnInterval) {
			float newSpawnInterval = spawnInterval - spawnIntervalIncrement;
			if(newSpawnInterval >= spawnIntervalMin) {
				spawnInterval = newSpawnInterval;
			} else {
				if(incrementFallSpeed) {
					float newFallSpeed = fallSpeed += fallSpeedIncrement;
					if(newFallSpeed <= fallSpeedMax) {
						fallSpeed += fallSpeedIncrement;
					} 
				}
			}
		} else {
			spawnInterval = originalSpawnInterval;
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	// Allows below functionality to work or not
	public void setIncreaseSpawnInterval(boolean increaseSpawnInterval) {
		this.increaseSpawnInterval = increaseSpawnInterval;
	}

	// Increments spawn time after each spawn by a given interval
	public void setSpawnIntervalIncrement(float spawnIntervalIncrement) {
		this.spawnIntervalIncrement = spawnIntervalIncrement;
	}

	public void setSpawnIntervalMin(float spawnIntervalMin) {
		this.spawnIntervalMin = spawnIntervalMin;
	}

	public void setIncrementFallSpeed(boolean incrementFallSpeed) {
		this.incrementFallSpeed = true;
	}

	public void setFallSpeedIncrement(float fallSpeedIncrement) {
		this.fallSpeedIncrement = fallSpeedIncrement;
	}

	public void setFallSpeedMax(float fallSpeedMax) {
		this.fallSpeedMax = fallSpeedMax;
	}


}
