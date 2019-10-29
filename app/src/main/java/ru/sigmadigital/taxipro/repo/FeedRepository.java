package ru.sigmadigital.taxipro.repo;

import androidx.lifecycle.MutableLiveData;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Order;

public class FeedRepository {


    private static FeedRepository instance;
    private MutableLiveData<Driver.Feed> feedLiveData = new MutableLiveData<>();

    public static FeedRepository getInstance() {
        if (instance == null) {
            instance = new FeedRepository();
        }
        return instance;
    }

    public void setFeed(Driver.Feed feed) {
        this.feedLiveData.setValue(feed);
    }

    public Driver.Feed getFeed() {
        return feedLiveData.getValue();
    }

    public MutableLiveData<Driver.Feed> getFeedLiveData() {
        return feedLiveData;
    }

    public void addOrder(Order.DriverOrder order) {
        Driver.Feed feed = feedLiveData.getValue();
        feed.addOrder(order);
        feedLiveData.setValue(feed);
    }

}
