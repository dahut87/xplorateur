package fr.meconnu.assets;

import java.util.Locale;

import fr.meconnu.database.Base.datatype;
import fr.meconnu.database.DatabaseManager;
import fr.meconnu.database.LocalBase;
import fr.meconnu.database.SqlBase;
import fr.meconnu.screens.MenuScreen;
import fr.meconnu.cache.Criteria;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class AssetLoader {
	public static Skin Skin_images;
	public static TextureAtlas Atlas_images;
	public static Texture Texture_fond;
	public static Texture Texture_fond2;
	public static Texture Texture_logo;
	public static int width;
	public static int height;
	public static float ratio;
	public static String[] Typenames;
	public static ScalingViewport viewport;
	public static OrthographicCamera Camera;
	private static Texture emptyT;
	private static Texture fullT;
	public static NinePatch empty;
	public static NinePatch full;
	public static AssetManager manager;
	public static TooltipManager Tooltipmanager;
	public static I18NBundle french, usa, language;
	public static TextureFilter quality;
	public static DatabaseManager Datahandler;
	public static Boolean Accelerometer, Compass, Vibrator, Gyroscope;
	
	public static void init() {
		Gdx.app.debug("xplorateur-AssetLoader","Initialisation de la résolution virtuelle...");
		int realWidth = Gdx.graphics.getWidth();
		int realHeight = Gdx.graphics.getHeight();
		float realRatio = realWidth / (float) realHeight;
		Gdx.app.debug("xplorateur-AssetLoader", "Résolution de " + realWidth + "x"
				+ realHeight + " ratio de " + String.format("%.2f", realRatio)
				+ ".");
		width = 1920;
		height = 1080;
		Gdx.app.debug("xplorateur-AssetLoader",	"Ratio 16/9, résolution virtuelle : 1920x1080.");
		Camera = new OrthographicCamera(width, height);
		Camera.position.set(width / 2, height / 2, 0);
		Camera.update();
		if (Preference.prefs.getInteger("Adaptation") == 1) {
			viewport = new StretchViewport(width, height);
			Gdx.app.debug("xplorateur-AssetLoader",
					"Adaptation d'écran maximale, 'Aspect-Ratio' non conservé.");
		} else {
			viewport = new FitViewport(width, height);
			Gdx.app.debug("xplorateur-AssetLoader",
					"Adaptation d'écran totale, 'Aspect-Ratio' conservé.");
		}
		Gdx.app.debug("xplorateur-AssetLoader","Détection des périphériques...");
		Accelerometer=Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
		Compass=Gdx.input.isPeripheralAvailable(Peripheral.Compass);
		Vibrator=Gdx.input.isPeripheralAvailable(Peripheral.Vibrator);
		Gyroscope=Gdx.input.isPeripheralAvailable(Peripheral.Gyroscope);
		Gdx.app.debug("xplorateur-AssetLoader","Accéléromètre:"+Accelerometer.toString()+" /Vibration:"+Vibrator.toString()+" / Gyroscope:"+Gyroscope.toString()+" / Boussole:"+Compass.toString() );
		viewport.apply();
		Gdx.app.debug("xplorateur-AssetLoader","Mise en place des sauvegardes de filtre" );
	}
	
	public static void loadall() {
		TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
		params.minFilter = quality;
		params.magFilter = quality;
		params.genMipMaps = (quality == TextureFilter.MipMap);
		Gdx.app.debug("xplorateur-AssetLoader", "Initialisation du asset manager");
		manager = new AssetManager();
		Gdx.app.debug("xplorateur-AssetLoader", "Initialisation du chargement des éléments multimédia");
		manager.load("textures/images.pack", TextureAtlas.class);
		manager.load("pictures/fond.png", Texture.class, params);
		manager.load("pictures/fond2.png", Texture.class, params);
		manager.load("textures/images.json", Skin.class,new SkinLoader.SkinParameter("textures/images.pack"));
	}
	
	public static void finishall() {
		Gdx.app.debug("xplorateur-AssetLoader", "Attente fin chargement...");
		manager.finishLoading();
		Gdx.app.debug("xplorateur-AssetLoader", "Affectation des éléments multimédia");
		Texture_fond = manager.get("pictures/fond.png");
		Texture_fond.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Texture_fond.setFilter(quality, quality);
		Texture_fond2 = manager.get("pictures/fond2.png");
		Texture_fond2.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		Texture_fond2.setFilter(quality, quality);
		Atlas_images = manager.get("textures/images.pack");
		Skin_images = manager.get("textures/images.json");
		Gdx.app.debug("xplorateur-AssetLoader", "Definition des constantes");
		Gdx.app.debug("xplorateur-AssetLoader", "Ajout de la gestion des tooltips");
		Tooltipmanager = new TooltipManager();
		Gdx.app.debug("xplorateur-AssetLoader", "Mise en place de la base de donnée");
		Datahandler = new DatabaseManager();
		Datahandler.RegisterBackend(LocalBase.class);
		Datahandler.RegisterBackend(SqlBase.class);
		Databasemanagerfrompref();
	}

	public static void Databasemanagerfrompref() {
		Datahandler.CloseAll();
		if (Datahandler.Attach(datatype.cache,
				Preference.prefs.getString("cachedata")))
			Gdx.app.debug("xplorateur-AssetLoader", "Base user ok");
		else
			Gdx.app.debug("xplorateur-AssetLoader", "Base user erreur");
		if (Datahandler.Attach(datatype.waypoint,
				Preference.prefs.getString("waypointdata")))
			Gdx.app.debug("xplorateur-AssetLoader", "Base stat ok");
		else
			Gdx.app.debug("xplorateur-AssetLoader", "Base stat erreur");
		if (Datahandler.Attach(datatype.patrimoine,
				Preference.prefs.getString("patrimoinedata")))
			Gdx.app.debug("xplorateur-AssetLoader", "Base jeu ok");
		else
			Gdx.app.debug("xplorateur-AssetLoader", "Base jeu erreur");
	}
	
	public static void load() {
		Gdx.app.debug("xplorateur-AssetLoader", "Ajout de la gestion des locales");
		FileHandle baseFileHandle = Gdx.files.internal("i18n/messages/messages");
		usa = I18NBundle.createBundle(baseFileHandle, new Locale("en"));
		french = I18NBundle.createBundle(baseFileHandle, new Locale("fr"));
		if (Preference.prefs.getBoolean("Language"))
			language = french;
		else
			language = usa;
		I18NBundle.setExceptionOnMissingKey(true);
		
		Gdx.app.debug("xplorateur-AssetLoader", "Réglage de la qualité des textures");
		quality = MenuScreen.quality.values()[Preference.prefs.getInteger("Quality")].getQuality();
		Gdx.app.debug("xplorateur-AssetLoader", "Chargements des éléments minimalistes");
		Texture_logo = new Texture(Gdx.files.internal("pictures/logo.png"),
				quality == TextureFilter.MipMap);
		Texture_logo.setFilter(quality, quality);
		emptyT = new Texture(Gdx.files.internal("pictures/empty.png"),
				quality == TextureFilter.MipMap);
		emptyT.setFilter(quality, quality);
		fullT = new Texture(Gdx.files.internal("pictures/full.png"),
				quality == TextureFilter.MipMap);
		fullT.setFilter(quality, quality);
		empty = new NinePatch(new TextureRegion(emptyT, 24, 24), 8, 8, 8, 8);
		full = new NinePatch(new TextureRegion(fullT, 24, 24), 8, 8, 8, 8);
	}


	public static void dispose() {
		Texture_logo.dispose();
		Texture_fond.dispose();
		Skin_images.dispose();
		Atlas_images.dispose();
	}
}


