package ru.sigmadigital.taxipro.fragments.earn;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;

import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.PageTotal;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.repo.FeedRepository;
import ru.sigmadigital.taxipro.view.StatisticView;


public class EarnFragment extends BaseNavigatorFragment implements View.OnClickListener {

    private Driver.Balance balanceResponse;
    private List<Driver.PaidRatePeriod> periods;
    private Driver.Feed feed;

   /* private Driver.Balance balanceResponse;
    private List<Driver.PaidRatePeriod> periods;
    private Driver.Feed feed;

    private ProgressBar progressBar;
    private TextView errorText;
    private NestedScrollView body;*/

    private StatisticView incomeOfWeek;
    private StatisticView ridesCount;
    private StatisticView currentRide;
    private StatisticView balance;
    private StatisticView buyWorkCount;

    public static EarnFragment newInstance() {
        return new EarnFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_income, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //action bar
        EarnActionBarFragment.setBackButtonVisible(false);
        EarnActionBarFragment.setActionBarTitle("ДОХОДЫ");

        //init
        /*errorText = v.findViewById(R.id.error);
        progressBar = v.findViewById(R.id.progress_bar);
        body = v.findViewById(R.id.body);*/

        incomeOfWeek = v.findViewById(R.id.sv_income_of_week);
        ridesCount = v.findViewById(R.id.sv_rides_count);
        balance = v.findViewById(R.id.sv_balance);
        buyWorkCount = v.findViewById(R.id.sv_work_count);
        Button buyWork = v.findViewById(R.id.btn_buy_work);
        currentRide = v.findViewById(R.id.sv_current_rate);

        incomeOfWeek.setTitle("ДОХОД НА ЭТОЙ НЕДЕЛЕ:");
        ridesCount.setTitle("ПОЕЗДКИ:");
        balance.setTitle("БАЛАНС:");
        buyWorkCount.setTitle("КУПЛЕНО СМЕН:");
        currentRide.setTitle("ТЕКУЩАЯ СМЕНА");

        currentRide.setOnClickListener(this);
        buyWorkCount.setOnClickListener(this);
        buyWork.setOnClickListener(this);
        balance.setOnClickListener(this);
        incomeOfWeek.setOnClickListener(this);

        ridesCount.setVisibleArrow(false);
        ridesCount.showProgressBar();
        incomeOfWeek.showProgressBar();
        balance.showProgressBar();
        buyWorkCount.showProgressBar();
        currentRide.showProgressBar();

        sendRequest();
        subscribeToFeed();

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
    }



    private void sendRequest() {
        Sender.getInstance().send("driver.balance", new Driver.BalanceFilter().toJson(), this.getClass().getSimpleName()+" balance");

        Sender.getInstance().send("driver.paidRatePeriod", new Driver.PaidRatePeriodFilter(0, 999).toJson(), this.getClass().getSimpleName()+" paidRatePeriod");

        //Sender.getInstance().send("driver.feed", new Driver.FeedFilter().toJson(), this.getClass().getSimpleName()+" feed");

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

    private<T> void onBalanceReceived(ReceivedResponse responce) {
        if (responce.getSenderFragment() != null && responce.getSenderFragment().equals(this.getClass().getSimpleName()) && responce.getData() != null) {

            if (responce.getData().getClass().equals(Driver.Balance.class)) {
                Log.e("Balance", "Balance");
                balanceResponse = (Driver.Balance) responce.getData();
                onBalanceRecieved((Driver.Balance) responce.getData());
                return;
            }

            if (responce.getData().getClass().equals(ArrayList.class)) {
                Log.e("ArrayList", "ArrayList");
                ArrayList<T> list = (ArrayList<T>) responce.getData();
                periods = new ArrayList<>();
                for (T item : list) {
                    if (item instanceof Driver.PaidRatePeriod) {
                        periods.add((Driver.PaidRatePeriod) item);
                    }
                }

                onPaidRatePeriodsRecieved(periods);
                return;
            }

            if (responce.getData().getClass().equals(PageTotal.class)) {
                periods = new ArrayList<>();
                onPaidRatePeriodsRecieved(periods);
            }

           /* if (responce.getData().getClass().equals(Driver.Feed.class)) {
                Log.e("Feed", "Feed");
                FeedRepository.getInstance().setFeed((Driver.Feed) responce.getData());
                feed = (Driver.Feed) responce.getData();

                onFeedRecieved();
                return;
            }
*/

            if (responce.getData().getClass().equals(Error.class) && responce.getSenderRequest() != null) {

                if (responce.getSenderRequest().equals("balance")){
                    Log.e("Error", "balance");
                    /*Error error = (Error) responce.getData();
                    errorText.setText("Ошибка:"+ error.getMessage());*/

                    incomeOfWeek.setCount("?");
                    ridesCount.setCount("?");
                    balance.setCount("?");
                }

                if (responce.getSenderRequest().equals("paidRatePeriod")){
                    Log.e("Error", "paidRatePeriod");

                    buyWorkCount.setCount("?");

                   /* Error error = (Error) responce.getData();
                    errorText.setText("Ошибка:"+ error.getMessage());*/
                }

                Log.e("Error", "Error");
                return;
            }

            Log.e("unknownDataAreasFr", responce.getData().getClass().toString());
        }
    }

    private void subscribeToFeed() {
        final LiveData<Driver.Feed> liveData = FeedRepository.getInstance().getFeedLiveData();
        liveData.observe(this, new Observer<Driver.Feed>() {
            @Override
            public void onChanged(Driver.Feed feedRep) {
                feed = feedRep;
                onFeedRecieved(feed);
            }
        });
    }

    private void onFeedRecieved(Driver.Feed feed) {
        Log.e("Feed", "Feed");
        if (feed == null){
            currentRide.setCount("не удалось получить");
            return;
        }
        if (feed.getPaidRatePeriod() != null){
            currentRide.setCount(feed.getPaidRatePeriod().getName()+"");
        } else {
            currentRide.setCount("нет");
        }
    }

    private void onBalanceRecieved(Driver.Balance balanceResponse) {
        incomeOfWeek.setCount(balanceResponse.getWeekTotal() + "\u20BD");
        ridesCount.setCount(balanceResponse.getWeekOrderCount()+"");
        balance.setCount(balanceResponse.getCurrentBalance() + "\u20BD");
    }

    private void onPaidRatePeriodsRecieved(List<Driver.PaidRatePeriod> periods) {
        if (periods != null) {
            buyWorkCount.setCount(periods.size()+"");
        } else {
            buyWorkCount.setCount("0");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sv_work_count:
                showFragment(RatePeriodsFragment.newInstance(RatePeriodsFragment.MY_PERIODS, periods));
                break;

            case R.id.sv_current_rate:
                showFragment(RatePeriodsFragment.newInstance(RatePeriodsFragment.CURRENT_PERIOD, feed.getPaidRatePeriod()));
                break;

            case R.id.btn_buy_work:
                showFragment(RatePeriodsFragment.newInstance(RatePeriodsFragment.BUY_PERIODS));
                break;

            case R.id.sv_balance:
                showFragment(BalanceFragment.newInstance(balanceResponse));
                break;

            case R.id.sv_income_of_week:
                showFragment(WeekEarnFragment.newInstance());
                break;
        }
    }


    private void showFragment(Fragment fragment){
        replaceCurrentFragmentWith(
                getFragmentManager(),
                R.id.fragments_income_container,
                fragment,
                true);

    }

}
