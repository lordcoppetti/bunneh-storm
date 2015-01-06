package com.bunneh.game.objects;

import com.badlogic.gdx.utils.Array;

public class EnemySpawnerContainer {
	
	private Array<EnemySpawner> enemySpawners = new Array<EnemySpawner>();
	
	public void addEnemySpawner(EnemySpawner es) {
		enemySpawners.add(es);
	}
	
	public void removeEnemySpawner(EnemySpawner es) {
		enemySpawners.removeValue(es, true);
	}

	public void update(float timeAccum) {
		for(EnemySpawner es : enemySpawners) {
			es.update(timeAccum);
		}
	}
	
	public void dispose() {
		for(EnemySpawner es : enemySpawners) {
			es.dispose();
		}
	}

}
