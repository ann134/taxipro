package ru.sigmadigital.taxipro.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.Order;
import ru.sigmadigital.taxipro.models.TitledGeoAddress;
import ru.sigmadigital.taxipro.util.DateFormatter;

public class OrderAddressesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Order.DriverOrder order;
    private List<TitledGeoAddress> list;
    private Order.OrderDriverDistance distance;


    public OrderAddressesAdapter(Order.DriverOrder order, Order.OrderDriverDistance distance) {
        this.order= order;
        this.list = order.getRouteItems();
        this.distance = distance;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            /*case 0: {
                @SuppressLint("InflateParams") View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, null);
                return new TimeHolder(v);
            }*/
            case 0: {
                @SuppressLint("InflateParams") View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_start_point, null);
                return new StartPointHolder(v);
            }
            default: {
                @SuppressLint("InflateParams") View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, null);
                return new AddressHolder(v);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        //Log.e("position", position+"");

        if (holder instanceof StartPointHolder){
            if (distance != null){
                ((StartPointHolder) holder).distance.setText(distance.getDistance()+"");
            }

            if (order.getPickupTime() != null){
                Date date = DateFormatter.getDateFromStringUTC(order.getPickupTime());
                String formattedDate  = DateFormatter.getSimpleTime(date);
                ((StartPointHolder) holder).time.setText(formattedDate);
            }


        }

        if (holder instanceof AddressHolder) {
            //Log.e("adr", address.getTitle()+"");

            final TitledGeoAddress address = list.get(position - 1);

            ((AddressHolder) holder).address.setText(address.getTitle());

            if (position == 1) {
                ((AddressHolder) holder).lineUp.setBackgroundColor(App.getAppContext().getResources().getColor(R.color.colorAccent));
                ((AddressHolder) holder).ring.setBackgroundDrawable(App.getAppContext().getDrawable(R.drawable.dot_gray));
            }

            if (position == getItemCount() - 1){
                ((AddressHolder)holder).lineDown.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0: {
                return 0;
            }
            /*case 1: {
                return 1;
            }*/
            default: {
                return 3;
            }
        }
    }


    class TimeHolder extends RecyclerView.ViewHolder {

        TextView when;

        TimeHolder(@NonNull View itemView) {
            super(itemView);
            when = itemView.findViewById(R.id.when);
        }
    }

    class StartPointHolder extends RecyclerView.ViewHolder {
        TextView distance;
        TextView time;

        View lineUp;
        View lineDown;
        View ring;

        StartPointHolder(@NonNull View itemView) {
            super(itemView);
            distance = itemView.findViewById(R.id.distance);
            time = itemView.findViewById(R.id.time);

            lineUp = itemView.findViewById(R.id.line_up);
            lineDown = itemView.findViewById(R.id.line_down);
            ring = itemView.findViewById(R.id.ring);
        }
    }


    class AddressHolder extends RecyclerView.ViewHolder {
        TextView address;
        TextView area;

        View lineUp;
        View lineDown;
        View ring;

        AddressHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            area = itemView.findViewById(R.id.area);
            lineUp = itemView.findViewById(R.id.line_up);
            lineDown = itemView.findViewById(R.id.line_down);
            ring = itemView.findViewById(R.id.ring);
        }
    }
}