package fr.meconnu.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class AndroidGPS extends Service implements LocationListener {
	 
    private final Context mContext;
 
    // flag for GPS status
    public boolean isGPSEnabled;
 
    // flag for network status
    public boolean isNetworkEnabled;
 
    public boolean canGetLocation;
 
    public Location location; // location
    public double latitude; // latitude
    public double longitude; // longitude
 
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 125; // 1 minute
 
    // Declaring a Location Manager
    protected LocationManager locationManager;
 
    public AndroidGPS(Context context) {
        this.mContext = context;
        initLocation();
    }
    
    public void initLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
 
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) {
                this.canGetLocation = false;
                // no network provider is enabled
            } else {
                this.canGetLocation = false;
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                this.canGetLocation = true;
                            }
                        }
                    }
                }
                else if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            this.canGetLocation = true;
                        }
                    }
                }

                
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
    	if (isGPSEnabled) {
    		if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            int satellite = location.getExtras().getInt("satellites", -1);
            Log.d("GPS SAT", String.valueOf(satellite));
            if (satellite < 4) 
            	canGetLocation=false;
            else
            	canGetLocation=true;
            Log.d("GPS SAT", String.valueOf(canGetLocation));
    		}
        else canGetLocation=false;
    	}	
    }
 
    @Override
    public void onProviderDisabled(String provider) {
    }
 
    @Override
    public void onProviderEnabled(String provider) {
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
 
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}