package com.bunneh.game.handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bunneh.game.Level;
import com.bunneh.game.objects.Player;
import com.bunneh.game.spawners.EnemySpawner;
import com.bunneh.game.spawners.EnemySpawner.EnemyType;

public class LevelHandler {

	private static final int levelAmount = 99;
	private static final int createBy = 3;
	
	private Level level;
	private Array<Level> levelArray;
	private int levelsCreated = 0;
	private float timer = 0f;
	private int changeLevelTimer = 20;
	private boolean gameDone;

	private Player player;
	
	public LevelHandler(Player player) {
		levelArray = new Array<Level>(levelAmount);
		this.player = player;
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
				float spawnInterval = 1.5f/n;
				Vector2 enemySize = new Vector2(15f, 15f);
				if(n == 3) {
					enemySize.x = 25f;
					enemySize.y = 25f;
				}
				EnemySpawner es1 = new EnemySpawner(EnemyType.Obstacle, spawnInterval, enemySize);
				es1.setEnemySpeed(n*0.7f);
				es1.setEnemyHealth((int) n*5);
				level.addEnemySpawner(es1);
			} else if(n <= 6) {
				changeLevelTimer = 30;
				Vector2 enemySize = new Vector2(20f, 20f);
				float spawnInterval = 2/n;
				EnemySpawner es1;
				if(n == 5) {
					changeLevelTimer = 20;
				}
				if(n == 6) {
					spawnInterval = 2.2f;
					enemySize.x = 18f;
					enemySize.y = 18f;
					Vector2 enemySize2 = new Vector2(15f, 15f);
					EnemySpawner es2 = new EnemySpawner(EnemyType.Obstacle, 0.6f, enemySize2);
					level.addEnemySpawner(es2);
				}
				es1 = new EnemySpawner(EnemyType.Obstacle, spawnInterval, enemySize);
				es1.setEnemyTarget(player);
				if(n == 6) {
					es1.setFollowTarget(true);
					es1.setEnemySpeed(3f);
				}
				level.addEnemySpawner(es1);
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
		player.dispose();
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


	public Player getPlayer() {
		return player;
	}


	public void setPlayer(Player player) {
		this.player = player;
	}

}
