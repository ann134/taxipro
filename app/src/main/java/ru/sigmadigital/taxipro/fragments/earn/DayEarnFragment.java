package ru.sigmadigital.taxipro.fragments.earn;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.adapters.DayOrderAdapter;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.util.DateFormatter;
import ru.sigmadigital.taxipro.view.StatisticView;

public class DayEarnFragment extends BaseNavigatorFragment implements DayOrderAdapter.OnTripClickListener {

    private Driver.WeekDay weekDay;
    private Driver.DayEarn dayEarn;

    TextView errorText;
    private ProgressBar progressBar;
    private NestedScrollView body;

    private StatisticView dayEarnTotal;
    private StatisticView ordersTotal;
    private StatisticView surgeTotal;
    private StatisticView feeTotal;
    private StatisticView timeOnline;
    private StatisticView ordersCount;
    private RecyclerView recyclerView;


    public static DayEarnFragment newInstance(Driver.WeekDay day){
        Bundle args = new Bundle();
        args.putSerializable("day", day);
        DayEarnFragment fragment = new DayEarnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("day")) {
                weekDay = (Driver.WeekDay) getArguments().getSerializable("day");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_day_earn, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Date date = DateFormatter.getDateFromStringUTCTimeZone(weekDay.getDate());
        if (date != null){
            String formattedDate  = DateFormatter.getStringDdMmmmEeFromDate(date);
            EarnActionBarFragment.setActionBarTitle(formattedDate);
            EarnActionBarFragment.setBackButtonVisible(true);
        }


        errorText = v.findViewById(R.id.error);
        progressBar = v.findViewById(R.id.progress_bar);
        body = v.findViewById(R.id.body);

        dayEarnTotal = v.findViewById(R.id.sv_income_of_week);
        dayEarnTotal.setTitle("суточный доход");
        dayEarnTotal.setVisibleArrow(false);

        ordersCount = v.findViewById(R.id.sv_trips_count);
        ordersCount.setTitle("ПОЕЗДОК");
        ordersCount.setTitleSize(14f);
        ordersCount.setCountSize(20f);
        ordersCount.setVisibleArrow(false);

        timeOnline = v.findViewById(R.id.sv_online_minutes);
        timeOnline.setTitle("МИНУТ В СЕТИ");
        timeOnline.setTitleSize(14f);
        timeOnline.setCountSize(20f);
        timeOnline.setVisibleArrow(false);

        ordersTotal = v.findViewById(R.id.sv_rides_count);
        ordersTotal.setTitle("заказы");
        ordersTotal.setVisibleArrow(false);

        surgeTotal = v.findViewById(R.id.sv_balance);
        surgeTotal.setTitle("наценка");
        surgeTotal.setVisibleArrow(false);

        feeTotal = v.findViewById(R.id.sv_work_count);
        feeTotal.setTitle("Комиссия");
        feeTotal.setVisibleArrow(false);

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(App.getAppContext(), 1));


        sendRequest();

        return v;
    }


    private void sendRequest() {
        Sender.getInstance().send("driver.dayEarn", new Driver.DayEarnFilter(weekDay.getDate()).toJson(), this.getClass().getSimpleName());

        LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(@Nullable ReceivedResponse responce) {
                if (responce != null) {
                    onWeeksReceived(responce);
                }
            }
        });
    }

    private void onWeeksReceived(ReceivedResponse responce) {
        if (responce.getSenderFragment().equals(this.getClass().getSimpleName()) && responce.getData() != null) {

            if (responce.getData().getClass().equals(Driver.DayEarn.class)) {
                Log.e("DayEarn", "DayEarn");
                dayEarn = (Driver.DayEarn) responce.getData();
                fillData();

                progressBar.setVisibility(View.GONE);
                body.setVisibility(View.VISIBLE);
                return;
            }

            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("Error", "Error");
                progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
                Error error = (Error) responce.getData();
                errorText.setText("Ошибка:"+ error.getMessage());
                return;
            }

            Log.e("unknownDataAreasFr", responce.getData().getClass().toString());
        }
    }


    private void fillData(){
        dayEarnTotal.setCount(dayEarn.getTotalEarn() + "\u20BD");
        ordersCount.setCount(dayEarn.getOrderCount()+ "");
        timeOnline.setCount(dayEarn.getTimeOnline()+"");
        ordersTotal.setCount(dayEarn.getOrderTotal()+ "\u20BD");
        surgeTotal.setCount(dayEarn.getSurgeTotal() + "\u20BD");
        feeTotal.setCount(dayEarn.getFeeTotal()+"\u20BD");
        initAdapter(dayEarn.getOrders());
    }

    private void initAdapter(List<Driver.DayOrder> list) {
        DayOrderAdapter adapter = new DayOrderAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onDayOrderClick() {
        replaceCurrentFragmentWith(getFragmentManager(), getParentContainer(this.getView()), DayOrderDetailsFragment.newInstance(), true);
    }

}