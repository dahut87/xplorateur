package fr.meconnu.wrapper;

import java.util.Locale;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.speech.tts.TextToSpeech;
import fr.meconnu.app.AndroidLauncher;
import fr.meconnu.app.Wrapper;

public class Android extends Wrapper {
	private Context context;
	private GPS gps;
	private TextToSpeech tts;
	private int speech;
	
	public Android() {
		this.context = AndroidLauncher.gettheApplication().getContext();
		this.gps = new GPS(this.context);
		tts=new TextToSpeech(this.context, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
            	speech=tts.setLanguage(Locale.FRANCE);
            }
        });
	}

	public Activity getActivity(Context context)
	{
		if (context == null)
		{
			return null;
		}
		else if (context instanceof ContextWrapper)
		{
			if (context instanceof Activity)
			{
				return (Activity) context;
			}
			else
			{
				return getActivity(((ContextWrapper) context).getBaseContext());
			}
		}

		return null;
	}
	
	@Override
	public boolean isLocalisable() {
		return this.gps.canGetLocation;
	}
	
	@Override
	public boolean hasGPS() {
		return this.gps.isGPSEnabled;
	}

	@Override
    public void speak(String text)
    {
    	 if (tts.isSpeaking())
    	 	tts.stop();
		 if(speech!=TextToSpeech.LANG_MISSING_DATA && speech!=TextToSpeech.LANG_NOT_SUPPORTED){
			 		tts.speak(text, TextToSpeech.QUEUE_ADD, null);
         }
    }
	
	@Override
	public Localisationtype getLocalisationtype() {
	    if (this.gps.isGPSEnabled == true) 
	    	return Localisationtype.GPS;
	    else if (this.gps.isNetworkEnabled == true) 
	    	return Localisationtype.NETWORK;
	    else
	    	return Localisationtype.NONE;
	}
	
	@Override
	public float getAccuracy() {
	   return this.gps.accuracy;
	}
	
	@Override
	public float getSpeed() {
		   return this.gps.speed;
		}
	
	@Override
	public Vector3 getLocation() {
		//android.location.Location location=this.gps.getLocation();
		return new Vector3((float)this.gps.latitude,(float)this.gps.longitude,(float)this.gps.altitude);
	}
	
	@Override
	public Vector2 get2DLocation() {
		//android.location.Location location=this.gps.getLocation();
		return new Vector2((float)this.gps.latitude,(float)this.gps.longitude);
	}
}
