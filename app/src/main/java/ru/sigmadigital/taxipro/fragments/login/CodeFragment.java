package ru.sigmadigital.taxipro.fragments.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.activities.LoginActivity;
import ru.sigmadigital.taxipro.activities.MainActivity;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseFragment;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogInfo;
import ru.sigmadigital.taxipro.fragments.registr.RegistrationCityFragment;
import ru.sigmadigital.taxipro.fragments.registr.RegistrationFotosFragment;
import ru.sigmadigital.taxipro.models.AuthSession;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.Ok;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;
import ru.sigmadigital.taxipro.models.Token;
import ru.sigmadigital.taxipro.repo.ProfileRepository;
import ru.sigmadigital.taxipro.util.SettingsHelper;

public class CodeFragment extends BaseFragment implements DialogInfo.DialogListener, View.OnClickListener {

    public static int TYPE_LOGIN = 76;
    public static int TYPE_MODIFY = 78;

    int type;
    String phone;

    private EditText code;
    private TextView sendAgain;


    public static CodeFragment newInstance(int type, String phone) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("phone", phone);
        CodeFragment fragment = new CodeFragment();
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
            if (getArguments().containsKey("phone")) {
                phone = getArguments().getString("phone");
            }
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("type")) {
                type = savedInstanceState.getInt("type");
            }
            if (getArguments().containsKey("phone")) {
                phone = getArguments().getString("phone");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_code, container, false);

        if (getActivity() != null && getActivity() instanceof LoginActivity) {
            ((LoginActivity) getActivity()).showToolbar();
        }

        code = v.findViewById(R.id.code);
        TextView text = v.findViewById(R.id.text);
        text.setText(String.format(getResources().getString(R.string.code_was_send), phone));
        sendAgain = v.findViewById(R.id.text_send_again);
        sendAgain.setOnClickListener(this);

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code.getText().length() == 4) {
                    codeWasEnter(code.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(@Nullable ReceivedResponse responce) {
                if (responce != null){
                    onDataReceived(responce);
                }
            }
        });
    }

    private void onDataReceived(ReceivedResponse responce){
        if (responce.getData() != null && responce.getSenderFragment() != null && responce.getSenderFragment().equals(this.getClass().getSimpleName())){

            if (responce.getData().getClass().equals(Token.HasBeenConfirmed.class)){
                ((LoginActivity)getActivity()).setNextActivityExpected(true);

                AuthSession.ContinueSession continueSession = new AuthSession.ContinueSession(0, SettingsHelper.getToken().getEntityId());
                Sender.getInstance().send("authSession.continueSession", continueSession.toJson(), this.getClass().getSimpleName());

                return;
            }


            if (responce.getData().getClass().equals(AuthSession.HasBeenContinued.class)) {
                Log.e("AuthSession", "HasBeenContinued");

                Sender.getInstance().send("driver.profile", new Driver.ProfileFilter().toJson(), this.getClass().getSimpleName());

                return;
            }

            if (responce.getData().getClass().equals(Driver.Profile.class)) {
                ProfileRepository.getInstance().setProfile((Driver.Profile) responce.getData());
                //Log.e("prof", "" + ProfileRepository.getInstance().getProfileLiveData().getPhone());

                if (ProfileRepository.getInstance().getProfile().getState() == Driver.DriverState.created){
                    //loadFragment(RegistrationFotosFragment.newInstance(1), "Reg2", true);
                    loadFragment(RegistrationCityFragment.newInstance(), "Reg3", true);
                } else {
                    startMainActivity();
                }



                return;
            }

            if (responce.getData().getClass().equals(Error.class)){
                Log.e("errorfragm", "known");
                Error error = (Error)responce.getData();
                if (error.getStatusCode() == 400){
                    showDialog();
                }
                return;
            }

            if (responce.getData().getClass().equals(Ok.class)){
                if (getActivity() != null && getActivity() instanceof MainActivity){
                    getActivity().onBackPressed();
                }
            }


            Log.e("errorfragm", "unknown");
        }
    }
    
    
    private void codeWasEnter(String code){

        if (type == TYPE_LOGIN) {
            Token.ConfirmSignIn tokenSignIn = new Token.ConfirmSignIn(SettingsHelper.getToken().getEntityId(), code, 0);
            Sender.getInstance().send("token.confirmSignIn", tokenSignIn.toJson(), this.getClass().getSimpleName());
        }

        if (type == TYPE_MODIFY){
            Sender.getInstance().send("driver.confirmModifyingPhone", new Driver.ConfirmModifyingPhone(code).toJson(), this.getClass().getSimpleName());
        }

    }

    private void showDialog() {
        DialogInfo dialogFragment = DialogInfo.newInstance(this, R.drawable.logo_alert, getString(R.string.error), getString(R.string.code_error), getString(R.string.ok));
        dialogFragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        if (getFragmentManager() != null)
            dialogFragment.show(getFragmentManager(), "DialogInfo");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_send_again: {

                break;
            }
        }
    }

    private void startMainActivity(){
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    protected int getFragmentContainer() {
        return R.id.fragment_container;
    }

    @Override
    public void onActionClick(int action) {
        code.setText("");
    }


}
