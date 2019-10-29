package ru.sigmadigital.taxipro.api;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import ru.sigmadigital.taxipro.App;


public class MyLocationListener implements LocationListener {

    private static MyLocationListener locationListener;

    private Location location;

    private MutableLiveData<Location> locationLiveData = new MutableLiveData<>();
    private  MutableLiveData<Boolean> providerEnabledLiveData = new MutableLiveData<>();

    public static MyLocationListener getInstance() {
        if (locationListener == null) {
            locationListener = new MyLocationListener();
            locationListener.setUpLocationListener();
        }
        return locationListener;
    }

    private void setUpLocationListener() {
        LocationManager locationManager = (LocationManager) App.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
            providerEnabledLiveData.setValue(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
            locationLiveData.setValue(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException ex) {
            Log.e("SecurityException", "SecurityException");
            Toast.makeText(App.getAppContext(), "SecurityException", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location loc) {
        locationLiveData.setValue(loc);
        location = loc;

        Toast.makeText(App.getAppContext(), "onLocationChanged", Toast.LENGTH_LONG).show();
        Log.e("onLocationChanged", loc.toString());
    }

    @Override
    public void onProviderDisabled(String provider) {
        providerEnabledLiveData.setValue(false);
        //locationManagerLD.setValue(locationManager);
        Log.e("onProviderDisabled", provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        providerEnabledLiveData.setValue(true);
        //locationManagerLD.setValue(locationManager);
        Log.e("onProviderEnabled", provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e("onStatusChanged", "onStatusChanged");
        //locationManagerLD.setValue(locationManager);
    }


    public MutableLiveData<Location> getLocationLiveData() {
        return locationLiveData;
    }

    public MutableLiveData<Boolean> getProviderEnabledLiveData() {
        return providerEnabledLiveData;
    }


    public Location getLocation() {
        return location;
    }
}

