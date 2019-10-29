package ru.sigmadigital.taxipro.fragments.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.fragments.main.HomeAddressMapFragment;
import ru.sigmadigital.taxipro.models.Order;
import ru.sigmadigital.taxipro.view.ButtonOrderView;

public class BottomBarFragment extends Fragment implements View.OnClickListener {

    public final static String BUTTON_APPOINT = "APPOINT";
    public final static String BUTTON_CONFIRM = "CONFIRM";

    public final static String BUTTON_DRIVER_ARRIVE = "DriverArrive";
    public final static String BUTTON_PICKUP_CLIENT = "PICKUP_CLIENT";

    public final static String BUTTON_WAIT_FOR_COMPLETION = "WAIT_FOR_COMPLETION";
    public final static String BUTTON_COMPLETE = "COMPLETE";

    public final static String BUTTON_CALL_CLIENT = "client";
    public final static String BUTTON_CALL_DISPETCHER = "dispetcher";

    public final static String BUTTON_CANCEL = "CANCEL";

    private OnButtonClickListener listener;

    private FrameLayout bt1Container;
    private FrameLayout bt2Container;
    private FrameLayout bt3Container;
    private FrameLayout bt4Container;

    private FrameLayout timerContainer;
    private View timerView;


    public static BottomBarFragment newInstance(OnButtonClickListener listener) {
        BottomBarFragment fragment = new BottomBarFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_boottom_bar, container, false);

        bt1Container = v.findViewById(R.id.bt_1_container);
        bt2Container = v.findViewById(R.id.bt_2_container);
        bt3Container = v.findViewById(R.id.bt_3_container);
        bt4Container = v.findViewById(R.id.bt_4_container);

        timerContainer = v.findViewById(R.id.timer_frame);
        timerView = v.findViewById(R.id.timer_line);


        subscribeToLiveData();

        return v;
    }

    private void subscribeToLiveData() {
        LiveData<Order.DriverOrder> liveData = OrderFragment.getLiveData();
        liveData.observe(this, new Observer<Order.DriverOrder>() {
            @Override
            public void onChanged(@Nullable Order.DriverOrder value) {
                if (value != null) {
                    switchState(value);
                }
            }
        });
    }

    private void switchState(Order.DriverOrder order){
        switch (order.getState()) {
            case Order.OrderState.Accepted: {
                addButtonsAccepted();
                break;
            }
            case Order.OrderState.Appointed:  {
                addButtonsAppointed();
                break;
            }
            case Order.OrderState.Confirmed:  {
                addButtonsConfirmed();
                break;
            }
            case Order.OrderState.DriverArrived:  {
                addButtonsDriverArrived();
                break;
            }
            case Order.OrderState.ClientWasPickedUp:  {
                addButtonsClientWasPickedUp();
                break;
            }
            case Order.OrderState.WaitedForCompletion:  {
                addButtonsWaitedForCompletion();
                break;
            }
            case Order.OrderState.Completed:  {
                addButtonsCompleted();
                break;
            }
            case Order.OrderState.Canceled:  {
                addButtonsCanceled();
                break;
            }
        }
    }




    private void addButtonsAccepted() {
        clearContainer();
        addButton(BUTTON_APPOINT, "взять", R.drawable.ic_accept, R.color.bottomBarButtonGreen, bt1Container);
        addButton(BUTTON_CALL_CLIENT, "клиент", R.drawable.ic_client, R.color.bottomBarButtonGray, bt2Container);
        addButton(BUTTON_CALL_DISPETCHER, "диспетчер", R.drawable.ic_dispetcher, R.color.bottomBarButtonGray, bt3Container);
        addButton(BUTTON_CANCEL, "отказаться", R.drawable.ic_close, R.color.bottomBarButtonRed, bt4Container);
    }

    private void addButtonsAppointed() {
        clearContainer();
        addButton(BUTTON_CONFIRM, "подтвердить", R.drawable.ic_accept, R.color.bottomBarButtonGreen, bt1Container);
        addButton(BUTTON_CALL_CLIENT, "клиент", R.drawable.ic_client, R.color.bottomBarButtonGray, bt2Container);
        addButton(BUTTON_CALL_DISPETCHER, "диспетчер", R.drawable.ic_dispetcher, R.color.bottomBarButtonGray, bt3Container);
        addButton(BUTTON_CANCEL, "отказаться", R.drawable.ic_close, R.color.bottomBarButtonRed, bt4Container);
    }

    private void addButtonsConfirmed() {
        clearContainer();
        addButton(BUTTON_DRIVER_ARRIVE, "по адресу", R.drawable.ic_map, R.color.bottomBarButtonGreen, bt1Container);
        addButton(BUTTON_CALL_CLIENT, "клиент", R.drawable.ic_client, R.color.bottomBarButtonGray, bt2Container);
        addButton(BUTTON_CALL_DISPETCHER, "диспетчер", R.drawable.ic_dispetcher, R.color.bottomBarButtonGray, bt3Container);
    }

    private void addButtonsDriverArrived() {
        clearContainer();
        addButton(BUTTON_PICKUP_CLIENT, "забрал", R.drawable.ic_take_client, R.color.bottomBarButtonGreen, bt1Container);
        addButton(BUTTON_CALL_CLIENT, "клиент", R.drawable.ic_client, R.color.bottomBarButtonGray, bt2Container);
        addButton(BUTTON_CALL_DISPETCHER, "диспетчер", R.drawable.ic_dispetcher, R.color.bottomBarButtonGray, bt3Container);
    }

    private void addButtonsClientWasPickedUp() {
        clearContainer();
        addButton(BUTTON_WAIT_FOR_COMPLETION, "завершить", R.drawable.ic_end_order, R.color.bottomBarButtonRed, bt1Container);
        addButton(BUTTON_CALL_CLIENT, "клиент", R.drawable.ic_client, R.color.bottomBarButtonGray, bt2Container);
        addButton(BUTTON_CALL_DISPETCHER, "диспетчер", R.drawable.ic_dispetcher, R.color.bottomBarButtonGray, bt3Container);
    }

    private void addButtonsWaitedForCompletion() {
        clearContainer();
    }

    private void addButtonsCompleted() {
        clearContainer();

    }

    private void addButtonsCanceled() {
        clearContainer();

    }


    private void clearContainer(){
        bt1Container.removeAllViews();
        bt2Container.removeAllViews();
        bt3Container.removeAllViews();
        bt4Container.removeAllViews();
    }

    private void addButton(String label, String title, int src, int color, FrameLayout container) {
        ButtonOrderView button = new ButtonOrderView(getContext(), label, title, src, color);
        button.setOnClickListener(this);
        container.addView(button);
    }


    void stateTimer(){
        if (timerContainer.getVisibility() == View.VISIBLE){
            timerContainer.setVisibility(View.INVISIBLE);
            timerView.setVisibility(View.INVISIBLE);
            bt2Container.setVisibility(View.VISIBLE);
            bt3Container.setVisibility(View.VISIBLE);
        } else {
            timerContainer.setVisibility(View.VISIBLE);
            timerView.setVisibility(View.VISIBLE);
            bt2Container.setVisibility(View.INVISIBLE);
            bt3Container.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        if (v instanceof ButtonOrderView) {
            String label = ((ButtonOrderView) v).getLabel();
            if (listener != null) {
                listener.OnButtonClick(label);
            }
        }
    }

    public interface OnButtonClickListener {
        void OnButtonClick(String label);
    }
}
