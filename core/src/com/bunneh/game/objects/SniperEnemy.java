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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.screens.PlayScreen;
import com.bunneh.game.tween.SpriteAccessor;
import com.bunneh.game.utils.MathChiches;

public class SniperEnemy extends Enemy {
	
	private Vector2 velocity = new Vector2(0, 0);
	private float speed;

	private float bulletTimer = 0f;
	private float bulletInterval = 2f;
	private float bulletSpeed = 1.5f;
	private int bulletPower = 1;

	private GameObject target;
	private float fireYlimit;
	private Color color = Color.RED;

	private Sprite sprite;
	private float animationTimer = 0f;
	private Animation explodeAnimation;
	private boolean explode = false;

	private TweenManager tm = new TweenManager();
	
	public SniperEnemy(Sprite sprite, Array<AtlasRegion> explosionRegions, float x, float y, float width, float height, float enemySpeed) {
		super(new Rectangle(x, y, width, height));
		this.speed = enemySpeed;
		this.health = 15;
		explodeAnimation = new Animation(FRAME_DURATION, explosionRegions);
		this.sprite = sprite;
		this.sprite.setColor(color);
		sprite.setBounds(rect.x, rect.y, rect.width, rect.height);
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
		fire(delta);
		sprite.setX(rect.x);
		sprite.setY(rect.y);
	}

	private void playExplosionAnimation() {
		sprite.setRegion(explodeAnimation.getKeyFrame(animationTimer));
		if(explodeAnimation.isAnimationFinished(animationTimer)) {
			destroy = true;
		}
		
	}

	private void fire(float delta) {
		if(target == null) return;
		if(rect.y <= fireYlimit) return;
		bulletTimer += delta;
		if(bulletTimer >= bulletInterval) {
			Rectangle targetRect = target.getRect();
			float angle = (float) MathChiches.getAngle(rect.x, rect.y, (targetRect.x+targetRect.width/2), (targetRect.y+targetRect.height/2));
			Sprite bSprite = new Sprite(PlayScreen.atlas.findRegion("watermelonBullet"));
			Bullet b = new Bullet(bSprite, bulletPower, rect.x+rect.width/2, rect.y, angle, bulletSpeed, 4f, 9f);
			b.setAllyBullet(false);
			BunnehStormGame.game.goHandler.addEnemyBullet(b);
			bulletTimer = 0f;
		}
	}

	private void updatePosition() {
		velocity.y = -speed;
		this.rect.y += velocity.y;
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	public void setBulletSpeed(float speed) {
		this.bulletSpeed = speed;
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
	
	public void setSize(float width, float height) {
		this.rect.width = width;
		this.rect.height = height;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean collided(GameObject target) {
		if(explode) return false;
		if(target instanceof Player) {
			Player player = (Player) target;
			if(!player.isInvulnerable()) {
				player.setLives(player.getLives()-1);
				player.setInvulnerable(true);
			}
			explode = true;
			if(player.getLives() < 0) {
				// cap it
				player.setLives(0);
				PlayScreen.setGameOver(true);
			}
			return true;
		}
		if(target instanceof Bullet) {
			Bullet b = (Bullet) target;
			if(!b.isAllyBullet()) return false;
			health -= b.getAttackPower();
			if(health <= 0) {
				explode = true;
				Player player = BunnehStormGame.game.levelHandler.getPlayer();
				player.addKillCount();
			}
			playHitAnimation();
			b.destroy = true;
			return true;
		}
		if(target instanceof Floor) {
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
	
	public void setBulletInterval(float interval) {
		this.bulletInterval = interval;
	}

	public void setFireLimitOnY(float yLimit) {
		this.fireYlimit = yLimit;
	}

}
