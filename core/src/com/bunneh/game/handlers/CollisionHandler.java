package com.bunneh.game.handlers;

import com.badlogic.gdx.utils.Array;
import com.bunneh.game.objects.Floor;
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

	public void checkCollision(Array<GameObject> groupA, Player player, Floor floor) {
		for(GameObject go : groupA) {
			if(go.getRect().overlaps(player.getRect())) {
				player.collided(go);
				continue;
			}
			if(go.getRect().overlaps(floor.getRect())) {
				go.collided(floor);
				continue;
			}
		}
	}

	public void checkCollision(Array<GameObject> enemies,
			Array<GameObject> playerBullets, Player player, Floor floor) {
		for(GameObject e : enemies) {
			if(e.getRect().overlaps(player.getRect())) {
				player.collided(e);
				continue;
			}
			if(e.getRect().overlaps(floor.getRect())) {
				e.collided(floor);
				continue;
			}
			for(GameObject pb : playerBullets) {
				if(e.getRect().overlaps(pb.getRect())) {
					e.collided(pb);
					pb.collided(e);
					continue;
				}
			}
		}
		
	}

}
