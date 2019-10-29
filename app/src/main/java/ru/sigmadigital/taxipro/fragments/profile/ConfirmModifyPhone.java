package ru.sigmadigital.taxipro.fragments.profile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.activities.MainActivity;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.fragments.dialogs.DialogInfo;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.Ok;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;


public class ConfirmModifyPhone extends BaseNavigatorFragment implements DialogInfo.DialogListener, View.OnClickListener {

    private ActionBarListener actionBarListener;

    private String phone;

    private EditText code;
    private TextView sendAgain;


    public static ConfirmModifyPhone newInstance(ActionBarListener listener, String phone) {
        Bundle args = new Bundle();
        args.putString("phone", phone);
        ConfirmModifyPhone fragment = new ConfirmModifyPhone();
        fragment.setArguments(args);
        fragment.setActionBarListener(listener);
        return fragment;
    }

    private void setActionBarListener(ActionBarListener actionBarListener) {
        this.actionBarListener = actionBarListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey("phone")) {
                phone = getArguments().getString("phone");
            }
        }
        if (savedInstanceState != null) {
            if (getArguments().containsKey("phone")) {
                phone = getArguments().getString("phone");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_code, container, false);

        if (actionBarListener != null) {
            actionBarListener.onSetTitle("подтвердить телефон");
            actionBarListener.onShowBackButton(true);
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
                if (responce != null) {
                    onDataReceived(responce);
                }
            }
        });
    }

    private void onDataReceived(ReceivedResponse responce) {
        if (responce.getData() != null && responce.getSenderFragment() != null && responce.getSenderFragment().equals(this.getClass().getSimpleName())) {

            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("errorfragm", "known");
                Error error = (Error) responce.getData();
                showDialog();
                return;
            }

            if (responce.getData().getClass().equals(Ok.class)) {
                if (getActivity() != null && getActivity() instanceof MainActivity) {
                    getActivity().onBackPressed();
                }
            }

            Log.e("errorfragm", "unknown");
        }
    }


    private void codeWasEnter(String code) {
        Sender.getInstance().send("driver.confirmModifyingPhone", new Driver.ConfirmModifyingPhone(code).toJson(), this.getClass().getSimpleName());
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

    @Override
    public void onActionClick(int action) {
        code.setText("");
    }

}
