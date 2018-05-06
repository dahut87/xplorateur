package fr.meconnu.app.desktop;

import org.oscim.awt.AwtGraphics;
import org.oscim.backend.GLAdapter;
import org.oscim.gdx.GdxAssets;
import org.oscim.gdx.LwjglGL20;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import fr.meconnu.app.Xplorateur;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = false;
		config.width = 1024;
		config.height = 768;
		initVtm();
		new LwjglApplication(new Xplorateur(null), config);
		Gdx.app.log("xplorateur-DesktopLauncher", "***** Lancement de l'application X-plorateur...");
	}
	
	public static void initVtm() {
	    new SharedLibraryLoader().load("vtm-jni");
	    AwtGraphics.init();
	    GdxAssets.init("assets/");
	    GLAdapter.init(new LwjglGL20());
	    GLAdapter.GDX_DESKTOP_QUIRKS = true;
	}
}

