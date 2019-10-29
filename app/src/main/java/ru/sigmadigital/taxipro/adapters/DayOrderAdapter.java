package ru.sigmadigital.taxipro.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.util.DateFormatter;


public class DayOrderAdapter extends RecyclerView.Adapter<DayOrderAdapter.TripHolder> {

    private OnTripClickListener listener;
    private List<Driver.DayOrder> list;

    public DayOrderAdapter(OnTripClickListener listener, List<Driver.DayOrder> list) {
        this.listener = listener;
        this.list = list;
    }

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, null);
        return new TripHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TripHolder holder, final int position) {
        Driver.DayOrder dayOrder = list.get(position);

        Date date = DateFormatter.getDateFromStringUTCWithSeconds(dayOrder.getDate());
        String time = DateFormatter.getStringSimpleTimeFromDate(date);
        holder.time.setText(time);
        holder.start.setText("?");
        holder.end.setText("?");
        holder.price.setText(dayOrder.getPrice() + "\u20BD");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDayOrderClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class TripHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView start;
        TextView end;
        TextView price;

        TripHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            start = itemView.findViewById(R.id.start_address);
            end = itemView.findViewById(R.id.end_address);
            price = itemView.findViewById(R.id.price);
        }
    }


    public interface OnTripClickListener {
        void onDayOrderClick();
    }
}
