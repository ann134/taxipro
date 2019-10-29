package ru.sigmadigital.taxipro.fragments.earn;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;

public class DayOrderDetailsFragment extends BaseNavigatorFragment implements View.OnClickListener {


    public static DayOrderDetailsFragment newInstance() {
        return new DayOrderDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_trip_details, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        EarnActionBarFragment.setActionBarTitle("подробно");
        EarnActionBarFragment.setBackButtonVisible(true);


        return v;
    }


    @Override
    public void onClick(View v) {


    }
}
