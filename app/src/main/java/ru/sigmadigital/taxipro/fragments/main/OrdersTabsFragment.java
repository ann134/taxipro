package ru.sigmadigital.taxipro.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.adapters.TabAdapter;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.models.Token;

import ru.sigmadigital.taxipro.util.SettingsHelper;

public class OrdersTabsFragment extends BaseNavigatorFragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;


    public static OrdersTabsFragment newInstance() {
        return new OrdersTabsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tabs, container, false);

        viewPager = v.findViewById(R.id.view_pager);
        tabLayout = v.findViewById(R.id.tab_layout);

        TabAdapter adapter = new TabAdapter(getChildFragmentManager());

        adapter.addFragment(OrdersListFragment.newInstance(false), "заказы");
        adapter.addFragment(OrdersListFragment.newInstance(true), "предзаказы");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);





        return v;
    }

}
