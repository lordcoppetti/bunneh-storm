package com.bunneh.game.handlers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.bunneh.game.objects.GameObject;

public final class GameObjectsHandler {
	
	private Array<GameObject> obstacles;
	private Array<GameObject> enemies;
	private Array<GameObject> playerBullets;
	private Array<GameObject> enemyBullets;
	
	public void initialize() {
		this.obstacles = new Array<GameObject>();
		this.enemies = new Array<GameObject>();
		this.playerBullets = new Array<GameObject>();
		this.enemyBullets = new Array<GameObject>();
	}
	
	public void addObstacle(GameObject go) {
		obstacles.add(go);
	}

	public void addEnemy(GameObject go) {
		enemies.add(go);
	}

	public void addPlayerBullet(GameObject go) {
		playerBullets.add(go);
	}

	public void addEnemyBullet(GameObject go) {
		enemyBullets.add(go);
	}
	
	public void update(float delta, GameObject... additional) {
		updateObstacles(delta);
		updateEnemies(delta);
		updatePlayerBullets(delta);
		updateEnemyBullets(delta);
		if(additional != null && additional.length > 0) {
			int i = 0;
			while(i < additional.length) {
				GameObject go = additional[i];
				if(go != null && go.needsDestroy()) {
					go.dispose();
					i++;
					continue;
				}
				go.update(delta);
				i++;
			}
		}
	}
	
	public void updateObstacles(float delta) {
		for(int i = 0 ; i < obstacles.size ; i++) {
			GameObject go = obstacles.get(i);
			if(go.needsDestroy()) {
				obstacles.removeIndex(i);
				go.dispose();
				continue;
			}
			go.update(delta);
		}
	}

	public void updateEnemies(float delta) {
		for(int i = 0 ; i < enemies.size ; i++) {
			GameObject go = enemies.get(i);
			if(go.needsDestroy()) {
				enemies.removeIndex(i);
				go.dispose();
				continue;
			}
			go.update(delta);
		}
	}

	public void updatePlayerBullets(float delta) {
		for(int i = 0 ; i < playerBullets.size ; i++) {
			GameObject go = playerBullets.get(i);
			if(go.needsDestroy()) {
				playerBullets.removeIndex(i);
				go.dispose();
				continue;
			}
			go.update(delta);
		}
	}

	public void updateEnemyBullets(float delta) {
		for(int i = 0 ; i < enemyBullets.size ; i++) {
			GameObject go = enemyBullets.get(i);
			if(go.needsDestroy()) {
				enemyBullets.removeIndex(i);
				go.dispose();
				continue;
			}
			go.update(delta);
		}
	}
	
	public void disposeAll() {
		for(GameObject go : obstacles) go.dispose();
		for(GameObject go : enemies) go.dispose();
		for(GameObject go : playerBullets) go.dispose();
		for(GameObject go : enemyBullets) go.dispose();
		obstacles.clear();
		enemies.clear();
		playerBullets.clear();
		enemyBullets.clear();
	}
	
	public Array<GameObject> getObstacles() {
		return this.obstacles;
	}

	public Array<GameObject> getEnemies() {
		return this.enemies;
	}

	public Array<GameObject> getPlayerBullets() {
		return this.playerBullets;
	}

	public Array<GameObject> getEnemyBullets() {
		return this.enemyBullets;
	}

	public void debugRender(ShapeRenderer debugRender, OrthographicCamera camera, GameObject... additional) {
			debugRender.setProjectionMatrix(camera.combined);
			debugRender.begin(ShapeType.Line);
			debugRender.setColor(Color.GREEN);
			for(GameObject go : obstacles) {
				Rectangle rect = go.getRect();
				debugRender.rect(rect.x, rect.y, rect.width, rect.height);
			}
			for(GameObject go : enemies) {
				Rectangle rect = go.getRect();
				debugRender.rect(rect.x, rect.y, rect.width, rect.height);
			}
			for(GameObject go : playerBullets) {
				Rectangle rect = go.getRect();
				debugRender.rect(rect.x, rect.y, rect.width, rect.height);
			}
			for(GameObject go : enemyBullets) {
				Rectangle rect = go.getRect();
				debugRender.rect(rect.x, rect.y, rect.width, rect.height);
			}
			for(GameObject go : additional) {
				Rectangle rect = go.getRect();
				debugRender.rect(rect.x, rect.y, rect.width, rect.height);
			}
			debugRender.end();
	}

	public int getObjectsSize() {
		int total = 0;
		total += obstacles.size + enemies.size
				+ playerBullets.size + enemyBullets.size;
		return total;
	}

	public void render(SpriteBatch batch, OrthographicCamera camera) {
		for(GameObject go : obstacles) go.render(batch);
		for(GameObject go : enemies) go.render(batch);
		for(GameObject go : enemyBullets) go.render(batch);
		for(GameObject go : playerBullets) go.render(batch);
		
	}

	public void render(SpriteBatch batch, OrthographicCamera camera, float alphaModulation) {
		for(GameObject go : obstacles) go.render(batch, alphaModulation);
		for(GameObject go : enemies) go.render(batch, alphaModulation);
		for(GameObject go : playerBullets) go.render(batch, alphaModulation);
		for(GameObject go : enemyBullets) go.render(batch, alphaModulation);
	}

}
