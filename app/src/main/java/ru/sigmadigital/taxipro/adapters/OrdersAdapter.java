package ru.sigmadigital.taxipro.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.Order;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderHolder> {

    private OnOrderClickListener listener;
    private List<Order.DriverOrder> orders;
    List<Order.OrderDriverDistance> distances;


    public OrdersAdapter(OnOrderClickListener listener, List<Order.DriverOrder> orders, List<Order.OrderDriverDistance> distances) {
        this.listener = listener;
        this.orders = orders;
        this.distances = distances;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, null);


        return new OrderHolder(v);
    }

    Order.OrderDriverDistance distance = null;

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, final int position) {
        final Order.DriverOrder order = orders.get(position);

        //Log.e("state", order.getState()+"");


        for (Order.OrderDriverDistance orderDistance : distances) {
            if (orderDistance.getId() == order.getId()) {
                distance = orderDistance;
                holder.distance.setText(distance.getDistance() + "");
                break;
            }
        }

        //Log.e("PickupTime", order.getPickupTime()+"");

        holder.price.setText(order.getPrice());

        holder.startAddress.setText(order.getRouteItems().get(0).getTitle());
        holder.startArea.setText(order.getRouteItems().get(0).getDistrict());
/*
        for(TitledGeoAddress address : order.getRouteItems()){
            Log.e("adr"+order.getId(), address.getTitle()+"");
        }*/

        if (order.getRouteItems().size() > 1) {
            holder.endAddress.setText(order.getRouteItems().get(order.getRouteItems().size() - 1).getTitle() + "");
            holder.endArea.setText(order.getRouteItems().get(order.getRouteItems().size() - 1).getDistrict() + "");
        } else {
            holder.endArea.setVisibility(View.GONE);
            holder.endAddress.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOrderClick(order, distance);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    class OrderHolder extends RecyclerView.ViewHolder {

        TextView price;
        TextView distance;
        TextView startAddress;
        TextView startArea;
        TextView endAddress;
        TextView endArea;

        OrderHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price);
            distance = itemView.findViewById(R.id.distance);
            startAddress = itemView.findViewById(R.id.start_address);
            startArea = itemView.findViewById(R.id.start_area);
            endAddress = itemView.findViewById(R.id.end_address);
            endArea = itemView.findViewById(R.id.end_area);
        }
    }

    public interface OnOrderClickListener {
        void onOrderClick(Order.DriverOrder order, Order.OrderDriverDistance distance);
    }
}
