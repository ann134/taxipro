package ru.sigmadigital.taxipro.fragments.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.adapters.OrderAddressesAdapter;
import ru.sigmadigital.taxipro.api.MyLocationListener;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogArrival;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogNavigator;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogOrderDone;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogRecalculateOrder;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.Geo;
import ru.sigmadigital.taxipro.models.Ok;
import ru.sigmadigital.taxipro.models.Order;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.repo.FeedRepository;
import ru.sigmadigital.taxipro.repo.OrdersRepository;
import ru.sigmadigital.taxipro.util.CallUtil;
import ru.sigmadigital.taxipro.util.DateFormatter;


public class OrderFragment extends Fragment implements View.OnClickListener, BottomBarFragment.OnButtonClickListener, DialogOrderDone.DialogRateListener, DialogRecalculateOrder.DialogRecalculateListener, DialogArrival.ArrivalDialogListener, DialogNavigator.NavigatorDialogListener {

    private BottomBarFragment bottomBarFragment;
    private PaymentFragment paymentFragment;

    private Order.DriverOrder order;
    private Order.OrderDriverDistance distance;

    private TextView time;


    //live data
    public static MutableLiveData<Order.DriverOrder> liveData = new MutableLiveData<>();

    public static MutableLiveData<Order.DriverOrder> getLiveData() {
        return liveData;
    }

    private static void setLiveData(Order.DriverOrder revenue) {
        liveData.setValue(revenue);
    }


    private TextView title;
    private RecyclerView recyclerView;


    public static OrderFragment newInstance(Order.DriverOrder order/*, Order.OrderDriverDistance distance*/) {
        Bundle args = new Bundle();
        args.putSerializable("order", order);
        //args.putSerializable("distance", distance);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("order")) {
                order = (Order.DriverOrder) getArguments().getSerializable("order");
            }

            if (getArguments().containsKey("distance")) {
                distance = (Order.OrderDriverDistance) getArguments().getSerializable("distance");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);

