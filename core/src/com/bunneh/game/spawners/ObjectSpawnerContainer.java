package com.bunneh.game.spawners;

import com.badlogic.gdx.utils.Array;

public class ObjectSpawnerContainer {
	
	private Array<ObjectSpawner> objectSpawners = new Array<ObjectSpawner>();
	
	public void addObjectSpawner(ObjectSpawner es) {
		objectSpawners.add(es);
	}
	
	public void removeObjectSpawner(ObjectSpawner es) {
		objectSpawners.removeValue(es, true);
	}

	public void update(float timeAccum) {
		for(ObjectSpawner es : objectSpawners) {
			es.update(timeAccum);
		}
	}
	
	public void dispose() {
		for(ObjectSpawner es : objectSpawners) {
			es.dispose();
		}
	}

}
