package ru.sigmadigital.taxipro.fragments.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.fragments.BaseFragment;
import ru.sigmadigital.taxipro.models.Country;

public class RegionsFragment extends BaseFragment implements View.OnClickListener {

    private WheelPicker wheelPicker;
    private List<String> regions;

    private OnRegionItemClick listener;
    private List<Country.AuthenticationCountry> countries;


    public static RegionsFragment newInstance() {
        return new RegionsFragment();
    }

    public void setListener(OnRegionItemClick listener) {
        this.listener = listener;
    }

    public void setCountries( List<Country.AuthenticationCountry> countries) {
        this.countries = countries;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_region, container, false);

        regions = new ArrayList<>();

        for (Country.AuthenticationCountry country : countries){
            regions.add(country.getName() +" "+ country.getCountryCode());
        }


        wheelPicker = v.findViewById(R.id.wheel_picker);
        wheelPicker.setData(regions);


        v.findViewById(R.id.rate).setOnClickListener(this);

        return v;
    }


    @Override
    protected int getFragmentContainer() {
        return R.id.regions_container;
    }


    @Override
    public void onClick(View v) {

        if (listener != null) {
            listener.OnRegionClick(getCountry(regions.get(wheelPicker.getCurrentItemPosition())));
        }

        if (getActivity() != null) {
            getActivity().onBackPressed();
        }

    }

    private Country.AuthenticationCountry getCountry(String str) {
        final String[] both = str.split("\\s");

        for (Country.AuthenticationCountry country : countries){
            if (both[1].equals(country.getCountryCode())) {
                return country;
            }
        }

        return null;
    }


    public interface OnRegionItemClick {
        void OnRegionClick(Country.AuthenticationCountry region);
    }
}
