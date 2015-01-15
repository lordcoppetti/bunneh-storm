package com.bunneh.game.objects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
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
import com.bunneh.game.screens.PlayScreen;
import com.bunneh.game.tween.SpriteAccessor;
import com.bunneh.game.utils.MathChiches;

public class Player extends GameObject {
	
	private static final float FRAME_DURATION = 1.0f / 20.0f;
	
	// For now we assume the rect is the physical representation,
	// since we're not using any sprite yet.
	private Rectangle rect;

	private float rightBoundary;
	private float leftBoundary;

	private Vector2 velocity = new Vector2(0, 0);
	private float movSpeed = 3f;
	private float lerpAlpha = 40f;
	
	// stats
	private int lives = 99;
	private int attackPower = 5;
	private int attackPowerMax = 100;
	private int attackPowerIncreaseAmount = 5;
	private float bulletSpeed = 4f;
	private int killsToPowerUp;
	private int killCount = 0;
	
	// Control stuff for firing
	private boolean fireRequested = false;
	private float fireDelayMs = 0.4f;
	private float fireCounter = fireDelayMs;
	
	private boolean movingLeft;
	private boolean movingRight;

	private boolean invulnerable;

	// animations and sprite
	private TweenManager tm = new TweenManager();
	private boolean flashingTweenOn = false;
	private boolean doneTweenOn = false;
	private Animation idle;
	private Animation shooting;
	private Animation running;
	private Animation runningShooting;
	private float animationTime = 0f;
	private Sprite sprite;
	private float spriteXoffset;
	private float spriteYoffset;

	private boolean fireStance;

	private boolean fireMovingStance;

	private boolean gameOver = false;

	public Player(Array<AtlasRegion> idleRegions, Array<AtlasRegion> runningRegions, 
			Array<AtlasRegion> runningShootingRegions, Array<AtlasRegion> shootingRegions, 
			Rectangle playerBounds, float offsetX, float offsetY) {
		idle = new Animation(FRAME_DURATION, idleRegions, PlayMode.LOOP);
		shooting = new Animation(FRAME_DURATION, shootingRegions, PlayMode.LOOP);
		running = new Animation(FRAME_DURATION, runningRegions, PlayMode.LOOP);
		runningShooting = new Animation(FRAME_DURATION, runningShootingRegions, PlayMode.LOOP);
		sprite = new Sprite(idle.getKeyFrame(animationTime));
		this.sprite.setOrigin((playerBounds.width/2)+offsetX, (playerBounds.height/2)+offsetY);
		sprite.setBounds(playerBounds.x-offsetX, playerBounds.y-offsetY, playerBounds.width, playerBounds.height);
		this.rect = playerBounds;
		this.spriteXoffset = offsetX;
		this.spriteYoffset = offsetY;
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		calculateKillsToPowerUp();
	}

	public Player(Rectangle rect) {
		this.rect = rect;
	}

	@Override
	public void update(float delta) {
		tm.update(delta);
		if(isGameOver()) return;
		animationTime += delta;
		if(animationTime > 10) animationTime = 0;
		if(needsDestroy()) return;
		if(invulnerable) {
			playFlashingTween(delta);
		}
		updatePosition(delta);
		updateFireRequest(delta);
		updateAnimation();
	}


