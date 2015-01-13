package com.bunneh.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

public class TextureAtlasChiches {
	
	public static Array<AtlasRegion> getRegions(TextureAtlas atlas, String namePrefix, String separator, int startIndex) {
		Array<AtlasRegion> regions = new Array<AtlasRegion>();
		String name = namePrefix + separator + startIndex;
		AtlasRegion aRegion = atlas.findRegion(name);
		while(aRegion != null) {
			regions.add(aRegion); 
			startIndex++;
			name = namePrefix + separator + startIndex;
			aRegion = atlas.findRegion(name);
		}
		return regions;
	}

}
