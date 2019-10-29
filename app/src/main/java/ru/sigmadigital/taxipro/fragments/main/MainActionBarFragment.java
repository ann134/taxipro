package ru.sigmadigital.taxipro.fragments.main;

import android.content.Intent;
import android.os.Bundle;
/*import android.util.Log;*/
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.activities.OrderActivity;
import ru.sigmadigital.taxipro.activities.SettingsActivity;
import ru.sigmadigital.taxipro.adapters.MessageAdapter;
import ru.sigmadigital.taxipro.adapters.PreOrdersAdapter;
import ru.sigmadigital.taxipro.api.MyLocationListener;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogStatus;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogTransparentButton;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.Geo;
import ru.sigmadigital.taxipro.models.GeoAddress;
import ru.sigmadigital.taxipro.models.Ok;
import ru.sigmadigital.taxipro.models.Order;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.repo.FeedRepository;
import ru.sigmadigital.taxipro.repo.ProfileRepository;
import ru.sigmadigital.taxipro.util.DpPxUtil;

public class MainActionBarFragment extends BaseNavigatorFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, DialogStatus.StatusDialogListener, DialogTransparentButton.DialogListener, PreOrdersAdapter.OnOrderClickListener {

    //SlidingUpPanel
    private TextView messCount;
    private TextView btnClear;
    private TextView btnShowAllMess;
    private RecyclerView rvMessages;
    private RecyclerView rvPreOrders;
    private LinearLayout orderContainer;

    SlidingUpPanelLayout sl;
    View transView;


    //action bar
    private TextView status;
    private Spinner titleSpinner;
    private String currentTitle;
    private Bundle saveState;


    //states sender fragment
    String goOnlineSender = "GoOnline";
    String goOfflineSender = "GoOffline";
    static String goHomeSender = "GoOnHome";


    public static MainActionBarFragment newInstance() {
        return new MainActionBarFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_action_bar, container, false);

        tryToRestoreData(savedInstanceState);
        //tryToRestoreData(saveState);


        initToolbar(view);
        initSlidingUpPanel(view, inflater);


        subscribeToProfile();
        subscribeToFeed(inflater);
        subscribeToResponces();

