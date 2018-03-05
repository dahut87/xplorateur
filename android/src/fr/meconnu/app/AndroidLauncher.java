package fr.meconnu.app;

import android.app.Application;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
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
		AndroidLauncher.sApplication=(AndroidApplication)this;
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = true;
	    config.useCompass = true;
	    config.useGyroscope = true;
		initialize(new Xplorateur((Location)new AndroidLocation()), config);
	}
}
