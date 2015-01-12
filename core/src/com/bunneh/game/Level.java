package com.bunneh.game;

import com.badlogic.gdx.utils.Array;
import com.bunneh.game.spawners.EnemySpawner;

public class Level {
	
	private int lvlNumber;
	private Array<EnemySpawner> enemySpawners;

	public Level(int lvlNumber) {
		this.lvlNumber = lvlNumber;
		enemySpawners = new Array<EnemySpawner>();
	}

	public int getLvlNumber() {
		return lvlNumber;
	}

	public void update(float delta) {
		for(int i = 0 ; i < enemySpawners.size ; i++) {
			EnemySpawner es = enemySpawners.get(i);
			if(es.isDone()) {
				enemySpawners.removeIndex(i);
				continue;
			}
			es.update(delta);
		}
	}

	public void addEnemySpawner(EnemySpawner enemySpawner) {
		enemySpawners.add(enemySpawner);
	}

	public void dispose() {
		enemySpawners.clear();
	}

}
