package ru.sigmadigital.taxipro.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.util.DateFormatter;

public class WeekDaysAdapter extends RecyclerView.Adapter<WeekDaysAdapter.WeekDayHolder> {

    private OnWeekDayClickListener listener;
    private List<Driver.WeekDay> list;

    public WeekDaysAdapter(OnWeekDayClickListener listener, List<Driver.WeekDay> list) {
        this.listener = listener;
        this.list = list;
    }

    @NonNull
    @Override
    public WeekDayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_day, null);
        return new WeekDayHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekDayHolder holder, final int position) {
        final Driver.WeekDay day = list.get(position);

        holder.subtitle.setText(day.getOrderCount() + " поездок");
        holder.value.setText(day.getTotal()+"\u20BD");

        Date date = DateFormatter.getDateFromStringUTCTimeZone(day.getDate());
        if (date != null){
            String formattedDate = DateFormatter.getStringDdMmmmEeFromDate(date);
            holder.title.setText(formattedDate);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onWeekDayClick(day);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class WeekDayHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subtitle;
        TextView value;
        ImageView arrow;

        WeekDayHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            value = itemView.findViewById(R.id.value);
            arrow = itemView.findViewById(R.id.arrow);
            subtitle.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.VISIBLE);
        }
    }


    public interface OnWeekDayClickListener {
        void onWeekDayClick(Driver.WeekDay day);
    }
}