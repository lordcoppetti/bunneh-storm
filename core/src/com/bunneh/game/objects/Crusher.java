package com.bunneh.game.objects;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bunneh.game.AssetManager;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.AssetManager.GameSound;
import com.bunneh.game.tween.SpriteAccessor;
import com.bunneh.game.utils.MathChiches;

public class Crusher extends Enemy {
	
	private float speed;
	private GameObject target;
	private Vector2 velocity = new Vector2(0, 0);
	private boolean followTarget = false;
	private float targetDistance;

	private Sprite sprite;
	private float animationTimer = 0f;
	private Animation<TextureRegion> explodeAnimation;
	private boolean explode = false;

	private TweenManager tm = new TweenManager();

	public Crusher(Sprite sprite, Array<AtlasRegion> explosionRegions, Rectangle rect, float speed) {
		super(rect);
		this.health = 20;
		this.speed = speed;
		this.velocity.y = -speed;
		explodeAnimation = new Animation(FRAME_DURATION, explosionRegions);
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
		tm.update(delta);
		if(explode) {
			animationTimer += delta;
			playExplosionAnimation();
			return;
		}
		updatePosition();
		sprite.setX(rect.x);
		sprite.setY(rect.y);
	}

	private void playExplosionAnimation() {
		sprite.setRegion(explodeAnimation.getKeyFrame(animationTimer));
		if(explodeAnimation.isAnimationFinished(animationTimer)) {
			destroy = true;
		}
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
		if(explode) return false;
		if(target instanceof Player) {
			AssetManager.playSound(GameSound.WMELONEXPLOSION);
			Player player = (Player) target;
			if(!player.isInvulnerable()) {
				player.setLives(player.getLives()-1);
				player.setInvulnerable(true);
			}
			explode = true;
			if(player.getLives() < 0) {
				// cap it
				player.setLives(0);
				player.setGameOver(true);
			}
			return true;
		}
		if(target instanceof Bullet) {
			Bullet b = (Bullet) target;
			if(!b.isAllyBullet()) return false;
			health -= b.getAttackPower();
			if(health <= 0) {
				AssetManager.playSound(GameSound.WMELONEXPLOSION);
				explode = true;
				Player player = BunnehStormGame.game.levelHandler.getPlayer();
				player.addKillCount();
			} 
			playHitAnimation();
			b.destroy = true;
			return true;
		}
		if(target instanceof Floor) {
			AssetManager.playSound(GameSound.WMELONEXPLOSION);
			explode = true;
			return true;
		}
		return false;
	}

	private void playHitAnimation() {
		Color o = sprite.getColor();
		Color c = Color.PINK;
		Timeline.createSequence().beginSequence()
		.push(Tween.to(sprite, SpriteAccessor.COLOR, 0.05f).target(c.r, c.g, c.b))
		.push(Tween.to(sprite, SpriteAccessor.COLOR, 0.05f).target(o.r, o.g, o.b))
		.end().start(tm);
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
	
	public void setExplode(boolean explode) {
		this.explode = explode;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}

}
