package fr.meconnu.app;

import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.oscim.android.gl.AndroidGL;
import org.oscim.backend.GLAdapter;
import org.oscim.gdx.GdxAssets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import fr.meconnu.app.Xplorateur;
import fr.meconnu.location.AndroidLocation;
import fr.meconnu.cache.Location;


public class AndroidLauncher extends AndroidApplication {
    private static AndroidApplication sApplication;

    public static AndroidApplication gettheApplication() {
        return sApplication;
    }
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		AndroidLauncher.sApplication=(AndroidApplication)this;
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		config.useAccelerometer = true;
	    config.useCompass = true;
	    config.useGyroscope = true;
		//config.hideStatusBar = true; //set to true by default
	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			getWindow().getDecorView().setSystemUiVisibility(View.STATUS_BAR_VISIBLE);
			getWindow().getDecorView().setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
		}
	    initVtm();
		initialize(new Xplorateur((Location)new AndroidLocation()), config);
	}

    protected void onStart() {
        super.onStart();
        Gdx.input.setCatchBackKey(true);
    }
    
    
    public static void initVtm() {
    	org.oscim.android.canvas.AndroidGraphics.init();
        GdxAssets.init("");
        GLAdapter.init(new AndroidGL());
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.stencil = 8;
        config.numSamples = 2;
        new SharedLibraryLoader().load("vtm-jni");
	}
   
}
