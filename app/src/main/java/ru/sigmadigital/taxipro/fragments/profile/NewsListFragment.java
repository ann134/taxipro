package ru.sigmadigital.taxipro.fragments.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.adapters.NewsAdapter;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.DriverNews;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.repo.FeedRepository;

public class NewsListFragment extends BaseNavigatorFragment implements NewsAdapter.OnNewsClickListener {

    private ActionBarListener actionBarListener;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorText;

    public static NewsListFragment newInstance(ActionBarListener listener) {
        NewsListFragment fragment = new NewsListFragment();
        fragment.setActionBarListener(listener);
        return fragment;
    }

    private void setActionBarListener(ActionBarListener actionBarListener) {
        this.actionBarListener = actionBarListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_list, container, false);

        if (actionBarListener != null) {
            actionBarListener.onSetTitle("новости");
            actionBarListener.onShowBackButton(true);
        }

        errorText = v.findViewById(R.id.error);
        progressBar = v.findViewById(R.id.progress_bar);
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(App.getAppContext(), 1));

        sendRequest();

        return v;
    }

    private void sendRequest() {
        Sender.getInstance().send("driverNews.listItem", new DriverNews.ListItemFilter(0, 999).toJson(), this.getClass().getSimpleName() + " listItem");
        Sender.getInstance().send("driver.readLastNews", new Driver.ReadLastNews().toJson(), this.getClass().getSimpleName() + " readLastNews");


        LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(@Nullable ReceivedResponse responce) {
                if (responce != null) {
                    onNewsReceived(responce);
                }
            }
        });
    }

    private<T> void onNewsReceived(ReceivedResponse responce) {
        if (responce.getSenderFragment() != null && responce.getSenderFragment().equals(this.getClass().getSimpleName()) && responce.getData() != null) {

            if (responce.getData().getClass().equals(ArrayList.class)) {
                Log.e("ArrayList", "ArrayList");
                ArrayList<T> list = (ArrayList<T>) responce.getData();
                List<DriverNews.ListItem> news  = new ArrayList<>();
                for (T item : list) {
                    if ( item instanceof DriverNews.ListItem) {
                        news.add((DriverNews.ListItem) item);
                    }
                }

                showNews(news);

                return;
            }


            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("Error", "Error");
                if (responce.getSenderRequest().equals("listItem")){
                    progressBar.setVisibility(View.GONE);
                    errorText.setVisibility(View.VISIBLE);
                    Error error = (Error) responce.getData();
                    errorText.setText("Ошибка:"+ error.getMessage());
                }

                return;
            }

            Log.e("unknownDataNewsFr", responce.getData().getClass().toString());
        }
    }

    private void showNews(List<DriverNews.ListItem> news ){
        progressBar.setVisibility(View.GONE);
        NewsAdapter adapter = new NewsAdapter(this, news);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onNewsClick(DriverNews.ListItem newsItem) {
        showFragment(NewsItemFragment.newInstance(actionBarListener, newsItem));
    }

    private void showFragment(Fragment fragment) {
        replaceCurrentFragmentWith(getFragmentManager(),
                getParentContainer(this.getView()),
                fragment,
                true);
    }
}
