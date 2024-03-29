package com.example.pathy;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pathy.aStar.Node;

public class LocationCoords implements LocationListener {

    Node currentNode = new Node(40, 51); // The node you're currently at
    MapPanning mp;
    public LocationCoords(Context context, MainActivity that) {
        this.mp = that.findViewById(R.id.im_move_zoom_rotate);
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(that, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        getLocation(context, that);
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

        // Current lon and lat
        double lon = location.getLatitude();  // [sic]
        double lat = location.getLongitude(); // [sic]

        if (MappingController.isHasInit()) {
            currentNode = MappingController.coordToNode(lon, lat);
            MapPanning.currentNode = currentNode;
            mp.postInvalidate();
            if(SearchBar.currentRouteQuery!=null) {
                MapPanning.drawPoints = MappingController.getPathBetween(MappingController.snapNearestNode(MainActivity.location.currentNode), SearchBar.currentRouteQuery);
            }
        }

        Log.d("LOCATION", "Lon: " + lon + ", Lat: " + lat);

    }

    private void getLocation(Context context, MainActivity that) {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this); // 500 = min time between updates, .5 = .5m min distance between updates
            }
            else {
                Log.e("GET LOCATION", "Big issues. Line 48 or so");
            }
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