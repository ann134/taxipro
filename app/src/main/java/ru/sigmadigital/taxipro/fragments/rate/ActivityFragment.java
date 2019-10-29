package ru.sigmadigital.taxipro.fragments.rate;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.adapters.ActivityAdapter;
import ru.sigmadigital.taxipro.adapters.RateAdapter;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.PageTotal;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.view.StatisticView;

public class ActivityFragment<T> extends BaseNavigatorFragment implements RateAdapter.OnRateClickListener {


    private StatisticView currentActivity;

    private ActivityAdapter adapter;
    private ProgressBar progressBar;
    private TextView errorText;

    public static ActivityFragment newInstance() {
        return new ActivityFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activity, container, false);

        currentActivity = v.findViewById(R.id.activity);
        RecyclerView recyclerView = v.findViewById(R.id.rv_activity);
        errorText = v.findViewById(R.id.error);
        progressBar = v.findViewById(R.id.progress_bar_loading);
        adapter = new ActivityAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(App.getAppContext()));
        recyclerView.setAdapter(adapter);

        currentActivity.setTitle("ТЕКУЩАЯ АКТИВНОСТЬ:");
        currentActivity.setVisibleArrow(false);


        sendRequest();

        return v;
    }

    private void sendRequest() {
        Sender.getInstance().send("driver.activity", new Driver.ActivityFilter().toJson(), this.getClass().getSimpleName());
        Sender.getInstance().send("driver.activityHistory", new Driver.ActivityHistoryFilter().toJson(), this.getClass().getSimpleName());

        LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(ReceivedResponse responce) {
                onResponceReceived(responce);
            }
        });
    }

    private void onResponceReceived(ReceivedResponse responce) {

        if (responce.getSenderFragment().equals(this.getClass().getSimpleName()) && responce.getData() != null) {

            if (responce.getData().getClass().equals(Driver.Activity.class)) {
                currentActivity.setCount(String.valueOf(((Driver.Activity) responce.getData()).getValue()));
                return;
            }

            if (responce.getData().getClass().equals(ArrayList.class)) {
                ArrayList<T> responseList = (ArrayList<T>) responce.getData();
                List<Driver.ActivityHistory> list = new ArrayList<>();
                for (T item : responseList) {
                    if (item instanceof Driver.ActivityHistory) {
                        list.add((Driver.ActivityHistory) item);
                    }
                }
                adapter.setItemList(list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                return;
            }

            if (responce.getData().getClass().equals(PageTotal.class)) {
                progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("История активности пуста");
                return;
            }

            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("Error", "Error");
                progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
                Error error = (Error) responce.getData();
                errorText.setText("Ошибка:" + error.getMessage());
                return;
            }
        }
    }


    @Override
    public void onRateClick() {

    }

}
