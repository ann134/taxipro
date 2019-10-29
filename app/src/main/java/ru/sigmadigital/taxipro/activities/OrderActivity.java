package ru.sigmadigital.taxipro.activities;

import android.os.Bundle;
import android.util.Log;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.fragments.order.OrderFragment;
import ru.sigmadigital.taxipro.models.DriverNotification;
import ru.sigmadigital.taxipro.models.Order;

public class OrderActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        if (getIntent().hasExtra("order")/* && getIntent().hasExtra("distance")*/) {
            Order.DriverOrder order = ( Order.DriverOrder) getIntent().getSerializableExtra("order");
           /* Order.OrderDriverDistance distance = ( Order.OrderDriverDistance) getIntent().getSerializableExtra("distance");*/
            loadFragment(OrderFragment.newInstance(order/*, distance*/), "dhrdh", false);
        }

    }

    @Override
    protected int getFragmentContainer() {
        return R.id.order_container;
    }
}
