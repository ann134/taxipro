package ru.sigmadigital.taxipro.fragments.main;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.activities.OrderActivity;
import ru.sigmadigital.taxipro.adapters.AreasAdapter;
import ru.sigmadigital.taxipro.adapters.OrdersAdapter;
import ru.sigmadigital.taxipro.api.MyLocationListener;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.fragments.order.OrderFragment;
import ru.sigmadigital.taxipro.models.Area;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.Order;
import ru.sigmadigital.taxipro.models.my.DistansesInArea;
import ru.sigmadigital.taxipro.models.my.OrdersInArea;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.repo.AreaRepository;
import ru.sigmadigital.taxipro.repo.OrdersRepository;
import ru.sigmadigital.taxipro.util.DateFormatter;

public class OrdersListFragment extends BaseNavigatorFragment implements OrdersAdapter.OnOrderClickListener, View.OnClickListener {

    private Area.AreaPanel area;
    private boolean isPreorders;
    private RecyclerView recyclerView;

    DistansesInArea currentDistanses;

    /*public static OrdersListFragment newInstance() {
        return new OrdersListFragment();
    }*/

    public static OrdersListFragment newInstance(boolean isPreorders) {
        OrdersListFragment fragment = new OrdersListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isPreorders", isPreorders);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static OrdersListFragment newInstance(Area.AreaPanel area, boolean isPreorders) {
        OrdersListFragment fragment = new OrdersListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("area", area);
        bundle.putBoolean("isPreorders", isPreorders);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("area")) {
            area = (Area.AreaPanel) getArguments().getSerializable("area");
        }
        if (getArguments() != null && getArguments().containsKey("isPreorders")) {
            isPreorders = getArguments().getBoolean("isPreorders");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders_list, container, false);

        if (area != null && !area.equals("")) {
            v.findViewById(R.id.action_bar_areas).setVisibility(View.VISIBLE);
            TextView tit = v.findViewById(R.id.title_area);
            tit.setText(area.getName());
            v.findViewById(R.id.back_button).setOnClickListener(this);
        }

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(App.getAppContext(), 1));


        if (area == null) {
            if (isPreorders) {
                subscribeToOrders(OrdersRepository.getInstance().getPreOrdersLiveData());
            } else {
                subscribeToOrders(OrdersRepository.getInstance().getOrdersLiveData());
            }
        } else {
            if (isPreorders) {
                subscribeToAreaOrders(OrdersRepository.getInstance().getPreordersInAreasLiveData());
            } else {
                subscribeToAreaOrders(OrdersRepository.getInstance().getOrdersInAreasLiveData());
            }
        }

        return v;
    }


    private void subscribeToOrders(LiveData<List<Order.DriverOrder>> liveData) {
        //final LiveData<List<Order.DriverOrder>> liveData = OrdersRepository.getInstance().getOrdersLiveData();
        liveData.observe(this, new Observer<List<Order.DriverOrder>>() {
            @Override
            public void onChanged(List<Order.DriverOrder> orders) {
                /* initRecycler(orders);*/
                //sendRequest(orders);
                checkCash(orders);
            }
        });
    }

    private void subscribeToAreaOrders(LiveData<List<OrdersInArea>> liveData) {
        liveData.observe(this, new Observer<List<OrdersInArea>>() {
            @Override
            public void onChanged(List<OrdersInArea> orders) {
                for (OrdersInArea inArea : orders) {
                    if (inArea.getAreaName().equals(area.getName())) {
                        //initRecycler(inArea.getOrders());
                        //sendRequest(inArea.getOrders());
                        checkCash(inArea.getOrders());
                    }
                }
            }
        });
    }

    private void checkCash(List<Order.DriverOrder> orders) {

        currentDistanses = OrdersRepository.getInstance().getDistansesList(area, isPreorders);

        Log.e("area" , area+"");
        Log.e("isPreorders" , isPreorders+"");
        Log.e("distanses" , currentDistanses+"");

        if (currentDistanses == null) {
            sendRequest(orders);
            return;
        }

        if (!checkLocationValidate(currentDistanses.getLocation()) || !checkTimeValidation(currentDistanses.getTime())) {
            sendRequest(orders);
            return;
        }


        initRecycler(orders, currentDistanses.getDistances());

    }


    private boolean checkLocationValidate(Location savedLoc) {
        double metters = savedLoc.distanceTo(MyLocationListener.getInstance().getLocation());
        return metters < 500;
    }

    private boolean checkTimeValidation(long savedTime) {
        long fiveMinutes = 1000 * 60 * 5;
        return System.currentTimeMillis() - savedTime <= fiveMinutes;
    }


    private void sendRequest(final List<Order.DriverOrder> orders) {

        List<Integer> ordersIds = new ArrayList<>();
        for (Order.DriverOrder order : orders) {
            ordersIds.add(order.getId());
        }

        Sender.getInstance().send("order.orderDriverDistance", new Order.OrderDriverDistanceFilter(ordersIds).toJson(), this.getClass().getSimpleName()+isPreorders);

        LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(@Nullable ReceivedResponse responce) {
                if (responce != null) {
                    onDistanceReceived(responce, orders);
                }
            }
        });
    }

    private <T> void onDistanceReceived(ReceivedResponse responce, List<Order.DriverOrder> orders) {
        if (responce.getSenderFragment() != null && responce.getSenderFragment().equals(this.getClass().getSimpleName()+isPreorders) && responce.getData() != null) {

            if (responce.getData().getClass().equals(ArrayList.class)) {
                ArrayList<T> list = (ArrayList<T>) responce.getData();
                List<Order.OrderDriverDistance> distances = new ArrayList<>();
                for (T item : list) {
                    if (item instanceof Order.OrderDriverDistance) {
                        distances.add((Order.OrderDriverDistance) item);
                    }
                }

                //progressBar.setVisibility(View.GONE);

                DistansesInArea newDistanss = new DistansesInArea(area);
                newDistanss.setLocation(MyLocationListener.getInstance().getLocation());
                newDistanss.setTime(System.currentTimeMillis());
                newDistanss.setDistances(distances);
                newDistanss.setPreorder(isPreorders);
                OrdersRepository.getInstance().addDistansesList(newDistanss);


                initRecycler(orders, distances);
                return;
            }

            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("Error", "Error");
                /*progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
                Error error = (Error) responce.getData();
                errorText.setText("Ошибка:"+ error.getMessage());*/
                return;
            }

            Log.e("unknownDataOrders", responce.getData().getClass().toString());
        }
    }


    private void initRecycler(List<Order.DriverOrder> orders, List<Order.OrderDriverDistance> distances) {
        OrdersAdapter adapter = new OrdersAdapter(this, orders, distances);
        recyclerView.setAdapter(adapter);

        /*for (Order.DriverOrder o : orders){
            Log.e("time", o.getPickupTime()+"");
        }*/
    }


    @Override
    public void onOrderClick(Order.DriverOrder order, Order.OrderDriverDistance distance) {
        if (getActivity() != null) {

            Intent intent = new Intent(getActivity(), OrderActivity.class);
            intent.putExtra("order", order);
            intent.putExtra("distance", distance);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button: {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                break;
            }
        }
    }
}
