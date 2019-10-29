package ru.sigmadigital.taxipro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.VehicleGroup;
import ru.sigmadigital.taxipro.util.DateFormatter;

public class RatePeriodsAdapter extends RecyclerView.Adapter<RatePeriodsAdapter.WorkHolder> {


    private List<VehicleGroup.DriverRatePeriod> buyPeriods;
    private List<Driver.PaidRatePeriod> myPeriods;
    private boolean showDeleteButton;

    private OnPeriodClickListener listener;
    private OnDeletePeriodClickListener deleteListener;


    public RatePeriodsAdapter(OnPeriodClickListener listener, List<VehicleGroup.DriverRatePeriod> list/*, boolean showDeleteButton*/) {
        this.listener = listener;
        this.buyPeriods = list;
    }

    public RatePeriodsAdapter(OnPeriodClickListener listener, OnDeletePeriodClickListener deleteListener, List<Driver.PaidRatePeriod> list, boolean showDeleteButton) {
        this.listener = listener;
        this.myPeriods = list;
        this.deleteListener = deleteListener;
        this.showDeleteButton = showDeleteButton;
    }


    @NonNull
    @Override
    public WorkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work, null);
        view.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new WorkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkHolder holder, final int position) {

        //смены для покупки
        if (buyPeriods != null) {
            final VehicleGroup.DriverRatePeriod period = buyPeriods.get(position);

            switch (period.getHours()) {
                case 12:
                    holder.round.setImageDrawable(App.getAppContext().getResources().getDrawable(R.drawable.ic_circle_12));
                case 24:
                    holder.round.setImageDrawable(App.getAppContext().getResources().getDrawable(R.drawable.ic_circle_24));
            }
            holder.hours.setText(period.getHours() + "");


            holder.name.setText(period.getName());
            holder.time.setVisibility(View.GONE);

            holder.price.setText(period.getPrice()+"\u20BD");

            holder.close.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onPeriodClick(position);
                }
            });
        }


        if (myPeriods != null) {
            final Driver.PaidRatePeriod period = myPeriods.get(position);

            //откатанные смены
            if (period != null) {

                holder.name.setText(period.getName());

                Date startDate = DateFormatter.getDateFromStringUTCTimeZone(period.getFrom());
                String startDateString = DateFormatter.getStringDateTimeFromDate(startDate);
                Date endDate = DateFormatter.getDateFromStringUTCTimeZone(period.getTo());
                String endDateString = DateFormatter.getStringDateTimeFromDate(endDate);
                holder.time.setText(startDateString + " - " + endDateString);

                holder.price.setText(period.getPrice() + "\u20BD");

                String hours = DateFormatter.getHoursDifference(startDate, endDate);
                holder.hours.setText(hours);
                switch (hours) {
                    case "12":
                        holder.round.setImageDrawable(App.getAppContext().getResources().getDrawable(R.drawable.ic_circle_12));
                    case "24":
                        holder.round.setImageDrawable(App.getAppContext().getResources().getDrawable(R.drawable.ic_circle_24));
                }

                if (showDeleteButton) {
                    holder.close.setVisibility(View.VISIBLE);
                    holder.close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (deleteListener != null)
                                deleteListener.onDeletePeriodClick();
                        }
                    });
                } else {
                    holder.close.setVisibility(View.GONE);
                }

            } else {
                holder.round.setImageDrawable(App.getAppContext().getDrawable(R.drawable.ic_circle_full));
                holder.hours.setText("");
                holder.name.setText("Круглосуточно");
                holder.time.setText("Прием заказов с комиссией");
                holder.line.setVisibility(View.GONE);
                holder.price.setText("");
                holder.close.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (buyPeriods != null)
            return buyPeriods.size();
        if (myPeriods != null)
            return myPeriods.size();
        return 0;
    }



    class WorkHolder extends RecyclerView.ViewHolder {
        ImageView round;
        TextView hours;
        TextView name;
        TextView time;
        View line;
        TextView price;
        ImageView close;

        WorkHolder(@NonNull View itemView) {
            super(itemView);
            round = itemView.findViewById(R.id.imv_round);
            hours = itemView.findViewById(R.id.tv_hours_count);
            name = itemView.findViewById(R.id.tv_time);
            time = itemView.findViewById(R.id.tv_status);
            line = itemView.findViewById(R.id.dash);
            price = itemView.findViewById(R.id.tv_cost);
            close = itemView.findViewById(R.id.imv_close);
        }
    }


    public interface OnPeriodClickListener {
        void onPeriodClick(int index);
    }

    public interface OnDeletePeriodClickListener {
        void onDeletePeriodClick();
    }
}
