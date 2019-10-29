package ru.sigmadigital.taxipro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.util.DateFormatter;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityHolder> {

    private List<Driver.ActivityHistory> itemList;

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(App.getAppContext()).inflate(R.layout.item_change_activity, null);
        return new ActivityHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
        holder.setIsRecyclable(false);
        Date date = DateFormatter.getDateFromStringUTCTimeZone(itemList.get(position).getDate());

        holder.date.setText("Дата:" + DateFormatter.getSimpleDate(date) + " " + DateFormatter.getSimpleTime(date));
        StringBuilder sb = new StringBuilder();
        sb.append("Актив: ");
        sb.append(itemList.get(position).getActivityValue());
        if (itemList.get(position).getActivityDelta() > 0) {
            sb.append(" + ").append(itemList.get(position).getActivityDelta());
        } else {
            sb.append(" - ").append(itemList.get(position).getActivityDelta() * (-1));
        }
        holder.activity.setText(sb.toString());
        holder.desc.setText(itemList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        if (itemList != null)
            return itemList.size();
        else return 0;
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView activity;
        private TextView desc;

        public ActivityHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tv_date);
            activity = itemView.findViewById(R.id.tv_change_activity);
            desc = itemView.findViewById(R.id.tv_desc);
        }
    }

    public List<Driver.ActivityHistory> getItemList() {
        return itemList;
    }

    public void setItemList(List<Driver.ActivityHistory> itemList) {
        this.itemList = itemList;
    }
}
