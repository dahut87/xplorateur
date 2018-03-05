package fr.meconnu.app.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.meconnu.app.Xplorateur;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = false;
		config.width = 1024;
		config.height = 768;
		new LwjglApplication(new Xplorateur(null), config);
		Gdx.app.log("xplorateur-DesktopLauncher", "***** Lancement de l'application X-plorateur...");
	}
}
