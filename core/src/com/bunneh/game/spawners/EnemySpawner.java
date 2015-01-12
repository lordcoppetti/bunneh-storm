package com.bunneh.game.spawners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.objects.Crusher;
import com.bunneh.game.objects.GameObject;

public class EnemySpawner {

	public enum EnemyType {
		Obstacle,
		Shooter,
	}

	private static float defaultLeftBoundary = (-BunnehStormGame.V_WIDTH/2);
	private static float defaultRightBoundary = (BunnehStormGame.V_WIDTH/3);
	private static float defaultTopBoundary = BunnehStormGame.V_HEIGHT/2;
	private static float defaultEnemySpeed = 2f;

	private EnemyType type;
	
	// enemy stats
	private int enemyHealth;
	private float enemySpeed;
	private Vector2 enemySize;

	private GameObject enemyTarget = null;
	private float distanceToTargetLimit = BunnehStormGame.V_HEIGHT/3.5f;
	private boolean enemyFollowTarget;
	
	// spawner control
	private float timer;
	private float globalTimer;
	private float timeLimit = 0;
	private float spawnInterval;
	private float leftBoundary;
	private float rightBoundary;
	private float bottomBoundary;
	private float topBoundary;
	private boolean done = false;
	
	public EnemySpawner(EnemyType type, float spawnInterval, Vector2 enemySize) {
		this(type, spawnInterval, defaultLeftBoundary, defaultRightBoundary, 
				enemySize, 15, defaultEnemySpeed);
	}

	public EnemySpawner(EnemyType type, float spawnInterval, float leftBoundary, float rightBoundary, 
			Vector2 enemySize, int enemyHealth, float enemySpeed) {
		this.setType(type);
		this.setSpawnInterval(spawnInterval);
		this.setEnemyHealth(enemyHealth);
		this.setEnemySpeed(enemySpeed);
		this.setLeftBoundary(leftBoundary);
		this.setRightBoundary(rightBoundary);
		this.setEnemySize(enemySize);
	}
	
	public void update(float delta) {
		if(isDone()) return;
		timer += delta;
		if(timeLimit > 0) globalTimer += delta;
		if(timeLimit > 0 && globalTimer >= timeLimit) {
			dispose();
			return;
		}
		if(timer >= spawnInterval) {
			spawnEnemy();
			timer = 0;
		}
	}

	private void spawnEnemy() {
		float x = MathUtils.random(leftBoundary+enemySize.x, rightBoundary-enemySize.x);
		float y = defaultTopBoundary;
		if(bottomBoundary != 0 && topBoundary != 0) y = MathUtils.random(bottomBoundary, topBoundary);
		y = y + enemySize.y;
		BunnehStormGame game = (BunnehStormGame) Gdx.app.getApplicationListener();
		switch (type) {
		case Obstacle:
			Crusher c = new Crusher(new Rectangle(x, y, enemySize.x, enemySize.y), enemySpeed);
			c.setHealth(enemyHealth);
			if(enemyTarget != null) {
				c.setTarget(enemyTarget);
				c.setFollowTarget(enemyFollowTarget);
				c.setFollowTargetLimit(distanceToTargetLimit);
			}
			game.goHandler.addEnemy(c);
			break;
		case Shooter:

		default:
			break;
		}
		
	}

	public void restart() {
	}

	public void dispose() {
		done = true;
	}

	public EnemyType getType() {
		return type;
	}

	public void setType(EnemyType type) {
		this.type = type;
	}

	public int getEnemyHealth() {
		return enemyHealth;
	}

	public void setEnemyHealth(int enemyHealth) {
		this.enemyHealth = enemyHealth;
	}

	public float getEnemySpeed() {
		return enemySpeed;
	}

	public void setEnemySpeed(float enemySpeed) {
		this.enemySpeed = enemySpeed;
	}

	public GameObject getEnemyTarget() {
		return enemyTarget;
	}

	public void setEnemyTarget(GameObject enemyTarget) {
		this.enemyTarget = enemyTarget;
	}
	
	public void setFollowTarget(boolean follow) {
		this.enemyFollowTarget = follow;
	}
	
	public void setDistanceToTargetLimit(float limit) {
		this.distanceToTargetLimit = limit;
	}
	
	public float getSpawnInterval() {
		return spawnInterval;
	}

	public void setSpawnInterval(float spawnInterval) {
		this.spawnInterval = spawnInterval;
	}

	public float getLeftBoundary() {
		return leftBoundary;
	}

	public void setLeftBoundary(float leftBoundary) {
		this.leftBoundary = leftBoundary;
	}

	public float getRightBoundary() {
		return rightBoundary;
	}

	public void setRightBoundary(float rightBoundary) {
		this.rightBoundary = rightBoundary;
	}

	public Vector2 getEnemySize() {
		return enemySize;
	}

	public void setEnemySize(Vector2 enemySize) {
		this.enemySize = enemySize;
	}

	public void setTimeLimit(float timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	public boolean isDone() {
		return done;
	}

}
