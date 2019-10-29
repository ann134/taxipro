package ru.sigmadigital.taxipro.fragments.registr;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.activities.LoginActivity;
import ru.sigmadigital.taxipro.api.MyLocationListener;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseFragment;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogChoose;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogInfo;
import ru.sigmadigital.taxipro.models.City;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.Geo;
import ru.sigmadigital.taxipro.models.Ok;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.repo.ProfileRepository;

public class RegistrationCityFragment extends BaseFragment implements View.OnClickListener, DialogChoose.OnAcceptClickListener, DialogInfo.DialogListener {

    private TextView city;
    private TextView agreement;
    private Button send;

    List<City.Item> cityList;

    public static RegistrationCityFragment newInstance() {
        return new RegistrationCityFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_registration_city, null);
        View view = inflater.inflate(R.layout.fragment_registration_city, container, false);

        if (getActivity() != null && getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).showSecondToolbar();
            ((LoginActivity) getActivity()).setTittleToolbar("регистрация 1/3");
        }

        city = view.findViewById(R.id.tv_city);
        agreement = view.findViewById(R.id.tv_agreement);
        send = view.findViewById(R.id.btn_send);

        send.setOnClickListener(this);
        city.setOnClickListener(this);

        sendRequest();


        return view;
    }

    private void sendRequest() {
        City.ItemFilter cityItemFilter = new City.ItemFilter(new Geo(MyLocationListener.getInstance().getLocation()), 0, 999);
        Sender.getInstance().send("city.item", cityItemFilter.toJson(), this.getClass().getSimpleName());

        LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(@Nullable ReceivedResponse responce) {
                if (responce != null) {
                    onDataReceived(responce);
                }
            }
        });
    }


    private <T> void onDataReceived(ReceivedResponse responce) {
        if (responce.getSenderFragment() != null && responce.getSenderFragment().equals(this.getClass().getSimpleName()) && responce.getData() != null) {


            if (responce.getData().getClass().equals(ArrayList.class)) {
                ArrayList<T> list = (ArrayList<T>) responce.getData();
                cityList = new ArrayList<>();
                for (T item : list) {
                    if (item instanceof City.Item) {
                        cityList.add((City.Item) item);
                    }
                }

                return;
            }

            if (responce.getData().getClass().equals(Ok.class)) {
                Log.e("Ok", "ok");
                return;
            }

            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("Error", "Error");
                return;
            }

            Log.e("unknownDataProfileFr", responce.getData().getClass().toString());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:

                if (ProfileRepository.getInstance().getProfile().getCityId() == 0) {
                    DialogInfo dialogInfo = DialogInfo.newInstance(this, R.drawable.ic_warning, "Ошибка", "Выберите город", "ОК");
                    dialogInfo.show(getFragmentManager(), "dialog");
                } else {
                    loadFragment(RegistrationFotosFragment.newInstance(RegistrationFotosFragment.TYPE_DOCS), "Reg2", true);
                }

                break;

            case R.id.tv_city:
                DialogChoose dialog = DialogChoose.newInstance(this, "Город");
               /* List<String> list = new ArrayList<>();
                Collections.addAll(list,getResources().getStringArray(R.array.cities_for_choose));*/
                List<String> list = new ArrayList<>();
                for (City.Item item : cityList) {
                    list.add(item.getName());
                }
                dialog.setDataList(list);
                dialog.show(getFragmentManager(), "dialog");
                break;
        }
    }

    @Override
    public void OnAcceptClick(String cityName) {
        for (City.Item item : cityList) {
            if (cityName.equals(item.getName())) {
                city.setText(item.getName());
                ProfileRepository.getInstance().setCity(item.getId());
            }
        }
    }

    /*@Override
    public void onActionClick(int action) {

    }*/


    @Override
    protected int getFragmentContainer() {
        return R.id.fragment_container;
    }

    @Override
    public void onActionClick(int action) {

    }
}
