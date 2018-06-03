package fr.meconnu.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.assets.Preference;
import fr.meconnu.cache.Filler;
import fr.meconnu.screens.SplashScreen;

public class Xplorateur extends Game {
	private static SpriteBatch batch;
	private static Texture img;

	public Xplorateur(Wrapper wrapper) {
		AssetLoader.wrapper=wrapper;
	}

	@Override
	public void create() {
		Preference.init();
		Gdx.app.debug("xplorateur-main","Récupération de la résolution des préférences.");
		Gdx.app.debug("xplorateur-main","Initialisation du système de positionnement.");
		AssetLoader.filler.init();
		if (Preference.prefs.getInteger("ResolutionX") > 0
				&& Preference.prefs.getInteger("ResolutionY") > 0) {
			try {
				int ResolutionX = Preference.prefs.getInteger("ResolutionX");
				int ResolutionY = Preference.prefs.getInteger("ResolutionY");
				boolean Fullscreen = Preference.prefs.getBoolean("Fullscreen");
				boolean VSync = Preference.prefs.getBoolean("VSync");
				Gdx.graphics.setWindowedMode(ResolutionX, ResolutionY);
				Gdx.graphics.setVSync(VSync);
			} catch (ClassCastException e) {
				Gdx.app.error("xplorateur-main","***** Impossible d'appliquer les préférences graphiques");
				Gdx.app.debug("xplorateur-main", e.getMessage());
			} finally {
				Gdx.app.log("xplorateur-main","***** Changement de résolution selon préférences graphiques");
			}
		} else
			Gdx.app.debug("xplorateur-main", "...Aucune préférence !");
		AssetLoader.init();
		Gdx.app.debug("xplorateur-main", "Creation de l'objet SplashScreen.");
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}


