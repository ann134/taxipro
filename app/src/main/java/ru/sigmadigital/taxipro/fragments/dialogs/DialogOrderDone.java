package ru.sigmadigital.taxipro.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.models.Order;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.util.DateFormatter;


public class DialogOrderDone extends DialogFragment implements View.OnClickListener {

    private TextView time;
    private TextView distance;
    private TextView price;
    private TextView payment;

    private int rate;
    private List<ImageView> stars = new ArrayList<>();
    private DialogRateListener listener;


    public static DialogOrderDone newInstance(DialogRateListener listener) {
        Bundle args = new Bundle();
        args.putSerializable("listener", listener);
        DialogOrderDone dialog = new DialogOrderDone();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("rate")) {
                rate = getArguments().getInt("rate");
            }
            if (getArguments().containsKey("listener")) {
                listener = (DialogRateListener) getArguments().getSerializable("listener");
            }
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("rate")) {
                rate = savedInstanceState.getInt("rate");
            }
            if (savedInstanceState.containsKey("listener")) {
                listener = (DialogRateListener) savedInstanceState.getSerializable("listener");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_order_done, null);

        time = view.findViewById(R.id.time);
        distance = view.findViewById(R.id.distance);
        price = view.findViewById(R.id.amount);
        payment = view.findViewById(R.id.payment);

        MutableLiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(ReceivedResponse receivedResponse) {
                if(receivedResponse != null && receivedResponse.getData().getClass().equals(Order.DriverOrder.class)){
                    Order.DriverOrder order = (Order.DriverOrder) receivedResponse.getData();
                    distance.setText(String.format("%.1f км", order.getTotalDistance() / 1000));
                    price.setText(order.getPrice());
                    Date date = new Date(System.currentTimeMillis());
                    time.setText(DateFormatter.getSimpleTime(date));
                }
            }
        });



        stars.add((ImageView) view.findViewById(R.id.star1));
        stars.add((ImageView) view.findViewById(R.id.star2));
        stars.add((ImageView) view.findViewById(R.id.star3));
        stars.add((ImageView) view.findViewById(R.id.star4));
        stars.add((ImageView) view.findViewById(R.id.star5));

        for (ImageView s : stars) {
            s.setOnClickListener(this);
        }
        tapStar();

        view.findViewById(R.id.rate).setOnClickListener(this);
        view.findViewById(R.id.close).setOnClickListener(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        Dialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rate: {
                if (listener != null) {
                    listener.onRateClick(rate);
                }
                dismiss();
                getActivity().onBackPressed();
            }
            case R.id.close: {
                dismiss();
                getActivity().onBackPressed();
            }
            case R.id.star1: {
                rate = 1;
                tapStar();
                break;
            }
            case R.id.star2: {
                rate = 2;
                tapStar();
                break;
            }
            case R.id.star3: {
                rate = 3;
                tapStar();
                break;
            }
            case R.id.star4: {
                rate = 4;
                tapStar();
                break;
            }
            case R.id.star5: {
                rate = 5;
                tapStar();
                break;
            }
        }
    }

    private void tapStar() {
        for (int i = 0; i < stars.size(); i++) {
            if (i <= rate - 1) {
                stars.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_star_full));
            } else {
                stars.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_star));
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("rate", rate);
        outState.putSerializable("listener", listener);
    }

    public interface DialogRateListener extends Serializable {
        void onRateClick(int rate);
    }
}
