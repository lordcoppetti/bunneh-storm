package com.bunneh.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bunneh.game.BunnehStormGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = BunnehStormGame.TITLE + " - " + BunnehStormGame.VERSION;
		new LwjglApplication(new BunnehStormGame(), config);
	}
}