        //tittle bar
        title = v.findViewById(R.id.title);
        v.findViewById(R.id.back_button).setOnClickListener(this);
        time = v.findViewById(R.id.when);
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(App.getAppContext(), 1));

        init();
        subscribeToResponces();

        return v;
    }



    private void init(){
        OrderFragment.setLiveData(order);
        //tittle bar
        title.setText("заказ");
        title.setTextColor(getResources().getColor(R.color.colorAccent));


        //block 1
        setTimeWarning();
        OrderAddressesAdapter adapter = new OrderAddressesAdapter(order, distance);
        recyclerView.setAdapter(adapter);


        //block 2
        loadFragment(ChipCloudFragment.newInstance(), R.id.block2_content);

        //loadFragment(CurrentTripFragment.newInstance(), R.id.block2_content);

        //block 3
        paymentFragment = PaymentFragment.newInstance();
        loadFragment(paymentFragment, R.id.block3_content);

        //bottom nav
        bottomBarFragment = BottomBarFragment.newInstance(this);
        loadFragment(bottomBarFragment, R.id.bottom_bar_container);
    }



    private void subscribeToResponces() {
        LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(@Nullable ReceivedResponse responce) {
                if (responce != null) {
                    onOrderReceived(responce);
                }
            }
        });
    }

    private<T> void onOrderReceived(ReceivedResponse responce) {
        if (responce.getSenderFragment() != null && responce.getSenderFragment().equals(this.getClass().getSimpleName()) && responce.getData() != null) {

            if (responce.getData().getClass().equals(Order.DriverOrder.class)) {
                order = (Order.DriverOrder) responce.getData();
                init();
                Log.e("orderstate", order.getState()+"");


                if (order.getState() == Order.OrderState.Appointed || order.getState() == Order.OrderState.Confirmed){
                    FeedRepository.getInstance().addOrder((Order.DriverOrder) responce.getData());

                }

                return;
            }

            if (responce.getData().getClass().equals(Ok.class)) {
                Log.e("Ok", "Ok");
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

            Log.e("unknownDataOrderr", responce.getData().getClass().toString());
        }
    }



    @Override
    public void OnButtonClick(String label) {
        switch (label) {

            case BottomBarFragment.BUTTON_CALL_CLIENT: {
                callToClient();
                //loadFragment(CurrentTripFragment.newInstance(), R.id.block2_content);
                //showArrivalDialog();
                break;
            }
            case BottomBarFragment.BUTTON_CALL_DISPETCHER: {
                callToOperator();

                break;
            }
            case BottomBarFragment.BUTTON_CANCEL: {
                /*bottomBarFragment.stateTimer();
                showOrderDoneDialog();*/
                removeDriver();

                /*boolean b = OrdersRepository.getInstance().removeCompleteOrder(order);
                Log.e("Removing order", String.valueOf(b));
                */
                break;
            }


            case BottomBarFragment.BUTTON_APPOINT: {
                showArrivalDialog();
                /*loadFragment(ChipCloudFragment.newInstance(), R.id.block2_content);
                showOrderRecalculateDialog();*/
                break;
            }
            case BottomBarFragment.BUTTON_CONFIRM: {
                confirm();
                break;
            }
            case BottomBarFragment.BUTTON_DRIVER_ARRIVE: {
                driverArrive();
                break;
            }
            case BottomBarFragment.BUTTON_PICKUP_CLIENT: {
                driverPickupClient();
                break;
            }
            case BottomBarFragment.BUTTON_WAIT_FOR_COMPLETION: {
                boolean b = OrdersRepository.getInstance().removeCompleteOrder(order);
                Log.e("Removing order", String.valueOf(b));
                showOrderDoneDialog();
                waitForCompletion();
                break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button: {
                if (getActivity() != null)
                    getActivity().finish();
                break;
            }
        }
    }


    //dialogs
    private void showOrderDoneDialog() {
        DialogOrderDone dialogFragment = DialogOrderDone.newInstance(this);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        if (getFragmentManager() != null)
            dialogFragment.show(getFragmentManager(), "Dialog");
    }

    private void showOrderRecalculateDialog() {
        DialogRecalculateOrder dialogFragment = DialogRecalculateOrder.newInstance(this);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        if (getFragmentManager() != null)
            dialogFragment.show(getFragmentManager(), "Dialog");
    }

    private void showArrivalDialog() {
        DialogArrival dialogFragment = DialogArrival.newInstance(this);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        if (getFragmentManager() != null)
            dialogFragment.show(getFragmentManager(), "Dialog");
    }

    private void showNavigatorDialog() {
        DialogNavigator dialogFragment = DialogNavigator.newInstance(this);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        if (getFragmentManager() != null)
            dialogFragment.show(getFragmentManager(), "Dialog");
    }


    @Override
    public void onRateClick(int rate) {
        Log.e("rate", "" + rate);
        complete(rate);
    }

    @Override
    public void onRecalculateClick() {
        Log.e("onRecalculateClick", "onRecalculateClick");
    }

    @Override
    public void onArrivalSelected(int time) {
        //Log.e("onArrivalSelected", time + "");
        appointDriver(time);
    }

    @Override
    public void onNavigatorSelected(int status) {
        Log.e("onNavigatorSelected", status + "");
    }


    //requests
    private void callToClient(){
        if (order.getClientPhone() != null && !order.getClientPhone().equals("")){
            CallUtil.call(getActivity(), order.getClientPhone());
        } else {
            Sender.getInstance().send("order.callToClient", new Order.CallToClient(order.getId(), new Geo(MyLocationListener.getInstance().getLocation())).toJson(), this.getClass().getSimpleName());
        }
    }

    private void callToOperator(){
        if (order.getBranchPhone() != null && !order.getBranchPhone().equals("")){
            CallUtil.call(getActivity(), order.getClientPhone());
        } else {
            Sender.getInstance().send("order.callToOperator", new Order.CallToOperator(order.getId(), new Geo(MyLocationListener.getInstance().getLocation())).toJson(), this.getClass().getSimpleName());
        }
    }

    private void appointDriver(int time){
        Order.AppointDriver appoint = new Order.AppointDriver(order.getId(), new Geo(MyLocationListener.getInstance().getLocation()), time);
        Sender.getInstance().send("order.appointDriver", appoint.toJson(), this.getClass().getSimpleName());
    }

    private void confirm(){
        Sender.getInstance().send("order.confirm", new Order.Confirm(order.getId(), new Geo(MyLocationListener.getInstance().getLocation())).toJson(), this.getClass().getSimpleName());
    }

    private void removeDriver(){
        Sender.getInstance().send("order.removeDriver", new Order.RemoveDriver(order.getId(), new Geo(MyLocationListener.getInstance().getLocation())).toJson(), this.getClass().getSimpleName());
    }

    private void driverArrive(){
        Sender.getInstance().send("order.driverArrive", new Order.DriverArrive(order.getId(), new Geo(MyLocationListener.getInstance().getLocation())).toJson(), this.getClass().getSimpleName());
    }

    private void driverPickupClient(){
        Sender.getInstance().send("order.driverPickupClient", new Order.DriverPickupClient(order.getId(), new Geo(MyLocationListener.getInstance().getLocation())).toJson(), this.getClass().getSimpleName());
    }


    private void waitForCompletion(){
        Sender.getInstance().send("order.waitForCompletion", new Order.WaitForCompletion(order.getId(), new Geo(MyLocationListener.getInstance().getLocation())).toJson(), this.getClass().getSimpleName());
    }

    private void complete(int rating){
        Sender.getInstance().send("order.complete", new Order.Complete(order.getId(),rating, new Geo(MyLocationListener.getInstance().getLocation())).toJson(), this.getClass().getSimpleName());
    }




    private void setTimeWarning() {

        if (order.getPickupTime() != null){
            Date orderDate = DateFormatter.getDateFromStringUTC(order.getPickupTime());
            Date nowDate = new Date();

            if ((0 < DateFormatter.getMinutesDifference(nowDate, orderDate)) && (DateFormatter.getMinutesDifference(nowDate, orderDate) < 60)){
                time.setText("через " + DateFormatter.getMinutesDifference(nowDate, orderDate) + " мин");
                return;
            }

            if (DateFormatter.isSameDay(nowDate, orderDate)){
                time.setText("сегодня в " + DateFormatter.getSimpleTime(orderDate) + "мин");
                return;
            }

            if (DateFormatter.isSameDay(DateFormatter.datePlusDays(nowDate, 1), orderDate)){
                time.setText("завтра в " + DateFormatter.getSimpleTime(orderDate) + "мин");
                return;
            }

            time.setText(DateFormatter.getSimpleDate(orderDate)+" " + DateFormatter.getSimpleTime(orderDate));
        }


        //Log.e("date", DateFormatter.getStringUTCFromDate(orderDate));



    }




    private void loadFragment(Fragment fragment, int fragmentContainer) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentContainer, fragment);
            fragmentTransaction.commit();
        }
    }


}
