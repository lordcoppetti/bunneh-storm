package com.bunneh.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bunneh.game.utils.MathChiches;

public class Crusher extends Enemy {
	
	private float speed;
	private GameObject target;
	private Vector2 velocity = new Vector2(0, 0);
	private boolean followTarget = false;
	private float targetDistance;
	private Sprite sprite;

	public Crusher(Sprite sprite, Rectangle rect, float speed) {
		super(rect);
		this.health = 20;
		this.speed = speed;
		this.velocity.y = -speed;
		this.sprite = sprite;
		sprite.setBounds(rect.x, rect.y, rect.width, rect.height);
	}

	public Crusher(Rectangle rect, float speed) {
		super(rect);
		this.health = 20;
		this.speed = speed;
		this.velocity.y = -speed;
	}

	@Override
	public void update(float delta) {
		updatePosition();
		sprite.setX(rect.x);
		sprite.setY(rect.y);
	}

	private void updatePosition() {
		if(target != null && followTarget) {
			followTargetUntilTooClose();
		} else {
			rect.x += velocity.x;
			rect.y += velocity.y;
		}
	}

	private void followTargetUntilTooClose() {
		float distance = MathChiches.distance(rect.x, rect.y, target.getRect().x, target.getRect().y);
		if(distance < targetDistance) {
			setFollowTarget(false);
			Rectangle tr = target.getRect();
			float angle = (float) MathChiches.getAngle(rect.x+rect.width/2, rect.y, (tr.x+tr.width/2), (tr.y+tr.height/2));
			this.velocity = MathChiches.getAngleBasedMovement(angle, speed);
			return;
		}
		float accelSpeed = (speed*0.01f);
		Vector2 v = MathChiches.vectorToTarget(rect.x+rect.width/2, rect.y, 
				target.getRect().x, target.getRect().y);
		rect.x += v.x*accelSpeed;
		rect.y += v.y*accelSpeed;

	}

	@Override
	public void render(SpriteBatch batch) {
		if(sprite == null) return;
		sprite.draw(batch);
	}

	@Override
	public void render(SpriteBatch batch, float alphaModulation) {
		if(sprite == null) return;
		sprite.draw(batch, alphaModulation);

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean collided(GameObject target) {
		if(target instanceof Bullet) {
			Bullet b = (Bullet) target;
			if(!b.isAllyBullet()) return false;
			health -= b.getAttackPower();
			if(health <= 0) {
				destroy = true;
			}
			return true;
		}
		if(target instanceof Floor) {
			destroy = true;
			return true;
		}
		return false;
	}

	public void setTarget(GameObject target) {
		this.target = target;
		Rectangle tr = target.getRect();
		float angle = (float) MathChiches.getAngle(rect.x, rect.y, (tr.x+tr.width/2), (tr.y+tr.height/2));
		this.velocity = MathChiches.getAngleBasedMovement(angle, speed);
	}
	
	public void setFollowTarget(boolean followTarget) {
		this.followTarget = followTarget;
	}
	
	public void setFollowTargetLimit(float distance) {
		this.targetDistance = distance;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}

}
