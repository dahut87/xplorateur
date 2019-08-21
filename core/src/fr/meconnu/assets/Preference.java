package fr.meconnu.assets;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;

public class Preference {
	public static Preferences prefs;

	public static void init() {
		prefs = Gdx.app.getPreferences("meconnu.fr xplorateur");
		if (!prefs.contains("log"))
			defaults();
		Gdx.app.setLogLevel(prefs.getInteger("log"));
	}

	public static Vector2 getmaxresolution() {
		Graphics.DisplayMode[] modes = Gdx.graphics.getDisplayModes();
		int totalpixel = 0;
		int res;
		for (DisplayMode mode : modes) {
			int temppixel = mode.height * mode.width;
			if (temppixel > totalpixel)
				totalpixel = temppixel;
		}
		for (DisplayMode mode : modes)
			if (totalpixel == mode.height * mode.width)
				return new Vector2(mode.width, mode.height);
		return null;
	}

	public static void defaults() {
		Vector2 maxres = getmaxresolution();
		Gdx.app.log("Preferences","Preference par defaut avec resolution native :" + maxres.x	+ "x" + maxres.y);
		Preference.prefs.putInteger("ResolutionX", (int) maxres.x);
		Preference.prefs.putInteger("ResolutionY", (int) maxres.y);
		Preference.prefs.putInteger("Resolution", 9);
		Preference.prefs.putBoolean("Fullscreen", true);
		Preference.prefs.putBoolean("Sound", true);
		Preference.prefs.putBoolean("Tutorial", true);
		Preference.prefs.putBoolean("VSync", true);
		Preference.prefs.putBoolean("Refresh", false);
		Preference.prefs.putBoolean("Animation", true);
		Preference.prefs.putBoolean("Language", false);
		Preference.prefs.putFloat("Effect", 1.0f);
		Preference.prefs.putFloat("Music", 0.75f);
		Preference.prefs.putInteger("Adaptation", 0);
		Preference.prefs.putInteger("Quality", 2);
		Preference.prefs.putInteger("log", Application.LOG_DEBUG);
		Preference.prefs.flush();
	}
}
