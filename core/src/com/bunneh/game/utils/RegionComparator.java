package com.bunneh.game.utils;

import java.util.Comparator;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class RegionComparator implements Comparator<AtlasRegion> {

	@Override
	public int compare(AtlasRegion region1, AtlasRegion region2) {
		return region1.name.compareTo(region2.name);
	}

}
