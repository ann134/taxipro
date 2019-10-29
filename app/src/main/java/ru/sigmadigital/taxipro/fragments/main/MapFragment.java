package ru.sigmadigital.taxipro.fragments.main;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.api.MyLocationListener;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.api.http.GeksagonsTask;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Geo;
import ru.sigmadigital.taxipro.models.http.Geksagons;
import ru.sigmadigital.taxipro.repo.ProfileRepository;
import ru.sigmadigital.taxipro.util.CallUtil;
import ru.sigmadigital.taxipro.view.Map;


public class MapFragment extends BaseNavigatorFragment implements View.OnClickListener, GeksagonsTask.OnGetGeksagonesListener {

    private Map map;

    TextView netState;
    TextView netCoords;
    TextView gpsState;
    TextView gpsCoords;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        MapView mapView = v.findViewById(R.id.map);
        map = new Map(mapView);
        v.findViewById(R.id.imv_call).setOnClickListener(this);

        netState = v.findViewById(R.id.net_state);
        netCoords = v.findViewById(R.id.net_coords);
        gpsState = v.findViewById(R.id.gps_state);
        gpsCoords = v.findViewById(R.id.gps_coords);


        subsribeToLocation();

        return v;
    }


    private void subsribeToLocation() {
        final LiveData<Location> liveData = MyLocationListener.getInstance().getLocationLiveData();
        liveData.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                drawLocation(location);
                if (location != null)
                    mapChangeMyLocation(location);
            }
        });


       /* final LiveData<LocationManager> liveDataLM = MyLocationListener.getInstance().getLocationManagerLD();
        liveDataLM.observe(this, new Observer<LocationManager>() {
            @Override
            public void onChanged(LocationManager location) {
                drawStatus(location);
            }
        });*/

    }


    private void drawLocation(Location location) {
        if (location != null) {
            gpsCoords.setText(location.getLatitude() + "," + location.getLongitude());
        } else {
            gpsCoords.setText("нет сигнала gps");
        }
        /*if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            gpsCoords.setText(location.getLatitude() + "," + location.getLongitude());
        }
        if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            netCoords.setText(location.getLatitude() + "," + location.getLongitude());
        }*/
    }

   /* private void drawStatus(LocationManager locationManager) {
        gpsState.setText("Enabled: " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        netState.setText("Enabled: " + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }*/


    private void mapChangeMyLocation(Location location) {
        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        map.setZoom(geoPoint);
        map.updateMyLocation(geoPoint);
    }


    @Override
    public void onResume() {
        super.onResume();
        map.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.pause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_call: {

                //if (ProfileRepository.getInstance().getProfile().)

                CallUtil.call(getActivity(), "+7(999)999–99-99");

                callToOperator();

//                new GeksagonsTask(this, new Geo(MyLocationListener.getInstance().getLocation())).execute();
//                break;
            }
        }
    }


    private void callToOperator() {
        Sender.getInstance().send("driver.callToOperator", new Driver.CallToOperator(new Geo(MyLocationListener.getInstance().getLocation())).toJson(), this.getClass().getSimpleName());
    }


    @Override
    public void onGetGeksagonesResponse(List<Geksagons> geksagons) {

//            H3Core h3 = H3Core.newSystemInstance();
//            for (Geksagons geksagon : geksagons) {
//                List<GeoCoord> geoCoords = h3.h3ToGeoBoundary(geksagon.getHex());
//                for (GeoCoord geoCoord : geoCoords){
//                    Log.e("GEO_COORDS", geoCoord.lat + " / " + geoCoord.lng);
//                }
//            }


    }
}
