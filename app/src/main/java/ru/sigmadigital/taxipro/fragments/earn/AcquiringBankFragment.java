package ru.sigmadigital.taxipro.fragments.earn;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.api.Receiver;
import ru.sigmadigital.taxipro.api.Sender;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.models.AcquiringBank;
import ru.sigmadigital.taxipro.models.Error;
import ru.sigmadigital.taxipro.models.my.ReceivedResponse;


public class AcquiringBankFragment extends BaseNavigatorFragment implements View.OnClickListener {

    private EditText editText;


    public static AcquiringBankFragment newInstance() {
        return new AcquiringBankFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_put_money, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        EarnActionBarFragment.setActionBarTitle("пополнить картой");
        EarnActionBarFragment.setBackButtonVisible(true);
        EarnActionBarFragment.setBackgroundTransparent(true);

        editText = v.findViewById(R.id.code);
        addTextListener();

        v.findViewById(R.id.enter).setOnClickListener(this);


        subscrubeToResponses();


        return v;
    }

    private void addTextListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void subscrubeToResponses() {
        LiveData<ReceivedResponse> liveData = Receiver.getInstance().getLiveData();
        liveData.observe(this, new Observer<ReceivedResponse>() {
            @Override
            public void onChanged(@Nullable ReceivedResponse responce) {
                if (responce != null) {
                    onBalanceReceived(responce);
                }
            }
        });
    }

    private <T> void onBalanceReceived(ReceivedResponse responce) {
        if (responce.getSenderFragment() != null && responce.getSenderFragment().equals(this.getClass().getSimpleName()) && responce.getData() != null) {


            if (responce.getData().getClass().equals(AcquiringBank.HasResponded.class)) {
                Log.e("HasResponded", "HasResponded");


                showFragment(WebViewFragment.newInstance((AcquiringBank.HasResponded) responce.getData()));
                Receiver.getInstance().clearLiveData();


                return;
            }



            if (responce.getData().getClass().equals(Error.class)) {
                Log.e("Error", "Error");
               /* progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
                Error error = (Error) responce.getData();
                errorText.setText("Ошибка:"+ error.getMessage());*/
                return;
            }

            Log.e("unknownDataAreasFr", responce.getData().getClass().toString());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter:
                sendReq();
                break;
        }
    }


    private void sendReq() {
        Sender.getInstance().send("acquiringBank.replenishDriverAccount", new AcquiringBank.ReplenishDriverAccount(Double.parseDouble(editText.getText().toString())).toJson(), this.getClass().getSimpleName());
    }


    @Override
    public void onPause() {
        super.onPause();
        EarnActionBarFragment.setBackgroundTransparent(false);
    }


    private void showFragment(Fragment fragment){
        replaceCurrentFragmentWith(getFragmentManager(), getParentContainer(this.getView()), fragment, true);
    }
}