package ru.sigmadigital.taxipro.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.DriverNews;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnNewsClickListener listener;
    private List<DriverNews.ListItem> list;

    public NewsAdapter(OnNewsClickListener listener, List<DriverNews.ListItem> list) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: {
                @SuppressLint("InflateParams") View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, null);
                return new NewsAdapter.DayHolder(v);
            }
            default: {
                @SuppressLint("InflateParams") View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, null);
                return new NewsAdapter.NewsHolder(v);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof NewsAdapter.NewsHolder) {
            final DriverNews.ListItem news = list.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNewsClick(news);
                }
            });

            ((NewsHolder) holder).tittle.setText(news.getTitle());
            ((NewsHolder) holder).text.setText(news.getText());
            ((NewsHolder) holder).indicator.setVisibility(View.GONE);


        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            /*case 0: {
                return 0;
            }
            case 3: {
                return 0;
            }*/
            default: {
                return 1;
            }
        }
    }

    class DayHolder extends RecyclerView.ViewHolder {
        TextView day;

        DayHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
        }
    }

    class NewsHolder extends RecyclerView.ViewHolder {
        TextView tittle;
        TextView text;
        ImageView indicator;

        NewsHolder(@NonNull View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            indicator = itemView.findViewById(R.id.indicator);
        }
    }


    public interface OnNewsClickListener {
        void onNewsClick(DriverNews.ListItem newsItem);
    }
}
