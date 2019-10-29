package ru.sigmadigital.taxipro.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.ArrayList;
import java.util.List;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.api.MyLocationListener;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.api.TaxiProService;
import ru.sigmadigital.taxipro.fragments.earn.EarnActionBarFragment;
import ru.sigmadigital.taxipro.fragments.main.MainNavigatorFragment;
import ru.sigmadigital.taxipro.fragments.profile.ProfileNavigatorFragment;
import ru.sigmadigital.taxipro.fragments.rate.ActivityNavigatorFragment;
import ru.sigmadigital.taxipro.models.Area;
import ru.sigmadigital.taxipro.models.AuthSession;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.Ok;
import ru.sigmadigital.taxipro.models.Order;
import ru.sigmadigital.taxipro.models.PushIdentity;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.repo.FeedRepository;
import ru.sigmadigital.taxipro.repo.OrdersRepository;
import ru.sigmadigital.taxipro.repo.ProfileRepository;
import ru.sigmadigital.taxipro.util.SettingsHelper;

public class MainActivity extends BaseActivity {

    private static MainActivity mainActivity;
    private boolean isActivityRestarted = false;

    private Handler mBackPressedHandler = new Handler();
    private boolean doubleBackToExitPressedOnce;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    private AHBottomNavigation bottomNavBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        TaxiProService.startSendMyLocation();

        //setTextSise
        App.setTextSize(this);

        /*//
        if (!TaxiProService.isServiceRun){
            TaxiProService.startTaxiProService(App.getAppContext());
        }*/


        //bottom navigation
        int position = 0;
        initBottomBar();
        if (savedInstanceState != null && savedInstanceState.containsKey("stateNavBar")) {
            position = savedInstanceState.getInt("stateNavBar");
            /*bottomNavBar.setCurrentItem(position);*/
        } else {
            /*bottomNavBar.setCurrentItem(position);
            setCurrentFragment(position);*/
        }
        bottomNavBar.setCurrentItem(position);
        setCurrentFragment(position);


