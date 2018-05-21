package fr.meconnu.app;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
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
    private Location androidlocation;
    private boolean resultValue=false;
	AndroidApplicationConfiguration config;

    public static AndroidApplication gettheApplication() {
        return sApplication;
    }
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps"))
            if (turnGPSOn(this))
            {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                while (!Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED).contains("gps"))  {};
                restart();
            };
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		AndroidLauncher.sApplication=(AndroidApplication)this;
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		config = new AndroidApplicationConfiguration();
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
		androidlocation=new AndroidLocation();
		initialize(new Xplorateur(androidlocation), config);
	}

	public void restart() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    public boolean turnGPSOn(Context context)
    {
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message mesg)
            {
                throw new RuntimeException();
            }
        };

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final String message = "Voulez vous activer le GPS ?";
        final String title = "GPS";
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("Oui", new
                DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        resultValue = true;
                        handler.sendMessage(handler.obtainMessage());
                    }
                });
        alert.setNegativeButton("Non", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                resultValue = false;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.show();

        try{ Looper.loop(); }
        catch(RuntimeException e){}

        return resultValue;
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
