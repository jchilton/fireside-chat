package com.example.firesidechat;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


public class Location implements LocationListener {
	
	private MainActivity parent;
	private Context context;
	private LocationManager locationManager;
	private String provider;
	private String latitude;
	private String longitude;

	public Location(MainActivity parent_activity, Context applicationContext) {
		parent = parent_activity;
		context = applicationContext;
		locate();
	}

	public void locate() {
		// Get the location manager
	    locationManager = (LocationManager) context.getSystemService(parent.getApplicationContext().LOCATION_SERVICE);
	    // Define the criteria how to select the location provider -> use
	    // default
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    android.location.Location location = locationManager.getLastKnownLocation(provider);
	
	    // Initialize the location fields
	    if (location != null) {
	      System.out.println("Provider " + provider + " has been selected.");
	      onLocationChanged(location);
	    } else {
	    	Log.d("Location", "location not available");
	    }
	}

	@Override
	public void onLocationChanged(android.location.Location location) {
		int lat = (int) (location.getLatitude());
	    int lng = (int) (location.getLongitude());
	    latitude = String.valueOf(lat);
	    longitude = String.valueOf(lng);
	    Log.d("Location", latitude+", "+longitude);
	    parent.locationHandler(latitude, longitude);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
