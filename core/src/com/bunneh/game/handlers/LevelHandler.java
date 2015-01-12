package com.bunneh.game.handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bunneh.game.Level;
import com.bunneh.game.spawners.EnemySpawner;
import com.bunneh.game.spawners.EnemySpawner.EnemyType;

public class LevelHandler {

	private static final int levelAmount = 9;
	private static final int createBy = 3;
	
	private Level level;
	private Array<Level> levelArray;
	private int levelsCreated = 0;
	private float timer = 0f;
	private int changeLevelTimer = 30;
	private boolean gameDone;
	
	public LevelHandler() {
		levelArray = new Array<Level>(levelAmount);
	}
	
	
	public void createLevels() {
		float lvlsToCreate = (levelsCreated-1 + createBy);
		if(lvlsToCreate > levelAmount) {
			gameDone = true;
			return;
		}
		int from = levelsCreated;
		int to = from + createBy;
		for(int i = from ; i < to ; i++) {
			Level level = new Level(i+1);
			prepareLevel(level);
			levelArray.add(level);
			levelsCreated++;
		}
		level = levelArray.removeIndex(0);
	}
	
	public void changeLevel() {
		if(levelArray.size == 0) {
			createLevels();
			return;
		}
		level = levelArray.removeIndex(0);
	}
 
	private void prepareLevel(Level level) {
		float n = level.getLvlNumber();
		if(n <= levelAmount) {
			if(n <= 3) {
				// TODO: Values difficulty 1
				float spawnInterval = 2f/n;
				Vector2 enemySize = new Vector2(15f, 15f);
				EnemySpawner es1 = new EnemySpawner(EnemyType.Obstacle, spawnInterval, enemySize);
				es1.setEnemySpeed(n*0.7f);
				es1.setEnemyHealth((int) n*5);
				level.addEnemySpawner(es1);
			} else if(n <= 6) {
				// TODO: Values difficulty 2
			} else if(n <= 9) {
				// TODO: Values difficulty 3

			} else if(n == 10) {
				// TODO: Values difficulty 4

			} else if(n <= 15) {
				// TODO: Values difficulty 5

			}
		}
	}


	public void update(float delta) {
		if(gameDone) {
			win();
			return;
		}
		timer += delta;
		if(timer > changeLevelTimer) {
			changeLevel();
			timer = 0;
			return;
		}
		level.update(delta);
	}
	
	private void win() {
		// TODO: TOTAL WIN!
		dispose();
	}


	public void dispose() {
		level.dispose();
		levelsCreated = 1;
		levelArray = null;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Level getCurrentLevel() {
		return level;
	}

	public void setCurrentLevel(Level currentLevel) {
		this.level = currentLevel;
	}
	
	public Array<Level> getAllLevels() {
		return levelArray;
	}

}
