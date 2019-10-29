package ru.sigmadigital.taxipro.view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.IOrientationConsumer;
import org.osmdroid.views.overlay.compass.IOrientationProvider;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import java.util.List;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.BuildConfig;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.util.DpPxUtil;

public class Map implements IOrientationConsumer, MapEventsReceiver {

    private MapView map;

    private MapTapListener tapListener;

    private DirectedLocationOverlay myLocation;
    private CompassOverlay compassOverlay;


    private boolean mapRotate = true;


    public Map(MapView map) {
        this.map = map;
        initMap();
    }

    private void initMap() {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(false);

        //touch
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(map.getContext(), this);
        map.getOverlays().add(0, mapEventsOverlay);


        compassOverlay = new CompassOverlay(map.getContext(), new InternalCompassOrientationProvider(map.getContext()), map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);
    }


    public void resume() {
        map.onResume();
        if (compassOverlay.mOrientationProvider != null)
            compassOverlay.mOrientationProvider.startOrientationProvider(this);
    }

    public void pause() {
        if (compassOverlay.mOrientationProvider != null)
            compassOverlay.mOrientationProvider.stopOrientationProvider();
        map.onPause();
    }

    public void setZoom(GeoPoint geoPoint) {
        map.getController().setZoom(18);
        map.getController().setCenter(geoPoint);
    }

    public void updateMyLocation(GeoPoint geoPoint) {
        if (myLocation == null) {
            createMyLocation();
        }
        myLocation.setLocation(geoPoint);
    }

    private void createMyLocation() {
        myLocation = new DirectedLocationOverlay(map.getContext());
        //icon
        Drawable drawable = App.getAppContext().getResources().getDrawable(R.drawable.ic_warning);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, DpPxUtil.getPixelsFromDp(30), DpPxUtil.getPixelsFromDp(30), true);
        //Drawable icon = new BitmapDrawable(App.getAppContext().getResources(), Bitmap.createScaledBitmap(bitmap, DpPxUtil.getPixelsFromDp(40), DpPxUtil.getPixelsFromDp(50), true));
        myLocation.setDirectionArrow(bitmap);
        myLocation.setBearing(0);
        map.getOverlays().add(myLocation);
    }


    public void setPinsArray(List<GeoPoint> pins) {
        for (GeoPoint geoPoint : pins) {
            Marker marker = new Marker(map);
            marker.setPosition(geoPoint);
            Drawable drawable = App.getAppContext().getResources().getDrawable(R.drawable.ic_pin);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Drawable icon = new BitmapDrawable(App.getAppContext().getResources(), Bitmap.createScaledBitmap(bitmap, DpPxUtil.getPixelsFromDp(40), DpPxUtil.getPixelsFromDp(50), true));
            marker.setIcon(icon);
            map.getOverlays().add(marker);
        }
    }

    public void setMapRotation(boolean rotate){
        mapRotate = rotate;
    }

    @Override
    public void onOrientationChanged(float orientation, IOrientationProvider source) {
        if (mapRotate){
            if (myLocation != null) {
                myLocation.setBearing(-orientation);
            }
            map.setMapOrientation(orientation);
        }
    }


    public void setSelectedGeoPointListener(MapTapListener listener) {
        tapListener = listener;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        if (tapListener != null) {
            tapListener.onTap(p);

        }
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }


    public interface MapTapListener {
        void onTap(GeoPoint geoPoint);
    }


}
