package ru.sigmadigital.taxipro.fragments.rate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;

public class ActivityNavigatorFragment extends BaseNavigatorFragment implements View.OnClickListener {

    private TextView actionBarTitle;
    private ImageView backButton;

    public static ActivityNavigatorFragment newInstance() {
        return new ActivityNavigatorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.navigator_rate, null);

        actionBarTitle = v.findViewById(R.id.tv_title);
        backButton = v.findViewById(R.id.imv_back_button);
        backButton.setOnClickListener(this);


        actionBarTitle.setText("активность");
        backButton.setVisibility(View.GONE);


        replaceCurrentFragmentWith(getFragmentManager(), R.id.rate_container, ActivityFragment.newInstance(), false);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back_button: {
                if (getActivity() != null)
                    getActivity().onBackPressed();
                break;
            }
        }
    }


}