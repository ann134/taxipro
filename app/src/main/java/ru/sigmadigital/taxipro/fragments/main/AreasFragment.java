package ru.sigmadigital.taxipro.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.adapters.AreasAdapter;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.models.Area;
import ru.sigmadigital.taxipro.models.my.OrdersInArea;
import ru.sigmadigital.taxipro.repo.AreaRepository;
import ru.sigmadigital.taxipro.repo.OrdersRepository;


public class AreasFragment extends BaseNavigatorFragment implements AreasAdapter.OnAreaClickListener {

    private boolean isPreorders;

    private AreasTabsFragment parentFragment;
    private RecyclerView recyclerView;


    public static AreasFragment newInstance(boolean isPreorders) {
        AreasFragment fragment = new AreasFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isPreorders", isPreorders);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("isPreorders")) {
            isPreorders = getArguments().getBoolean("isPreorders");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_areas_recycler, container, false);

        parentFragment = (AreasTabsFragment) this.getParentFragment();
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(App.getAppContext(), 4));


        if (isPreorders){
            subscribeToAreaOrders(OrdersRepository.getInstance().getPreordersInAreasLiveData());
        } else {
            subscribeToAreaOrders(OrdersRepository.getInstance().getOrdersInAreasLiveData());
        }

        return v;
    }


    private void subscribeToAreaOrders(LiveData<List<OrdersInArea>> liveData) {
        liveData.observe(this, new Observer<List<OrdersInArea>>() {
            @Override
            public void onChanged(List<OrdersInArea> ordersAreas) {
                initRecycler(ordersAreas);
            }
        });
    }

    private void initRecycler(List<OrdersInArea> areasList) {
        AreasAdapter adapter = new AreasAdapter(this, areasList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAreaClick(Area.AreaPanel area) {
        parentFragment.showFragment(OrdersListFragment.newInstance(area, isPreorders));
    }
}
