package com.bunneh.game.spawners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.objects.GameObject;
import com.bunneh.game.objects.SniperEnemy;


public class SniperEnemySpawner implements ObjectSpawner {

	private float leftBoundary;
	private float rightBoundary;
	private float timer = 0f;
	private float spawnInterval = 7f;
	private float enemySpeed = 0.5f;
	private float enemyWidth = 10f;
	private float enemyHeight = 16f;
	private float minInterval = 0f;
	private float maxInterval = 0f;
	private GameObject target;
	private float bulletInterval = 2f;

	public SniperEnemySpawner(float leftBoundary, float rightBoundary, float minInterval, float maxInterval) {
		this.leftBoundary = leftBoundary;
		this.rightBoundary = rightBoundary;
		this.minInterval = minInterval;
		this.maxInterval = maxInterval;
		spawnInterval = MathUtils.random(minInterval, maxInterval);
	}
	public SniperEnemySpawner(float leftBoundary, float rightBoundary, float spawnInterval) {
		this.leftBoundary = leftBoundary;
		this.rightBoundary = rightBoundary;
		this.spawnInterval = spawnInterval;
	}

	public SniperEnemySpawner(float leftBoundary, float rightBoundary) {
		this.leftBoundary = leftBoundary;
		this.rightBoundary = rightBoundary;
	}
	

	@Override
	public void update(float delta) {
		timer += delta;
		if(timer >= spawnInterval) {
			spawnEnemy();
		}
		
	}
	
	private void spawnEnemy() {
		timer = 0;
		float x = MathUtils.random(leftBoundary, rightBoundary);
		float y = BunnehStormGame.V_HEIGHT/2;
		BunnehStormGame game = (BunnehStormGame) Gdx.app.getApplicationListener();
		SniperEnemy enemy = new SniperEnemy(x, y, enemyWidth, enemyHeight, enemySpeed);
		enemy.setTarget(target);
		enemy.setBulletInterval(bulletInterval);
		game.goHandler.getEnemies().add(enemy);
		if(minInterval > 0 && maxInterval > 0) {
			spawnInterval = MathUtils.random(minInterval, maxInterval);
		} 
	}

	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	public void setEnemySpeed(float speed) {
		this.enemySpeed = speed;
	}
	
	public void setEnemySize(float width, float height) {
		this.enemyWidth = width;
		this.enemyHeight = height;
	}
	
	public void setSpawnIntervalRange(float min, float max) {
		spawnInterval = MathUtils.random(minInterval, maxInterval);
		this.minInterval = min;
		this.maxInterval = max;
	}
	
	public void setFixedSpawnInterval(float interval) {
		this.minInterval = 0;
		this.maxInterval = 0;
		this.spawnInterval = interval;
	}

	public void setBulletInterval(float interval) {
		this.bulletInterval = interval;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void restart() {
		
	}

}
