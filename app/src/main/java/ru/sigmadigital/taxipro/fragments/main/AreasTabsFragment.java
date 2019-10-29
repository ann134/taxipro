package ru.sigmadigital.taxipro.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
import com.google.android.material.tabs.TabLayout;
*/

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.adapters.TabAdapter;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;


public class AreasTabsFragment extends BaseNavigatorFragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    /* private Fragment parentFragment;*/

    public static AreasTabsFragment newInstance() {
        return new AreasTabsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tabs, container, false);

        viewPager = v.findViewById(R.id.view_pager);
        tabLayout = v.findViewById(R.id.tab_layout);

        TabAdapter adapter = new TabAdapter(getChildFragmentManager());

        adapter.addFragment(AreasFragment.newInstance(false), "заказы");
        adapter.addFragment(AreasFragment.newInstance(true), "предзаказы");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);










        return v;
    }





    public void showFragment(Fragment fragment) {
        replaceCurrentFragmentWith(getFragmentManager(),
                getParentContainer(this.getView()),
                /* R.id.main_container,*/
                fragment,
                true);
    }


    /*public ViewPager getViewPager() {
        return viewPager;
    }*/
}