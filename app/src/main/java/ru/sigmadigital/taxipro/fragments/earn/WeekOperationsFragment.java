package ru.sigmadigital.taxipro.fragments.earn;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.adapters.BalanceTransactionsAdapter;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.PageTotal;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.util.DateFormatter;
import ru.sigmadigital.taxipro.view.StatisticView;

public class WeekOperationsFragment extends BaseNavigatorFragment {

    String startDate;

    private TextView errorText;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;


    public static WeekOperationsFragment newInstance(String startDate){
        Bundle args = new Bundle();
        args.putString("startDate", startDate);
        WeekOperationsFragment fragment = new WeekOperationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("startDate")) {
                startDate = getArguments().getString("startDate");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_week_operations, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        EarnActionBarFragment.setBackButtonVisible(true);
        EarnActionBarFragment.setActionBarTitle("Операции за неделю");

        errorText = v.findViewById(R.id.error);
        progressBar = v.findViewById(R.id.loading);


        recyclerView = v.findViewById(R.id.rv_operations);
        recyclerView.setLayoutManager(new GridLayoutManager(App.getAppContext(), 1));




        sendRequest();

        return v;
    }


    private void sendRequest() {
        Date startDate = DateFormatter.getDateFromStringUTCTimeZone(this.startDate);
        if (startDate != null){
            Date endDate = DateFormatter.datePlusDays(startDate, +6);
            String endDateString = DateFormatter.getStringUTCFromDate(endDate);

            Sender.getInstance().send("driver.transaction", new Driver.TransactionFilter(this.startDate, endDateString).toJson(), this.getClass().getSimpleName());
        }

        LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(@Nullable ReceivedResponse responce) {
                if (responce != null) {
                    onBalanceReceived(responce);
                }
            }
        });
    }

    private <T> void onBalanceReceived(ReceivedResponse responce) {
        if (responce.getSenderFragment().equals(this.getClass().getSimpleName()) && responce.getData() != null) {

            if (responce.getData().getClass().equals(ArrayList.class)) {
                ArrayList<T> list = (ArrayList<T>) responce.getData();
                List<Driver.Transaction> transactions = new ArrayList<>();
                for (T item : list) {
                    if (item instanceof Driver.Transaction) {
                        transactions.add((Driver.Transaction) item);
                    }
                }

                progressBar.setVisibility(View.GONE);

                initRecycler(transactions);
                return;
            }

            if (responce.getData().getClass().equals(PageTotal.class)) {
                progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Список пуст");
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


    private void initRecycler(List<Driver.Transaction> list){

        BalanceTransactionsAdapter adapter = new BalanceTransactionsAdapter(list);
        recyclerView.setAdapter(adapter);
    }
}
