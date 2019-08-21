package fr.meconnu.location;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import android.content.Context;
import fr.meconnu.app.AndroidLauncher;
import fr.meconnu.cache.Location;

public class AndroidLocation extends Location {
	private Context context;
	private AndroidGPS gps;
	
	public AndroidLocation() {
		this.context = AndroidLauncher.gettheApplication().getContext();
		this.gps = new AndroidGPS(this.context);
	}
	
	public boolean isLocalisable() {
		return this.gps.canGetLocation;
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
	
	public float getAccuracy() {
	   return this.gps.accuracy;
	}
	
	public float getSpeed() {
		   return this.gps.speed;
		}
	
	public Vector3 getLocation() {
		//android.location.Location location=this.gps.getLocation();
		return new Vector3((float)this.gps.latitude,(float)this.gps.longitude,(float)this.gps.altitude);
	}
	
	public Vector2 get2DLocation() {
		//android.location.Location location=this.gps.getLocation();
		return new Vector2((float)this.gps.latitude,(float)this.gps.longitude);
	}
}