	private boolean isGameOver() {
		if(doneTweenOn) {
			Timeline.createSequence().beginSequence()
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(1))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(0))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(1))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(0))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(1))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(0))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(1))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(0))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(1))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(0))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(1))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(0))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(0.5f))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(0))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(0.2f))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(0))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(0.1f))
			.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.08f).target(0))
			.end().start(tm);
			Color c = Color.RED;
			Tween.to(sprite, SpriteAccessor.COLOR, 2f).target(c.r, c.g, c.b).start(tm);
			Tween.to(sprite, SpriteAccessor.ROTATE, 2f).target(45f).repeatYoyo(1, 0).setCallback(new TweenCallback() {
				@Override
				public void onEvent(int arg0, BaseTween<?> arg1) {
					PlayScreen.setGameOver(true);
				}
			}).start(tm);
			doneTweenOn = false;
		}
		return gameOver;
	}

	private void playFlashingTween(float delta) {
		if(!flashingTweenOn) return;
		Timeline.createSequence().beginSequence()
		.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.04f).target(1))
		.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.04f).target(0))
		.push(Tween.to(sprite, SpriteAccessor.ALPHA, 0.04f).target(1))
		.end().repeat(12, 0.04f).setCallback(new TweenCallback() {
			@Override
			public void onEvent(int arg0, BaseTween<?> arg1) {
				setInvulnerable(false);
			}
		}).start(tm);
		flashingTweenOn = false;
	}

	private void updateAnimation() {
		if(fireStance) {
			sprite.setRegion(shooting.getKeyFrame(animationTime));
		} else if(movingLeft || movingRight) {
			TextureRegion region = null;
			if(fireMovingStance) {
				region = runningShooting.getKeyFrame(animationTime);
			} else {
				region = running.getKeyFrame(animationTime);
			}
			sprite.setRegion(region);
			sprite.setFlip(movingRight, false);
		} else if(!movingLeft && !movingRight) {
			sprite.setRegion(idle.getKeyFrame(animationTime));
		}
	}


	private void updatePosition(float delta) {
		Vector2 velGoal = new Vector2(0, 0);
		if(movingLeft) {
			velGoal.x = -movSpeed;
			fireStance = false;
		}
		if(movingRight) {
			velGoal.x = movSpeed;
			fireStance = false;
		}
		
		velocity.x = MathChiches.approach(velGoal.x, velocity.x, delta*lerpAlpha);
		velocity.y = MathChiches.approach(velGoal.y, velocity.y, delta*lerpAlpha);

		rect.x += velocity.x;

		if(rect.x <= leftBoundary) {
			rect.x = leftBoundary;
			velocity.x = 0;
		}
		if(rect.x >= rightBoundary) {
			rect.x = rightBoundary;
			velocity.x = 0;
		}
		
		sprite.setX(rect.x - spriteXoffset);
		sprite.setY(rect.y - spriteYoffset);
	}
	
	private void updateFireRequest(float delta) {
		fireCounter += delta;
		if(!fireRequested) return;
		if(fireCounter >= fireDelayMs) {
			fire();
			fireCounter = 0f;
		}
	}

	private void fire() {
		AssetManager.playSound(GameSound.BUNNEHGUN);
		fireRequested = false;
		Sprite bSprite = new Sprite(AssetManager.assetsAtlas.findRegion("carrotBullet"));
		Bullet newBullet = new Bullet(bSprite, attackPower, rect.x + (rect.width/2), rect.y + rect.height, 90f, bulletSpeed, 4f, 9f);
		BunnehStormGame.game.goHandler.addPlayerBullet(newBullet);
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
	public Rectangle getRect() {
		return rect;
	}

	public void setXboundaries(float leftBoundary, float rightBoundary) {
		this.leftBoundary = leftBoundary;
		this.rightBoundary = rightBoundary;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void setMovementSpeed(float speed) {
		this.movSpeed = speed;
	}

	public int getAtkPow() {
		return this.attackPower;
	}

	@Override
	public boolean collided(GameObject target) {
		if(target instanceof Bullet) {
			Bullet b = (Bullet) target;
			if(b.isAllyBullet()) return false;
			if(!invulnerable) {
				lives -= 1;
				setInvulnerable(true);
			}
			target.destroy = true;
			if(lives < 0) {
				// cap it
				lives = 0;
				PlayScreen.setGameOver(true);
			}
			return true;
		}
		return false;
	}

	public void moveLeft(boolean move) {
		this.movingLeft = move;
		if(move) {
			this.movingRight = false;
		}
		fireMovingStance = false;
	}

	public void moveRight(boolean move) {
		this.movingRight = move;
		if(move) {
			this.movingLeft = false;
		}
		fireMovingStance = false;
	}

	public void setSpriteOffset(float xOffset, float yOffset) {
		spriteXoffset = xOffset;
		spriteYoffset = yOffset;
	}

	public void setSpriteSize(float width, float height) {
		sprite.setSize(width, height);
	}

	public void requestFire() {
		fireRequested = true;
		if(movingLeft || movingRight) {
			fireMovingStance = true;
		} else {
			fireStance = true;
		}
	}

	public boolean isInvulnerable() {
		return this.invulnerable;
	}

	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
		this.flashingTweenOn = true;
	}
	
	public void addKillCount() {
		if(attackPower == attackPowerMax) return;
		killCount += 1;
		if(killCount >= killsToPowerUp) {
			powerUp();
		}
	}
	
	private void powerUp() {
		attackPower += attackPowerIncreaseAmount;
		calculateKillsToPowerUp();
		if(attackPower == 15) {
			fireDelayMs -= 0.1f;
		} else if(attackPower == 25) {
			fireDelayMs -= 0.1f;
		} else if(attackPower == 35) {
			fireDelayMs -= 0.05f;
		} else if(attackPower == 60) {
			fireDelayMs -= 0.05f;
		} else if(attackPower == 85) {
			fireDelayMs -= 0.05f;
		}
	}

	private void calculateKillsToPowerUp() {
		this.killsToPowerUp = (int) ((attackPower * 3)/Math.log(attackPower));
		this.killCount = 0;
	}

	public void setGameOver(boolean over) {
		this.gameOver = over;
		this.doneTweenOn = over;
	}

}
