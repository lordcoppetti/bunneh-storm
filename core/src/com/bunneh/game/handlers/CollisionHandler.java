package com.bunneh.game.handlers;

import com.badlogic.gdx.utils.Array;
import com.bunneh.game.objects.GameObject;
import com.bunneh.game.objects.Player;

public final class CollisionHandler {
	
	public void checkCollision(Player player, Array<GameObject> obstacles) {
		for(GameObject go : obstacles) {
			if(!player.getRect().overlaps(go.getRect())) continue;
			player.collided(go);
			go.collided(go);
		}
	}

	// This is a generic collision detection against 2 groups
	public void checkCollision(Array<GameObject> groupA, Array<GameObject> groupB) {
		for(GameObject go : groupA) {
			for(GameObject go2 : groupB) {
				if(!go.getRect().overlaps(go2.getRect())) continue;
				go.collided(go2);
				go2.collided(go);
			}
		}
	}

	// Also generic but checks groupA against groupB and C, if C collides,
	// there is no further collision check for current A
	public void checkCollision(Array<GameObject> groupA, Array<GameObject> groupB, GameObject c) {
		for(GameObject go : groupA) {
			if(go.getRect().overlaps(c.getRect())) {
				go.collided(c);
				c.collided(go);
				continue;
			}
			for(GameObject go2 : groupB) {
				if(!go.getRect().overlaps(go2.getRect())) continue;
				go.collided(go2);
				go2.collided(go);
			}
		}
	}

}
