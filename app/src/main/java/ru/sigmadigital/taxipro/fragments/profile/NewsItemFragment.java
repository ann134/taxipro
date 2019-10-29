package ru.sigmadigital.taxipro.fragments.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.models.DriverNews;
import ru.sigmadigital.taxipro.util.DateFormatter;

public class NewsItemFragment extends BaseNavigatorFragment {

    private ActionBarListener actionBarListener;
    private DriverNews.ListItem newsItem;

    public static NewsItemFragment newInstance(ActionBarListener listener, DriverNews.ListItem newsItem) {
        NewsItemFragment fragment = new NewsItemFragment();
        Bundle args = new Bundle();
        args.putSerializable("newsItem", newsItem);
        fragment.setArguments(args);
        fragment.setActionBarListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("newsItem")) {
                newsItem = (DriverNews.ListItem) getArguments().getSerializable("newsItem");
            }
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("newsItem")) {
                newsItem = (DriverNews.ListItem) savedInstanceState.getSerializable("newsItem");
            }
        }
    }

    private void setActionBarListener(ActionBarListener actionBarListener) {
        this.actionBarListener = actionBarListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_news_item, null);

        if (newsItem != null) {

            if (actionBarListener != null) {
                actionBarListener.onSetTitle(newsItem.getTitle());
                actionBarListener.onShowBackButton(true);
                //actionBarListener.onShowTrashButton(true);
            }

            TextView text = v.findViewById(R.id.text);
            TextView address = v.findViewById(R.id.address);
            TextView when = v.findViewById(R.id.when);

            text.setText(newsItem.getText());
            //address.setText("Можайское шоссе 56");

            Date date = DateFormatter.getDateFromStringUTCTimeZone(newsItem.getDate());
            if (date != null) {
                String formattedDate = DateFormatter.getStringDdMmmmEeFromDate(date);
                when.setText(formattedDate);
            }
        } else {
            TextView text = v.findViewById(R.id.text);
            text.setText("что то пошло не так");
        }

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (actionBarListener != null) {
            actionBarListener.onShowTrashButton(false);
        }
    }
}
