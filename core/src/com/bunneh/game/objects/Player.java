package com.bunneh.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bunneh.game.BunnehStormGame;
import com.bunneh.game.screens.PlayScreen;
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
	
	private int lives = 3;
	private int attackPower = 5;
	private float bulletSpeed = 4f;
	
	// Control stuff for firing
	private boolean fireRequested = false;
	private float fireDelayMs = 0.3f;
	private float fireCounter = fireDelayMs;
	
	private boolean movingLeft;
	private boolean movingRight;

	private boolean invulnerable;
	private float invulnerableTimer = 0f;
	private float invulnerableLength = 1f; // 1 second
	
	// animations and sprite
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
	
	public Player(Array<AtlasRegion> idleRegions, Array<AtlasRegion> runningRegions, 
			Array<AtlasRegion> runningShootingRegions, Array<AtlasRegion> shootingRegions, 
			Rectangle playerBounds, float offsetX, float offsetY) {
		idle = new Animation(FRAME_DURATION, idleRegions, PlayMode.LOOP);
		shooting = new Animation(FRAME_DURATION, shootingRegions, PlayMode.LOOP);
		running = new Animation(FRAME_DURATION, runningRegions, PlayMode.LOOP);
		runningShooting = new Animation(FRAME_DURATION, runningShootingRegions, PlayMode.LOOP);
		sprite = new Sprite(idle.getKeyFrame(animationTime));
		sprite.setBounds(playerBounds.x-offsetX, playerBounds.y-offsetY, playerBounds.width, playerBounds.height);
		this.rect = playerBounds;
		this.spriteXoffset = offsetX;
		this.spriteYoffset = offsetY;
	}

	
	public Player(Rectangle rect) {
		this.rect = rect;
	}

	@Override
	public void update(float delta) {
		animationTime += delta;
		if(animationTime > 10) animationTime = 0;
		if(needsDestroy()) return;
		if(invulnerable) {
			invulnerableTimer += delta;
			if(invulnerableTimer >= invulnerableLength) {
				invulnerable = false;
				invulnerableTimer = 0f;
			}
		}
		updatePosition(delta);
		updateFireRequest(delta);
		updateAnimation();
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
		fireRequested = false;
		Sprite bSprite = new Sprite(PlayScreen.atlas.findRegion("carrotBullet"));
		Bullet newBullet = new Bullet(bSprite, attackPower, rect.x + (rect.width/2), rect.y + rect.height, 90f, bulletSpeed, 4f, 9f);
		BunnehStormGame game = (BunnehStormGame) Gdx.app.getApplicationListener();
		game.goHandler.addPlayerBullet(newBullet);
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
		if(target instanceof Enemy) {
			if(!invulnerable) {
				lives -= 1;
				invulnerable = true;
			}
			target.destroy = true;
			if(lives < 0) {
				// cap it
				lives = 0;
				PlayScreen.setGameOver(true);
			}
			return true;
		}
		if(target instanceof Bullet) {
			Bullet b = (Bullet) target;
			if(b.isAllyBullet()) return false;
			if(!invulnerable) {
				lives -= 1;
				invulnerable = true;
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

}
