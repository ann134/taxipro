package ru.sigmadigital.taxipro.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.Area;
import ru.sigmadigital.taxipro.models.my.OrdersInArea;

public class AreasAdapter extends RecyclerView.Adapter<AreasAdapter.AreaHolder> {

    private OnAreaClickListener listener;
    private List<OrdersInArea> list;

    public AreasAdapter(OnAreaClickListener listener, List<OrdersInArea> list) {
        this.listener = listener;
        this.list = list;
    }

    @NonNull
    @Override
    public AreaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, null);

        WindowManager wm = (WindowManager) v.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int margin = (int) (1 * v.getContext().getResources().getDisplayMetrics().density);
        int side = (size.x / 4) - 2 * margin; // изменить на количество столбцов

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(side, side);
        params.setMargins(margin, margin, margin, margin);
        v.setLayoutParams(params);

        return new AreaHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaHolder holder, final int position) {
        final OrdersInArea areaOrders = list.get(position);

        holder.name.setText(areaOrders.getAreaName());
        if (areaOrders.getOrdersCount() != 0){
            holder.number.setText(areaOrders.getOrdersCount() + "");
        }

        holder.layout.setBackgroundColor(App.getAppContext().getResources().getColor(getColorBg(areaOrders.getOrdersCount())));
        holder.name.setTextColor(App.getAppContext().getResources().getColor(getColorText(areaOrders.getOrdersCount())));
        holder.number.setTextColor(App.getAppContext().getResources().getColor(getColorText(areaOrders.getOrdersCount())));

        //location
        /*if (area.getName().contains("адмира")) {
            holder.location.setVisibility(View.VISIBLE);
        }*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAreaClick(areaOrders.getArea());
            }
        });
    }

    private int getColorBg(int type) {
        if (type == 0){
            return R.color.areaWhite;
        }

        if (type > 7) {
            return R.color.areaRed;
        }

        return R.color.areaYellow;
    }

    private int getColorText(int type) {
        if (type == 0){
            return R.color.textGrayLight;
        }

        if (type > 7) {
            return R.color.white;
        }

        return R.color.black;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class AreaHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        TextView name;
        TextView number;
        ImageView location;

        AreaHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            layout = itemView.findViewById(R.id.layout);
            location = itemView.findViewById(R.id.location);
        }
    }

    public interface OnAreaClickListener {
        void onAreaClick(Area.AreaPanel area);
    }
}