        subscribeToResponces();
        subsribeToLocation();
    }


    //bottom navigation
    void initBottomBar() {
        bottomNavBar = findViewById(R.id.navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.main,
                R.drawable.ic_home_act,
                R.color.colorAccent);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.revenue,
                R.drawable.ic_snow,
                R.color.colorAccent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.activity,
                R.drawable.ic_fav,
                R.color.colorAccent);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.account,
                R.drawable.ic_client,
                R.color.colorAccent);

        bottomNavBar.addItem(item1);
        bottomNavBar.addItem(item2);
        bottomNavBar.addItem(item3);
        bottomNavBar.addItem(item4);


        bottomNavBar.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavBar.setTitleTextSizeInSp(13, 11);

        bottomNavBar.setDefaultBackgroundColor(getResources().getColor(R.color.barsBackground));
        bottomNavBar.setAccentColor(getResources().getColor(R.color.colorAccent));
        bottomNavBar.setInactiveColor(getResources().getColor(R.color.navBarInactive));

        bottomNavBar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (!wasSelected) {
                    setCurrentFragment(position);
                }
                return true;
            }
        });
    }

    private void setCurrentFragment(int position) {
        switch (position) {
            case 0: {
                showNavigatorFragment(MainNavigatorFragment.newInstance());
                break;
            }
            case 1: {
                showNavigatorFragment(EarnActionBarFragment.newInstance());
                break;
            }
            case 2: {
                showNavigatorFragment(ActivityNavigatorFragment.newInstance());
                break;
            }
            case 3: {
                showNavigatorFragment(ProfileNavigatorFragment.newInstance());
                break;
            }
        }
    }

    private void showNavigatorFragment(Fragment fragment) {
        replaceCurrentFragmentWith(getSupportFragmentManager(),
                R.id.fragment_container,
                fragment,
                false);
    }


    //requests

    private void subscribeToResponces() {
        continueSessionRequest();
        //sendRequests();

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

    private <T> void onDataReceived(ReceivedResponse responce) {
        if (responce.getData() != null && responce.getSenderFragment() != null && responce.getSenderFragment().equals(this.getClass().getSimpleName())) {

            if (responce.getData().getClass().equals(AuthSession.HasBeenContinued.class)) {
                Log.e("AuthSession", "HasBeenContinued");
                //Log.e("ProfileRepository", ProfileRepository.getInstance().getProfile() + "");

                sendRequests();

                    /*if (ProfileRepository.getInstance().getProfile() == null) {

                    }*/
                return;
            }

            if (responce.getData().getClass().equals(Driver.Profile.class)) {
                ProfileRepository.getInstance().setProfile((Driver.Profile) responce.getData());
                //Log.e("prof", "" + ProfileRepository.getInstance().getProfileLiveData().getPhone());
                return;
            }

            if (responce.getData().getClass().equals(Driver.Feed.class)) {
                //Log.e("Feed", "Feed");
                FeedRepository.getInstance().setFeed((Driver.Feed) responce.getData());
                return;
            }

            if (responce.getSenderRequest() != null && responce.getSenderRequest().equals("areaPanel")) {
                List<Area.AreaPanel> areasList = new ArrayList<>();
                Log.e("areaPanel", "areaPanel");

                if (responce.getData().getClass().equals(ArrayList.class)) {
                    ArrayList<T> list = (ArrayList<T>) responce.getData();
                    for (T item : list) {
                        if (item instanceof Area.AreaPanel) {
                            areasList.add((Area.AreaPanel) item);
                        }
                    }
                }
                OrdersRepository.getInstance().setAreasList(areasList);
                return;
            }

            if (responce.getSenderRequest() != null && responce.getSenderRequest().equals("driverOrder")) {
                List<Order.DriverOrder> ordersList = new ArrayList<>();
                Log.e("driverOrder", "driverOrder");

                if (responce.getData().getClass().equals(ArrayList.class)) {
                    ArrayList<T> list = (ArrayList<T>) responce.getData();
                    for (T item : list) {
                        if (item instanceof Order.DriverOrder) {
                            ordersList.add((Order.DriverOrder) item);
                        }
                    }
                }
                OrdersRepository.getInstance().setOrdersList(ordersList);
                return;
            }

            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("Error", "Error");
                return;
            }

            if (responce.getData().getClass().equals(Ok.class)) {
                Log.e("Ok", "Ok");
                return;
            }


            Log.e("unknownDataMain", responce.getData().getClass().toString());
        }

    }


    private void sendRequests() {
        //profile
        Sender.getInstance().send("driver.profile", new Driver.ProfileFilter().toJson(), this.getClass().getSimpleName());

        //pushregistr
        PushIdentity.Register register = new PushIdentity.Register(SettingsHelper.getToken().getEntityId(), "");
        Sender.getInstance().send("pushIdentity.register", register.toJson(), this.getClass().getSimpleName());


        //driver.feed
        Sender.getInstance().send("driver.feed", new Driver.FeedFilter().toJson(), this.getClass().getSimpleName());

        //areas
        Area.AreaPanelFilter areaPanelFilter = new Area.AreaPanelFilter(0, 999);
        Sender.getInstance().send("area.areaPanel", areaPanelFilter.toJson(), this.getClass().getSimpleName() + " areaPanel");

        //order.driverOrder
        Order.DriverOrderFilter driverOrderFilter = new Order.DriverOrderFilter(0, 999);
        Sender.getInstance().send("order.driverOrder", driverOrderFilter.toJson(), this.getClass().getSimpleName() + " driverOrder");

    }


    //location
    private void subsribeToLocation() {
        final LiveData<Boolean> liveDataProvider = MyLocationListener.getInstance().getProviderEnabledLiveData();
        liveDataProvider.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean enabled) {
                if (!enabled) {
                    startSplashActivity();
                }
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("stateNavBar", bottomNavBar.getCurrentItem());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager()
                .getBackStackEntryCount() > 0) {
            this.doubleBackToExitPressedOnce = true;
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            doubleBackToExitPressedOnce = false;
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "нажмите еще раз чтобы выйти", Toast.LENGTH_SHORT).show();
        mBackPressedHandler.postDelayed(mRunnable, 2000);
    }


    @Override
    protected int getFragmentContainer() {
        return R.id.fragment_container;
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* if (!isActivityRestarted) {
            TaxiProService.stopTaxiProService(App.getAppContext());
        }*/
    }


    static public void startSplashActivity() {
        Intent intent = new Intent(mainActivity, SplashActivity.class);
        mainActivity.startActivity(intent);
        mainActivity.finish();
    }

    //recreate activity
    public static void recreateActivity() {
        if (mainActivity != null) {
            mainActivity.isActivityRestarted = true;
            mainActivity.recreate();
        }
    }


    public static void continueSessionRequest() {
        if (SettingsHelper.getToken() != null) {
            AuthSession.ContinueSession continueSession = new AuthSession.ContinueSession(0, SettingsHelper.getToken().getEntityId());
            Sender.getInstance().send("authSession.continueSession", continueSession.toJson(), mainActivity.getClass().getSimpleName());
        }
    }

}
