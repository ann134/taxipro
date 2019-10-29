package ru.sigmadigital.taxipro.fragments.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.Order;

public class PaymentFragment extends Fragment {

    private ImageView iconPayment;
    private TextView payment;
    private TextView amount;

    public static PaymentFragment newInstance() {
        return new PaymentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment, container, false);

        iconPayment = v.findViewById(R.id.icon_payment);
        payment = v.findViewById(R.id.payment);
        amount = v.findViewById(R.id.amount);


        subscribeToLiveData();

        return v;
    }

    private void subscribeToLiveData() {
        LiveData<Order.DriverOrder> liveData = OrderFragment.getLiveData();
        liveData.observe(this, new Observer<Order.DriverOrder>() {
            @Override
            public void onChanged(@Nullable Order.DriverOrder value) {
                if (value != null) {
                    setData(value);
                }
            }
        });
    }

    private void setData(Order.DriverOrder order) {

        switch (order.getBillingType()){
            case Order.BillingType.usual:{
                iconPayment.setImageDrawable(getResources().getDrawable(R.drawable.ic_rub));
                payment.setText("Oплата наличными");
                break;
            }

            case Order.BillingType.card:{
                iconPayment.setImageDrawable(getResources().getDrawable(R.drawable.ic_credit_card));
                payment.setText("Oплата картой");
                break;
            }

            case Order.BillingType.contractor:{
                iconPayment.setImageDrawable(getResources().getDrawable(R.drawable.ic_work));
                payment.setText("Корпоративный");
                break;
            }
        }

        amount.setText(order.getPrice());
    }
}
