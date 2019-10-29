package ru.sigmadigital.taxipro.fragments.earn;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.adapters.RatePeriodsAdapter;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogTransparentButton;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.Ok;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.models.VehicleGroup;
import ru.sigmadigital.taxipro.repo.FeedRepository;

public class RatePeriodsFragment extends BaseNavigatorFragment implements DialogTransparentButton.DialogListener, RatePeriodsAdapter.OnPeriodClickListener, RatePeriodsAdapter.OnDeletePeriodClickListener {

    public static int CURRENT_PERIOD = 95;
    public static int MY_PERIODS = 65;
    public static int BUY_PERIODS = 89;

    private int type;

    private List<Driver.PaidRatePeriod> periods;
    private RecyclerView recyclerView;

    private int action_delete_rate_period = 576;
    private int acion_re_buy_rate_period = 765;

    private int indexForBuy;

    public static RatePeriodsFragment newInstance(int type) {
        RatePeriodsFragment fragment = new RatePeriodsFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }


    public static RatePeriodsFragment newInstance(int type, List<Driver.PaidRatePeriod> periods) {
        RatePeriodsFragment fragment = new RatePeriodsFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        fragment.setPeriods(periods);
        return fragment;
    }

    public static RatePeriodsFragment newInstance(int type, Driver.PaidRatePeriod period) {
        RatePeriodsFragment fragment = new RatePeriodsFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        List<Driver.PaidRatePeriod> periods = new ArrayList<>();
        periods.add(period);
        fragment.setPeriods(periods);
        return fragment;
    }

    private void setPeriods(List<Driver.PaidRatePeriod> periods) {
        this.periods = periods;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("type")) {
                type = getArguments().getInt("type");
            }
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("type")) {
                type = savedInstanceState.getInt("type");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_work, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        EarnActionBarFragment.setBackButtonVisible(true);

        recyclerView = v.findViewById(R.id.rv_work);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));


        if (type == MY_PERIODS) {
            EarnActionBarFragment.setActionBarTitle("МОИ СМЕНЫ");
        }

        if (type == BUY_PERIODS) {
            EarnActionBarFragment.setActionBarTitle("КУПИТЬ СМЕНУ");
        }

        if (type == CURRENT_PERIOD) {
            EarnActionBarFragment.setActionBarTitle("МОЯ СМЕНА");
        }

        sendRequest();

        return v;
    }


    private void sendRequest() {

        if (type == BUY_PERIODS) {
            Sender.getInstance().send("vehicleGroup.driverRatePeriodsForSale", new VehicleGroup.DriverRatePeriodsForSaleFilter().toJson(), this.getClass().getSimpleName());
        }

        if (type == MY_PERIODS) {
            initAdapter(false);
        }

        if (type == CURRENT_PERIOD) {
            initAdapter(true);

        }


        LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(@Nullable ReceivedResponse responce) {
                if (responce != null) {
                    onPeriodsReceived(responce);
                }
            }
        });
    }

    private void onPeriodsReceived(ReceivedResponse responce) {
        if (responce.getSenderFragment().equals(this.getClass().getSimpleName()) && responce.getData() != null) {

            if (responce.getData().getClass().equals(VehicleGroup.DriverRatePeriodsForSale.class)) {
                Log.e("RatePeriodsForSale", "DriverRatePeriodsForSale");

                initAdapter(((VehicleGroup.DriverRatePeriodsForSale) responce.getData()).getPeriods());
                return;
            }

            if (responce.getData().getClass().equals(Ok.class)) {
                if (responce.getSenderRequest() != null) {
                    Log.e("Ok", responce.getSenderRequest());
                    if (responce.getSenderRequest().equals("takeOffRatePeriod")) {
                        if (periods != null){
                            periods.clear();
                            periods.add(null);
                        }
                        initAdapter(true);
                    }

                    if (responce.getSenderRequest().equals("takeRatePeriod")) {
                        showDialogWasBougth();
                    }


                }
                return;
            }

            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("Error", "Error");
                Error e = (Error) responce.getData();
                Toast.makeText(App.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
               /* progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
                Error error = (Error) responce.getData();
                errorText.setText("Ошибка:"+ error.getMessage());*/
                return;
            }

            Log.e("unknownDataAreasFr", responce.getData().getClass().toString());
        }
    }


    private void initAdapter(List<VehicleGroup.DriverRatePeriod> periods) {
        RatePeriodsAdapter adapter = new RatePeriodsAdapter(this, periods);
        recyclerView.setAdapter(adapter);
    }

    private void initAdapter(boolean showDeleteButton) {
        RatePeriodsAdapter adapter = new RatePeriodsAdapter(this, this, periods, showDeleteButton);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPeriodClick(int index) {
        indexForBuy = index;
        if (FeedRepository.getInstance().getFeed().getPaidRatePeriod() == null) {
           showDialogToBuy();
        } else {
            showDialogReBuy();
        }
    }

    @Override
    public void onDeletePeriodClick() {
        showDialogDelete();
    }


    private void buyPeriod() {
        Sender.getInstance().send("driver.takeRatePeriod", new Driver.TakeRatePeriod(indexForBuy).toJson(), this.getClass().getSimpleName()+" "+"takeRatePeriod");
    }


    private void showDialogDelete() {
        DialogTransparentButton dialog = DialogTransparentButton.newInstance(this,
                R.drawable.ic_warning,
                "Удалить смену?",
                "Удаление смены активирует прием заказов с комиссией",
                getString(R.string.close),
                "ДА",
                action_delete_rate_period);
        dialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        dialog.show(getFragmentManager(), "dialog");
    }

    private void showDialogReBuy() {
        DialogTransparentButton dialog = DialogTransparentButton.newInstance(this,
                R.drawable.ic_warning,
                "Eсть активная смена!",
                "Заказ новой смены приведет к замене активной смены",
                getString(R.string.cancell),
                "ЗАМЕНИТЬ",
                acion_re_buy_rate_period);
        dialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        dialog.show(getFragmentManager(), "dialog");
    }

    private void showDialogToBuy() {
        DialogTransparentButton dialog = DialogTransparentButton.newInstance(this,
                R.drawable.ic_warning,
                "Купить эту смену?",
                "",
                getString(R.string.close),
                "ДА",
                acion_re_buy_rate_period);
        dialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        dialog.show(getFragmentManager(), "dialog");
    }

    private void showDialogWasBougth() {
        DialogTransparentButton dialog = DialogTransparentButton.newInstance(this,
                R.drawable.ic_warning,
                "Смена куплена!",
                "",
                getString(R.string.close),
                "ДА");
        dialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        dialog.show(getFragmentManager(), "dialog");
    }


    @Override
    public void onActionClick(int action) {
        if (action == action_delete_rate_period) {
            Sender.getInstance().send("driver.takeOffRatePeriod", new Driver.TakeOffRatePeriod().toJson(), this.getClass().getSimpleName() + " " + "takeOffRatePeriod");
        }

        if (action == acion_re_buy_rate_period) {
            buyPeriod();
        }
    }

}
