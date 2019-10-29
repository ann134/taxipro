package ru.sigmadigital.taxipro.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.activities.MainActivity;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.main.HomeAddressFragment;
import ru.sigmadigital.taxipro.fragments.main.HomeAddressMapFragment;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.DriverSetting;
import ru.sigmadigital.taxipro.repo.ProfileRepository;
import ru.sigmadigital.taxipro.util.SettingsHelper;
import ru.sigmadigital.taxipro.view.SettingsView;

public class SettingsFragment extends BaseNavigatorFragment implements View.OnClickListener {

    private SettingsView volume;
    private SettingsView payment;
    private SettingsView address;
    private SettingsView theme;

    DriverSetting.DriverSettingsValue settingsValue;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        //tittle bar
        TextView title = v.findViewById(R.id.title);
        title.setText("настройки");
        title.setTextColor(getResources().getColor(R.color.colorAccent));
        v.findViewById(R.id.back_button).setOnClickListener(this);


        //views
        SettingsView distance = v.findViewById(R.id.distance);
        distance.setTitle("Дистанция");
        distance.setText("7 км");
        distance.setOnClickListener(this);

        payment = v.findViewById(R.id.payment);
        payment.setTitle("оплата");
        payment.setOnClickListener(this);

        theme = v.findViewById(R.id.theme);
        theme.setTitle("цветовая гамма");
        theme.setOnClickListener(this);

        SettingsView voice = v.findViewById(R.id.voice);
        voice.setTitle("Произношение заказов");
        voice.setText("вкл");
        voice.setOnClickListener(this);

        volume = v.findViewById(R.id.volume);
        volume.setTitle("Громкость");
        volume.setText("50%");
        volume.setOnClickListener(this);

        SettingsView font = v.findViewById(R.id.font);
        font.setTitle("Шрифт");
        font.setText("Кк");
        font.setOnClickListener(this);


        address = v.findViewById(R.id.address);
        address.setTitle("Домашний адресс");
        address.setOnClickListener(this);

        subscribeToProfile();

        return v;
    }



    private void subscribeToProfile() {
        final LiveData<Driver.Profile> liveData = ProfileRepository.getInstance().getProfileLiveData();
        liveData.observe(this, new Observer<Driver.Profile>() {
            @Override
            public void onChanged(Driver.Profile profile) {
                fillData(profile);
            }
        });
    }


    private void fillData(Driver.Profile profile){
        settingsValue = profile.getSettings();

        switch (profile.getSettings().getNightMode()) {
            case 0: {
                theme.setText("авто");
                break;
            }
            case 1: {
                theme.setText("ночная");
                break;
            }
            case 2: {
                theme.setText("дневная");
                break;
            }
        }

        if(profile.getSettings().isOnlyCash()){
            payment.setText("только наличные");
        } else {
            payment.setText("не только наличные");
        }

        if (profile.getSettings().getHomeAddressGeo() != null){
            address.setText(profile.getSettings().getHomeAddressGeo().getAddressId()+"");
        } else if (ProfileRepository.getInstance().getGeoAddressHome() != null) {
            address.setText(ProfileRepository.getInstance().getGeoAddressHome().getAddressId()+"");
        } else {
            address.setText("не указан");
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button: {

                Sender.getInstance().send("driverSetting.setup", new DriverSetting.Setup(settingsValue).toJson(), this.getClass().getSimpleName());


                if (getActivity() != null)
                    getActivity().finish();
                break;
            }

            case R.id.font: {
                changeTextSize();
                break;
            }

            case R.id.volume: {
                changeVolume();
                break;
            }

            case R.id.address: {
                showHomeFragment();
                break;
            }

        }
    }


    private void changeVolume() {
        int volumeInt = Integer.parseInt(volume.getText().substring(0, volume.getText().length() - 1));

        if (volumeInt < 100) {
            volumeInt = volumeInt + 10;
        } else {
            volumeInt = 10;
        }

        volume.setText(volumeInt + "%");
    }

    private void changeTextSize() {
        if (SettingsHelper.getTextSize() < 3) {
            SettingsHelper.setNewTextSise(SettingsHelper.getTextSize() + 1);
        } else {
            SettingsHelper.setNewTextSise(1);
        }

        App.setTextSize(getActivity());
        if (getActivity() != null) {
            getActivity().recreate();
        }
        MainActivity.recreateActivity();
    }


    private void showHomeFragment(){
        replaceCurrentFragmentWith(getFragmentManager(),
                getParentContainer(this.getView()),
                HomeAddressFragment.newInstance(),
                true);
    }
}