        return view;
    }

    private void tryToRestoreData(Bundle bundle){
        //Log.e("tryToRestoreData", currentTitle+"");
        if (bundle != null){
            //Log.e("bundle != null", currentTitle+"");
            if (bundle.containsKey("title")){
                currentTitle = bundle.getString("title");
                //Log.e("containsKey", currentTitle+"");
            }
        }
    }


    //action bar
    void initToolbar(View v) {

        //status
        status = v.findViewById(R.id.status);
        status.setOnClickListener(this);
        //status.setText(R.string.status_home);

        //title spinner
        titleSpinner = v.findViewById(R.id.title_spinner);

        List<String> titles = new ArrayList<>();
        titles.add("карта");
        titles.add("районы");
        titles.add("список");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_main_item, titles);
        adapter.setDropDownViewResource(R.layout.spinner_main_dropdown);
        titleSpinner.setAdapter(adapter);
        titleSpinner.setOnItemSelectedListener(this);

        for (int i = 0; i< titles.size() ; i++){
            if (titles.get(i).equals(currentTitle)) {
                titleSpinner.setSelection(i);
                break;
            }
        }

        //settings
        ImageView settings = v.findViewById(R.id.settings);
        settings.setOnClickListener(this);
    }

    //SlidingUpPanel
    private void initSlidingUpPanel(View v, LayoutInflater inflater){
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
        rvMessages = v.findViewById(R.id.rv_message);
        rvPreOrders = v.findViewById(R.id.rv_pre_order);
        orderContainer = v.findViewById(R.id.container_layout);


        /*MessageAdapter messageAdapter = new MessageAdapter();
        rvMessages.setLayoutManager(new LinearLayoutManager(getValue()));
        rvMessages.setAdapter(messageAdapter);*/

        rvPreOrders.setLayoutManager(new LinearLayoutManager(getActivity()));



        sl.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //Log.e("State", sl.getPanelState().name());
                if(previousState != SlidingUpPanelLayout.PanelState.COLLAPSED && newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
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


    @Override
    public void onResume() {
        super.onResume();
    }


    private void subscribeToFeed(final LayoutInflater inflater) {
        final LiveData<Driver.Feed> liveData = FeedRepository.getInstance().getFeedLiveData();
        liveData.observe(this, new Observer<Driver.Feed>() {
            @Override
            public void onChanged(Driver.Feed feed) {
               setData(feed, inflater);
            }
        });
    }


    private void setData(Driver.Feed feed , final LayoutInflater inflater){
        int count = feed.getOrders().size() + feed.getPreOrders().size();
        messCount.setText(count+"");
        orderContainer.removeAllViews();

        if (feed.getOrders().size() > 0){
            View view = inflater.inflate(R.layout.item_current_order, null);
            TextView address = view.findViewById(R.id.tv_address);
            address.setText(feed.getOrders().get(0).getRouteItems().get(0).getTitle());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActionBarFragment.this.onOrderClick(feed.getOrders().get(0));
                }
            });
            orderContainer.addView(view);
        }


        if (feed.getOrders().size() > 1){
            View view2 = inflater.inflate(R.layout.item_current_order, null);
            TextView address = view2.findViewById(R.id.tv_address);
            address.setText(feed.getOrders().get(1).getRouteItems().get(0).getTitle());
            view2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActionBarFragment.this.onOrderClick(feed.getOrders().get(1));
                }
            });
            orderContainer.addView(view2);
        }

        PreOrdersAdapter preOrdersAdapter = new PreOrdersAdapter(this, feed.getPreOrders());
        rvPreOrders.setAdapter(preOrdersAdapter);


               /* View view2 = inflater.inflate(R.layout.item_current_order, null);
                orderContainer.addView(view2);*/
    }



    private void subscribeToProfile() {
        final LiveData<Driver.Profile> liveData = ProfileRepository.getInstance().getProfileLiveData();
        liveData.observe(this, new Observer<Driver.Profile>() {
            @Override
            public void onChanged(Driver.Profile profile) {

                switch (profile.getState()) {
                    case Driver.DriverState.online: {
                        status.setText(R.string.status_on);
                        break;
                    }
                    case Driver.DriverState.offline: {
                        status.setText(R.string.status_off);
                        break;
                    }
                    case Driver.DriverState.onWayHome: {
                        status.setText(R.string.status_home);
                        break;
                    }
                    case Driver.DriverState.created: {
                        status.setText("создан");
                        break;
                    }
                    case Driver.DriverState.moderating: {
                        status.setText("на модерации");
                        break;
                    }
                    case Driver.DriverState.blocked: {
                        status.setText("блокирован");
                        break;
                    }
                    case Driver.DriverState.fired: {
                        status.setText("уволен");
                        break;
                    }
                }
            }
        });
    }

    //requests
    private void subscribeToResponces() {

        LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(@Nullable ReceivedResponse responce) {
                if (responce != null) {
                    onDataReceived(responce);
                }
            }
        });
    }

    private void onDataReceived(ReceivedResponse responce) {
        if (responce.getData() != null && responce.getSenderFragment() != null) {

            /*if (responce.getData().getClass().equals(Driver.StateHasBeenModifiedAsync.class)) {
                Log.e("StateHasBeenModified", "StateHasBeenModifiedAsync");
                ProfileRepository.getInstance().setStateFromAsync(((Driver.StateHasBeenModifiedAsync) responce.getData()));
                return;
            }*/

            if (responce.getSenderFragment().equals(this.getClass().getSimpleName())) {

                if (responce.getData().getClass().equals(Ok.class)) {

                    if (responce.getSenderRequest().equals(goOnlineSender)) {
                        ProfileRepository.getInstance().setStateOnline();
                        Log.e("Ok", "goOnlineSender");
                        return;
                    }

                    if (responce.getSenderRequest().equals(goOfflineSender)) {
                        ProfileRepository.getInstance().setStateOffline();
                        Log.e("Ok", "goOfflineSender");
                        return;
                    }

                    if (responce.getSenderRequest().equals(goHomeSender)) {
                        ProfileRepository.getInstance().setStateHome();
                        Log.e("Ok", "goHomeSender");
                        return;
                    }

                }

                if (responce.getData().getClass().equals(Error.class)) {
                    Log.e("Error", "Error");
                    Toast.makeText(App.getAppContext(), ((Error) responce.getData()).getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.e("unknownDataMainFrBar", responce.getData().getClass().toString());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.status: {
                showStatusDialog();
                break;
            }

            case R.id.settings: {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    //title spinner item click
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: {
                showFragment(MapFragment.newInstance());
                break;
            }
            case 1: {
                AreasTabsFragment fragment = AreasTabsFragment.newInstance();
                showFragment(fragment);

                break;
            }
            case 2: {
                showFragment(OrdersTabsFragment.newInstance());
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void showFragment(Fragment fragment) {
        replaceCurrentFragmentWith(getFragmentManager(),
                R.id.main_container,
                fragment,
                false);
    }

    //status item click
    @Override
    public void onStatusSelected(int status) {
        switch (status) {
            case 100: {
                sendGoOnline();
                break;
            }
            case 0: {
                sendGoOffline();
                break;
            }
            case 400: {
                sendGoHome();
                break;
            }
        }
    }

    private void sendGoOnline(){
        Driver.GoOnline goOnline = new Driver.GoOnline(new Geo(MyLocationListener.getInstance().getLocation()));
        Sender.getInstance().send("driver.goOnline", goOnline.toJson(), this.getClass().getSimpleName() + " " + goOnlineSender);
    }

    private void sendGoOffline(){
        Driver.GoOffline goOffline = new Driver.GoOffline(new Geo(MyLocationListener.getInstance().getLocation()));
        Sender.getInstance().send("driver.goOffline", goOffline.toJson(), this.getClass().getSimpleName() + " " + goOfflineSender);
    }

    private void sendGoHome(){

        GeoAddress geoAddressHome;
        if (ProfileRepository.getInstance().getProfile().getSettings().getHomeAddressGeo() != null){
            geoAddressHome = ProfileRepository.getInstance().getProfile().getSettings().getHomeAddressGeo();
        } else if (ProfileRepository.getInstance().getGeoAddressHome() != null) {
            geoAddressHome = ProfileRepository.getInstance().getGeoAddressHome();
        } else {
            showDialogHome();
            return;
        }

        Driver.GoHome goHome = new Driver.GoHome(new Geo(MyLocationListener.getInstance().getLocation()), geoAddressHome);
        Sender.getInstance().send("driver.goHome", goHome.toJson(), this.getClass().getSimpleName() + " " + goHomeSender);
    }


    private void showStatusDialog() {
        DialogStatus dialogFragment = DialogStatus.newInstance(this);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        if (getFragmentManager() != null)
            dialogFragment.show(getFragmentManager(), "DialogInfoTwo");
    }

    private void showDialogHome() {
        DialogTransparentButton dialogFragment = DialogTransparentButton.newInstance(this,
                R.drawable.ic_warning,
                getString(R.string.no_adress),
                getString(R.string.no_adress_descr),
                getString(R.string.cancell),
                "указать");
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        if (getFragmentManager() != null)
            dialogFragment.show(getFragmentManager(), "DialogInfoTwo");
    }


    @Override
    public void onActionClick(int action) {
        replaceCurrentFragmentWith(getFragmentManager(),
                getParentContainer(this.getView()),
                HomeAddressFragment.newInstance(true),
                true);
    }


    @Override
    public void onPause() {
        super.onPause();
       /* Log.e("pause", "pause");
        saveState = new Bundle();
        saveState.putString("title", titleSpinner.getSelectedItem().toString());*/
    }

    /*@Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("onSaveInstanceState", "onSaveInstanceState");
        outState.putString("title", titleSpinner.getSelectedItem().toString());
    }*/


    @Override
    public void onOrderClick(Order.DriverOrder order) {
        if (getActivity() != null) {

            Intent intent = new Intent(getActivity(), OrderActivity.class);
            intent.putExtra("order", order);
            //intent.putExtra("distance", distance);
            startActivity(intent);
        }
    }


}