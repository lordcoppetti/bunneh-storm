package com.bunneh.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.bunneh.game.utils.RegionComparator;
import com.bunneh.game.utils.TextureAtlasChiches;

public final class AssetManager {
	
	private AssetManager() {}

	public static TextureAtlas assetsAtlas;
	public static TextureAtlas explosionAtlas;

	private static Array<AtlasRegion> assetsRegions;
	private static Array<AtlasRegion> explosionRegions;
	
	public enum GameMusic {
		LEVEL("sound/theme.mp3");
		
		private final String filename;
		
		private GameMusic(String filename) {
			this.filename = filename;
		}
		
		public String getFilename() {
			return filename;
		}
	};

	public enum GameSound {
		BUNNEHGUN("sound/bunnehGun.wav"),
		WMELONGUN("sound/watermelonGun.wav"),
		WMELONEXPLOSION("sound/watermelonExplodes.wav");
		
		private final String filename;
		
		private GameSound(String filename) {
			this.filename = filename;
		}
		
		public String getFilename() {
			return filename;
		}
		
		public Sound getSound(GameSound sound) {
			return AssetManager.sounds.get(sound);
		}
	};
	
	private static Music musicBeingPlayed;
	private static float musicVolume = 1f;
	private static float soundVolume = 1f;
	private static boolean musicEnabled = true;

	private static HashMap<GameSound, Sound> sounds;
	
	public static void loadAll() {
		loadSpritesAtlas();
		loadExplosionAtlas();
		loadSounds();
	}

	private static void loadSounds() {
		sounds = new HashMap<GameSound, Sound>();
		sounds.put(GameSound.BUNNEHGUN, Gdx.audio.newSound(Gdx.files.internal(GameSound.BUNNEHGUN.getFilename())));
		sounds.put(GameSound.WMELONGUN, Gdx.audio.newSound(Gdx.files.internal(GameSound.WMELONGUN.getFilename())));
		sounds.put(GameSound.WMELONEXPLOSION, Gdx.audio.newSound(Gdx.files.internal(GameSound.WMELONEXPLOSION.getFilename())));
	}

	public static void playSound(GameSound sound) {
		if(sound == GameSound.WMELONEXPLOSION) {
			float vol = soundVolume/2f;
			sound.getSound(sound).play(vol);
			return;
		}
		if(sound == GameSound.WMELONGUN) {
			float vol = soundVolume/2f;
			sound.getSound(sound).play(vol);
			return;
		}
		sound.getSound(sound).play(soundVolume);
	}
	
	public static void playMusic(GameMusic music) {
		if(!musicEnabled) return;
		stopMusic();
		FileHandle musicFile = Gdx.files.internal(music.getFilename());
		musicBeingPlayed = Gdx.audio.newMusic(musicFile);
		musicBeingPlayed.setVolume(musicVolume);
		musicBeingPlayed.setLooping(true);
		musicBeingPlayed.play();
	}

	public static void stopMusic() {
		if(musicBeingPlayed != null) {
			musicBeingPlayed.stop();
			musicBeingPlayed.dispose();
		}
	}
	
	public static void setMusicVolume(float volume) {
		if(volume < 0 || volume > 1f) {
			throw new IllegalArgumentException("The volume must be inside the range: [0,1]");
		}
		AssetManager.musicVolume = volume;
		if(musicBeingPlayed != null) {
			musicBeingPlayed.setVolume(volume);
		}

	}
	
	public static void setSoundVolume(float volume) {
		soundVolume = volume;
	}

	public static void setMusicEnabled(boolean enabled) {
		AssetManager.musicEnabled = enabled;
		
		if(!musicEnabled) {
			stopMusic();
		}
	}

	public static void loadSpritesAtlas() {
		assetsAtlas = new TextureAtlas(Gdx.files.internal("assets.atlas"));
		assetsRegions = assetsAtlas.getRegions();
		assetsRegions.sort(new RegionComparator());
	}

	public static void loadExplosionAtlas() {
		explosionAtlas = new TextureAtlas(Gdx.files.internal("explosion.atlas"));
		explosionRegions = AssetManager.getAssetsRegions();
		explosionRegions.sort(new RegionComparator());
		explosionRegions = TextureAtlasChiches.getRegions(explosionAtlas, "watermelonExplosion", "-", 0);
	}
	
	public static Array<AtlasRegion> getAssetsRegions() {
		return assetsRegions;
	}

	public static Array<AtlasRegion> getExplosionRegions() {
		return explosionRegions;
	}
	
	public static void dispose() {
		assetsAtlas.dispose();
		explosionAtlas.dispose();
		stopMusic();
		for(Sound sound : sounds.values()) {
			sound.dispose();
		}
	}

}
