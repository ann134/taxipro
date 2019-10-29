package ru.sigmadigital.taxipro.fragments.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.api.MyLocationListener;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogInfoTwo;
import ru.sigmadigital.taxipro.fragments.login.RegionsFragment;
import ru.sigmadigital.taxipro.models.Country;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.Geo;
import ru.sigmadigital.taxipro.models.Ok;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;

public class ModifyPhone extends BaseNavigatorFragment implements View.OnClickListener, RegionsFragment.OnRegionItemClick, DialogInfoTwo.DialogListener {

    private ActionBarListener actionBarListener;

    private Country.AuthenticationCountry currentCountry;
    private List<Country.AuthenticationCountry> countries;

    private int ANSWER_ACCEPT_NUMBER = 45;

    private TextView region;
    private EditText phone;
    private FrameLayout regionsContainer;

    private String phoneStr;


    public static ModifyPhone newInstance(ActionBarListener listener) {
        ModifyPhone fragment = new ModifyPhone();
        fragment.setActionBarListener(listener);
        return fragment;
    }

    private void setActionBarListener(ActionBarListener actionBarListener) {
        this.actionBarListener = actionBarListener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_modify_phone, container, false);

        if (actionBarListener != null) {
            actionBarListener.onSetTitle("сменить телефон");
            actionBarListener.onShowBackButton(true);
        }

        region = v.findViewById(R.id.region);
        phone = v.findViewById(R.id.phone);
        regionsContainer = v.findViewById(R.id.regions_container);

        Button btnNext = v.findViewById(R.id.next_button);
        btnNext.setOnClickListener(this);
        region.setOnClickListener(this);
        v.findViewById(R.id.flag).setOnClickListener(this);

        Country.AuthenticationCountryFilter authenticationCountryFilter = new Country.AuthenticationCountryFilter(new Geo(MyLocationListener.getInstance().getLocation()), 0, 999);
        Sender.getInstance().send("country.authenticationCountry", authenticationCountryFilter.toJson(), this.getClass().getSimpleName());

        subscribeToResponces();

        return v;
    }

    private void subscribeToResponces() {
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
        if (responce.getData() != null && responce.getSenderFragment() != null && responce.getSenderFragment().equals(this.getClass().getSimpleName())) {

           /* if (responce.getData().getClass().equals(Token.DriverAppSignedIn.class)) {
                Token.DriverAppSignedIn token = (Token.DriverAppSignedIn) responce.getData();
                if (token != null) {
                    Log.e("gettoken", token.getEntityId());
                    SettingsHelper.setToken(token);
                    //loadFragment(CodeFragment.newInstance(type, phoneStr), getResources().getString(R.string.app_name), true);
                    showFragment(ConfirmModifyPhone.newInstance(actionBarListener, phoneStr));
                }
                return;
            }*/

            if (responce.getData().getClass().equals(ArrayList.class)) {
                Log.e("Country", "AuthenticationCountry");
                ArrayList<T> list = (ArrayList<T>) responce.getData();
                countries = new ArrayList<>();
                for (T item : list) {
                    if (item instanceof Country.AuthenticationCountry) {
                        countries.add((Country.AuthenticationCountry) item);
                    }
                }

                if (!countries.isEmpty()) {
                    currentCountry = countries.get(0);
                    region.setText(countries.get(0).getCountryCode() + "");
                }

                return;
            }

            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("errorfragm", "known");
                Error error = (Error) responce.getData();
                showDialogError();
                return;
            }

            if (responce.getData().getClass().equals(Ok.class)) {
                showFragment(ConfirmModifyPhone.newInstance(actionBarListener, phoneStr));
            }

            Log.e("errorfragm", "unknown");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.region: {
                if (countries != null) {
                    showRegionsFragment();
                } else {
                    Toast.makeText(App.getAppContext(), "Не удалось получить", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.flag: {
                showRegionsFragment();
                break;
            }
            case R.id.next_button: {
                showDialogAcceptPhone();
                break;
            }
        }
    }


    private void showRegionsFragment() {
        RegionsFragment fragment = RegionsFragment.newInstance();
        fragment.setListener(this);
        fragment.setCountries(countries);
        // loadFragment(fragment, getResources().getString(R.string.app_name), true, R.id.regions_container);

        replaceCurrentFragmentWith(getFragmentManager(),
                R.id.regions_container,
                fragment,
                true);

        regionsContainer.setVisibility(View.VISIBLE);
        //enter.setVisibility(View.GONE);
        //register.setVisibility(View.GONE);
    }

    private void showDialogAcceptPhone() {
        phoneStr = region.getText() + " " + phone.getText();
        DialogInfoTwo dialogFragment = DialogInfoTwo.newInstance(this, R.drawable.logo_phone,
                getString(R.string.confirm_number), String.format(getString(R.string.yours_number), "\n" + region.getText() + phone.getText()),
                getString(R.string.change), getString(R.string.my_number));
        dialogFragment.setTypeAnswer(ANSWER_ACCEPT_NUMBER);

        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        if (getFragmentManager() != null)
            dialogFragment.show(getFragmentManager(), "DialogInfoTwo");
    }

    private void showDialogError() {
        DialogInfoTwo dialogFragment = DialogInfoTwo.newInstance(this, R.drawable.logo_alert,
                getString(R.string.error), getString(R.string.text_error_number),
                getString(R.string.registr_s), getString(R.string.ok));

        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        if (getFragmentManager() != null)
            dialogFragment.show(getFragmentManager(), "DialogInfoTwo");
    }


    @Override
    public void OnRegionClick(Country.AuthenticationCountry region) {
        this.region.setText(region.getCountryCode());
        currentCountry = region;
        //enter.setVisibility(View.VISIBLE);
        // register.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActionClick(int action) {
        if (action == ANSWER_ACCEPT_NUMBER) {
            String number = /*"8" +*/ phone.getText().toString();
            number = number.replace(" ", "");

            Sender.getInstance().send("driver.modifyPhone", new Driver.ModifyPhone(number).toJson(), this.getClass().getSimpleName());

        }
    }


    private void showFragment(Fragment fragment) {
        replaceCurrentFragmentWith(getFragmentManager(),
                getParentContainer(this.getView()),
                fragment,
                true);
    }


}
