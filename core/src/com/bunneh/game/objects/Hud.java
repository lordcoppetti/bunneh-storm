package com.bunneh.game.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Hud {
	
	public static String PLAYER_LIVES = "Lives: ";
	public static String PLAYER_ATK_POW = "Power: ";
	
	private Player player;
	private Vector2 playerLivesPos = new Vector2(0, 0);
	private Vector2 playerAtkPowPos = new Vector2(0, 0);
	private BitmapFont font;
	
	public Hud(BitmapFont font) {
		this.font = font;
	}

	public void render(SpriteBatch batch) {
		if(player == null) return;
		font.draw(batch, PLAYER_LIVES + player.getLives(), playerLivesPos.x, playerLivesPos.y);
		font.draw(batch, PLAYER_ATK_POW + player.getAtkPow() + "%", playerAtkPowPos.x, playerAtkPowPos.y);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setPlayerLivesPos(Vector2 pos) {
		this.playerLivesPos = pos;
	}

	public void setPlayerAtkPowPos(Vector2 pos) {
		this.playerAtkPowPos = pos;
	}

	public Vector2 getPlayerAtkPowPos() {
		return this.playerAtkPowPos;
	}

	public Vector2 getPlayerLivesPos() {
		return this.playerLivesPos;
	}

}
