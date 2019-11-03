package com.example.pathy;

import android.content.Context;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.example.pathy.aStar.Node;

public class LocationCoords implements LocationListener {

    Node currentNode; // The node you're currently at
    private LocationManager locationManager;

    public LocationCoords(Context context) {
        getLocation(context);
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

        double lon = location.getLatitude();  // [sic]
        // Current lon and lat
        double lat = location.getLongitude(); // [sic]

        Node currentNode = MappingController.coordToNode(lon, lat);

        Log.d("LOCATION", "Lon: " + lon + ", Lat: " + lat);

    }

    void getLocation(Context context) {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, (float) 0.5, this); // 500 = min time between updates, .5 = .5m min distance between updates
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    // Just here to make the compiler happy with us
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
}