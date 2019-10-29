package ru.sigmadigital.taxipro.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;

public class MainNavigatorFragment extends BaseNavigatorFragment {

    public static MainNavigatorFragment newInstance() {
        return new MainNavigatorFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigator_main, container, false);

        replaceCurrentFragmentWith(getFragmentManager(), R.id.main__nav_container, MainActionBarFragment.newInstance(), false);

        return view;
    }

}
