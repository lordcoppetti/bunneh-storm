package com.bunneh.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.bunneh.game.BunnehStormGame;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig () {
        GwtApplicationConfiguration gwtac = new GwtApplicationConfiguration(600, 600);
        return gwtac;
    }

    @Override
    public ApplicationListener createApplicationListener () {
        return new BunnehStormGame();
    }
}

