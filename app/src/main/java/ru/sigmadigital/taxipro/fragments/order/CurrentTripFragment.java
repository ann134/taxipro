package ru.sigmadigital.taxipro.fragments.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.sigmadigital.taxipro.R;

public class CurrentTripFragment extends Fragment {

    public static CurrentTripFragment newInstance() {
        return new CurrentTripFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_trip, container, false);



        return v;
    }
}
