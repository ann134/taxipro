package ru.sigmadigital.taxipro.fragments.main;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.api.MyLocationListener;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.view.Map;

public class HomeAddressMapFragment extends BaseNavigatorFragment implements View.OnClickListener, Map.MapTapListener {

    private Map map;

    public static HomeAddressMapFragment newInstance() {
        return new HomeAddressMapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_address_map, container, false);

        //tittle bar
        TextView title = v.findViewById(R.id.tv_title);
        title.setText("домашний адрес");
        v.findViewById(R.id.imv_back_button).setOnClickListener(this);
        v.findViewById(R.id.imv_locate).setVisibility(View.GONE);


        MapView mapView = v.findViewById(R.id.map);
        map = new Map(mapView);
        map.setSelectedGeoPointListener(this);
        map.setMapRotation(false);

        subsribeToLocation();

        return v;
    }

    private void subsribeToLocation() {
        final LiveData<Location> liveData = MyLocationListener.getInstance().getLocationLiveData();
        liveData.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {

                mapChangeMyLocation(location);
            }
        });
    }

    private void mapChangeMyLocation(Location location) {
        if (location != null){
            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            map.setZoom(geoPoint);
        }
    }


    @Override
    public void onTap(GeoPoint geoPoint) {
        Log.e("tapedGeopoint", geoPoint.toString());
        Toast.makeText(getContext(), geoPoint.toString(), Toast.LENGTH_SHORT).show();
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
            case R.id.imv_back_button: {
                if (getActivity() != null)
                    getActivity().onBackPressed();
                break;
            }
        }
    }


}
