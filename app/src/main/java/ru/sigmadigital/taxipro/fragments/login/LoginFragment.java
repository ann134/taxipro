package ru.sigmadigital.taxipro.fragments.login;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.activities.LoginActivity;
import ru.sigmadigital.taxipro.activities.MainActivity;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseFragment;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogInfoTwo;
import ru.sigmadigital.taxipro.models.Country;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.Geo;
import ru.sigmadigital.taxipro.models.Ok;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.models.Token;
import ru.sigmadigital.taxipro.util.SettingsHelper;


public class LoginFragment extends BaseFragment implements View.OnClickListener, RegionsFragment.OnRegionItemClick, DialogInfoTwo.DialogListener {

    public static int TYPE_LOGIN = 76;
    public static int TYPE_MODIFY = 78;

    int type;

    Country.AuthenticationCountry currentCountry;
    List<Country.AuthenticationCountry> countries;


    private int ANSWER_ACCEPT_NUMBER = 45;

    private ImageView logo;
    private TextView codeWillSend;

    private TextView region;
    private EditText phone;

    private FrameLayout regionsContainer;

    private Button enter;
    private Button register;

    String phoneStr;

    public static LoginFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("type")) {
                type = getArguments().getInt("type");
            }
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("type")) {
                type = savedInstanceState.getInt("type");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_login, container, false);

        if (getActivity() != null && getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).hideToolbar();
        }

        logo = v.findViewById(R.id.logo);
        codeWillSend = v.findViewById(R.id.text_code);

        region = v.findViewById(R.id.region);
        phone = v.findViewById(R.id.phone);
        regionsContainer = v.findViewById(R.id.regions_container);

        enter = v.findViewById(R.id.enter);
        register = v.findViewById(R.id.registr);

        enter.setOnClickListener(this);
        register.setOnClickListener(this);
        region.setOnClickListener(this);
        v.findViewById(R.id.flag).setOnClickListener(this);
        v.findViewById(R.id.tv_next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });


        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    logo.setVisibility(View.GONE);
                    codeWillSend.setVisibility(View.VISIBLE);
                    if (getActivity() != null && getActivity() instanceof LoginActivity) {
                        ((LoginActivity) getActivity()).showToolbar();
                        ((LoginActivity) getActivity()).hideBackBtnToolbar();

                    }
                }
            }
        });

        if (getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).setWatcher(new LoginActivity.OnKeyboardWatcher() {
                @Override
                public void keyboardOpen() {
                    enter.setVisibility(View.INVISIBLE);
                    register.setVisibility(View.INVISIBLE);
                    TextView textAccept = v.findViewById(R.id.text_accept);
                    textAccept.setVisibility(View.INVISIBLE);
                    TextView btnNext = v.findViewById(R.id.tv_next_button);
                    btnNext.setVisibility(View.VISIBLE);

                }

                @Override
                public void keyboardClose() {
                    enter.setVisibility(View.VISIBLE);
                    register.setVisibility(View.VISIBLE);
                    TextView textAccept = v.findViewById(R.id.text_accept);
                    textAccept.setVisibility(View.VISIBLE);
                    TextView btnNext = v.findViewById(R.id.tv_next_button);
                    btnNext.setVisibility(View.GONE);
                }
            });
        }

        Country.AuthenticationCountryFilter authenticationCountryFilter = new Country.AuthenticationCountryFilter(new Geo(59.931107, 30.437443), 0, 10);
        Sender.getInstance().send("country.authenticationCountry", authenticationCountryFilter.toJson(), this.getClass().getSimpleName());

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

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

            //Log.e("onDataReceived", responce.getData().toString());

            if (responce.getData().getClass().equals(Token.DriverAppSignedIn.class)) {
                Token.DriverAppSignedIn token = (Token.DriverAppSignedIn) responce.getData();
                if (token != null) {
                    Log.e("gettoken", token.getEntityId());
                    SettingsHelper.setToken(token);
                    loadFragment(CodeFragment.newInstance(type, phoneStr), getResources().getString(R.string.app_name), true);
                }
                return;
            }


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
                if (error.getStatusCode() == 400) {
                    showDialogError();
                }
                return;
            }

            if (responce.getData().getClass().equals(Ok.class)) {
                loadFragment(CodeFragment.newInstance(type, phoneStr), getResources().getString(R.string.app_name), true);
            }

            Log.e("errorfragm", "unknown");

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.region: {
                showRegionsFragment();
                break;
            }
            case R.id.flag: {
                showRegionsFragment();
                break;
            }
            case R.id.enter: {
                showDialogAcceptPhone();
                break;
            }
            case R.id.registr: {
                showDialogAcceptPhone();
                break;
            }
        }
    }


    private void showRegionsFragment() {
        RegionsFragment fragment = RegionsFragment.newInstance();
        fragment.setListener(this);
        fragment.setCountries(countries);
        loadFragment(fragment, getResources().getString(R.string.app_name), true, R.id.regions_container);
        regionsContainer.setVisibility(View.VISIBLE);
        enter.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
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
        enter.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActionClick(int action) {
        if (action == ANSWER_ACCEPT_NUMBER) {
            String number = /*"8" +*/ phone.getText().toString();
            number = number.replace(" ", "");

            if (type == TYPE_LOGIN) {
                Token.SignIn tokenSignIn = new Token.SignIn(currentCountry.getId(), number, 0);
                Sender.getInstance().send("token.signIn", tokenSignIn.toJson(), this.getClass().getSimpleName());
            }

            if (type == TYPE_MODIFY) {
                Sender.getInstance().send("driver.modifyPhone", new Driver.ModifyPhone(number).toJson(), this.getClass().getSimpleName());
            }
        }
    }


    @Override
    protected int getFragmentContainer() {
        return R.id.fragment_container;
    }
}
