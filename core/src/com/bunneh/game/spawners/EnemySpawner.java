package com.bunneh.game.spawners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.objects.Crusher;
import com.bunneh.game.objects.GameObject;
import com.bunneh.game.objects.SniperEnemy;
import com.bunneh.game.screens.PlayScreen;

public class EnemySpawner {

	public enum EnemyType {
		Obstacle,
		Sniper,
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
	private float bulletInterval = 0.2f;
	private float bulletSpeed = 4f;

	private GameObject enemyTarget = null;
	private float distanceToTargetLimit = BunnehStormGame.V_HEIGHT/3.5f;
	private float fireLimitOnY;
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
	
	// watermelon explosion regions
	private Array<AtlasRegion> explosionRegions;
	
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
		float x = MathUtils.random(leftBoundary, rightBoundary+(enemySize.x*2));
		float y = defaultTopBoundary;
		if(bottomBoundary != 0 && topBoundary != 0) y = MathUtils.random(bottomBoundary, topBoundary);
		y = y + enemySize.y;
		BunnehStormGame game = (BunnehStormGame) Gdx.app.getApplicationListener();
		switch (type) {
		case Obstacle:
			Sprite cSprite = new Sprite(PlayScreen.atlas.findRegion("evilWatermelon"));
			Crusher c = new Crusher(cSprite, game.levelHandler.explosionRegions, new Rectangle(x, y, enemySize.x, enemySize.y), enemySpeed);
			c.setHealth(enemyHealth);
			if(enemyTarget != null) {
				cSprite.setColor(Color.YELLOW);
				c.setTarget(enemyTarget);
				c.setFollowTarget(enemyFollowTarget);
				c.setFollowTargetLimit(distanceToTargetLimit);
			}
			game.goHandler.addEnemy(c);
			break;
		case Sniper:
			Sprite sSprite = new Sprite(PlayScreen.atlas.findRegion("evilWatermelon"));
			SniperEnemy s = new SniperEnemy(sSprite, game.levelHandler.explosionRegions, x, y, enemySize.x, enemySize.y, enemySpeed);
			s.setBulletInterval(bulletInterval);
			s.setBulletSpeed(bulletSpeed);
			s.setTarget(enemyTarget);
			s.setFireLimitOnY(fireLimitOnY);
			game.goHandler.addEnemy(s);
			break;
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

	public float getBulletInterval() {
		return bulletInterval;
	}

	public void setBulletInterval(float bulletInterval) {
		this.bulletInterval = bulletInterval;
	}

	public float getBulletSpeed() {
		return bulletSpeed;
	}

	public void setBulletSpeed(float bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}
	
	public boolean isDone() {
		return done;
	}

	public void setFireLimitOnY(float yLimit) {
		this.fireLimitOnY = yLimit;
		
	}
	
	public void setEnemyExplosionAnimation(Array<AtlasRegion> regions) {
		this.explosionRegions = regions;
	}

}
