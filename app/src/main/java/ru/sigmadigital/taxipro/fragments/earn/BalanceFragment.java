package ru.sigmadigital.taxipro.fragments.earn;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class BalanceFragment extends BaseNavigatorFragment implements View.OnClickListener {

    private Driver.Balance balanceResponse;

    private TextView errorText;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;


    public static BalanceFragment newInstance(Driver.Balance balanceResponse){
        Bundle args = new Bundle();
        args.putSerializable("balanceResponse", balanceResponse);
        BalanceFragment fragment = new BalanceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("balanceResponse")) {
                balanceResponse = (Driver.Balance) getArguments().getSerializable("balanceResponse");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_balance, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        EarnActionBarFragment.setBackButtonVisible(true);
        EarnActionBarFragment.setActionBarTitle("БАЛАНС");

        errorText = v.findViewById(R.id.error);
        progressBar = v.findViewById(R.id.loading);
        StatisticView balance = v.findViewById(R.id.sv_balance);
        ConstraintLayout payCard = v.findViewById(R.id.cl_pay_card);
        ConstraintLayout terminals = v.findViewById(R.id.cl_terminals);

        balance.setVisibleArrow(false);
        balance.setTitle("ТЕКУЩИЙ БАЛАНС");
        balance.setCount(balanceResponse.getCurrentBalance() +"\u20BD");

        recyclerView = v.findViewById(R.id.rv_operations);
        recyclerView.setLayoutManager(new GridLayoutManager(App.getAppContext(), 1));

        terminals.setOnClickListener(this);
        payCard.setOnClickListener(this);


        sendRequest();

        return v;
    }


    private void sendRequest() {
        Date startDate = DateFormatter.getDateFromStringUTCTimeZone(balanceResponse.getCurrentWeekStart());
        if (startDate != null){
            Date endDate = DateFormatter.datePlusDays(startDate, -6);
            String endDateString = DateFormatter.getStringUTCFromDate(endDate);

            Sender.getInstance().send("driver.transaction", new Driver.TransactionFilter(/*balanceResponse.getCurrentWeekStart(), endDateString, 0, 999*/).toJson(), this.getClass().getSimpleName());
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cl_terminals: {
                showFragment(QiwiFragment.newInstance());
                break;
            }
            case R.id.cl_pay_card: {
                showFragment(AcquiringBankFragment.newInstance());
                break;
            }
        }
    }

    private void showFragment(Fragment fragment){
        replaceCurrentFragmentWith(getFragmentManager(), getParentContainer(this.getView()), fragment, true);
    }
}
