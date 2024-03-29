package fr.meconnu.wrapper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContextWrapper;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPS extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    public boolean isGPSEnabled;

    // flag for network status
    public boolean isNetworkEnabled;

    public boolean canGetLocation;

    public Location location; // location
    public double latitude, longitude, altitude;
    public float accuracy, speed;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 125; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPS(Context context) {
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
            this.canGetLocation = false;
            if (!isGPSEnabled && !isNetworkEnabled) {
               return;
            } else {
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
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
            if (location.hasAltitude())
            	altitude = location.getAltitude();
            else
            	altitude = -1;
            if (location.hasAccuracy())
            	accuracy = location.getAccuracy();
            else
            	accuracy = -1;
            if (location.hasSpeed())
            	speed = location.getSpeed();
            else
            	speed = -1;
            if (location.getExtras()!=null)
            {
            	int satellite = location.getExtras().getInt("satellites", -1);
            	Log.d("GPS SAT", String.valueOf(satellite));
            	if (satellite>-1 && accuracy==-1)
            		if (satellite<3)
            			accuracy=50;
            		else
            			accuracy=(satellite-4)/10*50;
            	if (satellite < 3) 
            		canGetLocation=false;
            	else
            		canGetLocation=true;
    			}
    		}	
            else 
            	canGetLocation=false;
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