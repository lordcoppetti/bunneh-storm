package com.bunneh.game.spawners;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.objects.Crusher;
import com.bunneh.game.objects.GameObject;

public class CrusherSpawner implements ObjectSpawner {
	
	private float leftBoundary;
	private float rightBoundary;
	private float timer = 0f;
	private float originalSpawnInterval;
	private float spawnInterval; // In seconds example: 2f = 2 secs
	private float fallSpeed = 1f;
	private float width = 10f;
	private float height = 10f;
	

	// Modifier values
	private GameObject target = null;
	private boolean followTarget = false;
	private float targetDistance = 0f;

	private boolean increaseSpawnInterval = false;
	private float spawnIntervalIncrement = 0f;
	private float spawnIntervalMin = 0.1f;

	private boolean incrementFallSpeed = false;
	private float fallSpeedIncrement = 0f;
	private float fallSpeedMax = 10f;

	Random rand;

	public CrusherSpawner(float leftBoundary, float rightBoundary, float spawnInterval) {
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
			spawnCrusher();
		}
	}

	private void spawnCrusher() {
		timer = 0;
		float x = MathUtils.random(leftBoundary, rightBoundary);
		float y = BunnehStormGame.V_HEIGHT/2;
		BunnehStormGame game = (BunnehStormGame) Gdx.app.getApplicationListener();
		Crusher c = new Crusher(new Rectangle(x, y, width, height), fallSpeed);
		if(target != null) {
			c.setTarget(target);
			c.setFollowTarget(followTarget);
			if(targetDistance > 0) c.setFollowTargetLimit(targetDistance);
				
		}
		game.goHandler.addObstacle(c);
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

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}

	public void setTarget(GameObject target) {
		this.target = target;
	}

	public void setFollowTarget(boolean followTarget) {
		this.followTarget = followTarget;
	}

	public void setFollowTargetLimit(float distance) {
		this.targetDistance = distance;
	}


}
