package ru.sigmadigital.taxipro.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateHolder> {

    private OnRateClickListener listener;
    private List<String> list;

    public RateAdapter(OnRateClickListener listener, List<String> list) {
        this.listener = listener;
        this.list = list;
    }

    @NonNull
    @Override
    public RateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rate, null);
        return new RateHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RateHolder holder, final int position) {
        String o = list.get(position);

        holder.title.setText("Капец...");
        holder.text.setText("Водитель ездит в шлепках на носки!!!");
        tapStar(3, holder.stars);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRateClick();
            }
        });
    }

    private void tapStar(int rate, List<ImageView> stars) {
        for (int i = 0; i < stars.size(); i++) {
            if (i <= rate - 1) {
                stars.get(i).setBackgroundDrawable(App.getAppContext().getResources().getDrawable(R.drawable.ic_star_small));
            } else {
                stars.get(i).setBackgroundDrawable(App.getAppContext().getResources().getDrawable(R.drawable.ic_star_small_border));
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class RateHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView text;
        List<ImageView> stars = new ArrayList<>();

        RateHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            stars.add((ImageView) itemView.findViewById(R.id.star1));
            stars.add((ImageView) itemView.findViewById(R.id.star2));
            stars.add((ImageView) itemView.findViewById(R.id.star3));
            stars.add((ImageView) itemView.findViewById(R.id.star4));
            stars.add((ImageView) itemView.findViewById(R.id.star5));
        }
    }

    public interface OnRateClickListener {
        void onRateClick();
    }
}
