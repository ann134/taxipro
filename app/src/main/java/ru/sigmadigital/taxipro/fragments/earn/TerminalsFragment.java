package ru.sigmadigital.taxipro.fragments.earn;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.adapters.OrdersAdapter;
import ru.sigmadigital.taxipro.api.MyLocationListener;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.models.Order;
import ru.sigmadigital.taxipro.util.DpPxUtil;
import ru.sigmadigital.taxipro.view.Map;


public class TerminalsFragment extends BaseNavigatorFragment implements View.OnClickListener, OrdersAdapter.OnOrderClickListener {

    //map
    private Map map;

    //SlidingUpPanel
    private TextView messCount;
    private TextView btnClear;
    private TextView btnShowAllMess;
    private RecyclerView rvMessages;
    private RecyclerView rvPreOrders;
    private LinearLayout orderContainer;


    SlidingUpPanelLayout sl;
    View transView;

    public static TerminalsFragment newInstance() {
        return new TerminalsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_terminals, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        EarnActionBarFragment.setActionBarTitle("терминалы оплаты");
        EarnActionBarFragment.setBackButtonVisible(true);

        initSlidingUpPanel(v, inflater);

        MapView mapView = v.findViewById(R.id.map);
        map = new Map(mapView);



        List<GeoPoint> terminalsList = new ArrayList<>();
        terminalsList.add(new GeoPoint(59.930963, 30.445684));
        terminalsList.add(new GeoPoint(59.9309475, 30.428084));
        terminalsList.add(new GeoPoint(59.347345, 30.420184));
        terminalsList.add(new GeoPoint(59.9345, 30.428084));
        terminalsList.add(new GeoPoint(59.030963, 30.428084));
        terminalsList.add(new GeoPoint(59.932663, 30.45804));
        terminalsList.add(new GeoPoint(59.631263, 30.479084));
        terminalsList.add(new GeoPoint(59.130963, 30.828084));
        terminalsList.add(new GeoPoint(59.830963, 30.928084));


        map.setPinsArray(terminalsList);







        subsribeToLocation();

        return v;
    }

    //SlidingUpPanel
    private void initSlidingUpPanel(View v, LayoutInflater inflater) {
        sl = v.findViewById(R.id.slide_up_layout);
        sl.setTouchEnabled(false);
        sl.setPanelHeight(DpPxUtil.getPixelsFromDp(50));
        sl.setShadowHeight(0);
        sl.setCoveredFadeColor(android.R.color.transparent);

        transView = v.findViewById(R.id.trans_view);
        transView.getLayoutParams().height = DpPxUtil.getPixelsFromDp(50);

        messCount = v.findViewById(R.id.tv_mess_count);
        btnClear = v.findViewById(R.id.tv_clear_button);
        btnShowAllMess = v.findViewById(R.id.tv_show_all_button);

        rvPreOrders = v.findViewById(R.id.rv_pre_order);
        orderContainer = v.findViewById(R.id.container_layout);


        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");

        RecyclerView recyclerView = v.findViewById(R.id.recycler_view_terminals);
        recyclerView.setLayoutManager(new GridLayoutManager(App.getAppContext(), 1));
        OrdersAdapter adapter = new OrdersAdapter(this, new ArrayList<Order.DriverOrder>(), new ArrayList<Order.OrderDriverDistance>());
        recyclerView.setAdapter(adapter);


        sl.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.e("State", sl.getPanelState().name());
                if (previousState != SlidingUpPanelLayout.PanelState.COLLAPSED && newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    sl.setTouchEnabled(false);
                }
            }
        });

        transView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sl.setTouchEnabled(true);
                return false;
            }
        });
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
            map.updateMyLocation(geoPoint);
        }
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

    }


    @Override
    public void onOrderClick(Order.DriverOrder order, Order.OrderDriverDistance distance) {

    }
}