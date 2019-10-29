package ru.sigmadigital.taxipro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.Order;
import ru.sigmadigital.taxipro.util.DateFormatter;

public class PreOrdersAdapter extends RecyclerView.Adapter<PreOrdersAdapter.PreOrderHolder> {

    private List<Order.DriverOrder> preorders;
    private OnOrderClickListener listener;

    public PreOrdersAdapter(OnOrderClickListener listener, List<Order.DriverOrder> preorders) {
        this.preorders = preorders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PreOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pre_order, null);
        view.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new PreOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreOrderHolder holder, int position) {

        final Order.DriverOrder order = preorders.get(position);

        Date orderDate = DateFormatter.getDateFromStringUTC(order.getPickupTime());
        holder.day.setText(DateFormatter.getDay(orderDate));
        holder.month.setText(DateFormatter.getMonth(orderDate));
        //holder.month
        holder.time.setText(DateFormatter.getSimpleTime(orderDate));


        holder.address.setText(order.getRouteItems().get(0).getTitle());

        holder.setIsRecyclable(false);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOrderClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return preorders.size();
    }

    public class PreOrderHolder extends RecyclerView.ViewHolder{

        TextView day;
        TextView month;
        TextView time;
        TextView address;

        public PreOrderHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.tv_day);
            month = itemView.findViewById(R.id.tv_month);
            time = itemView.findViewById(R.id.tv_time);
            address = itemView.findViewById(R.id.tv_message);
        }
    }

    public interface OnOrderClickListener {
        void onOrderClick(Order.DriverOrder order);
    }
}
