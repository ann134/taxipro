package ru.sigmadigital.taxipro.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;
import ru.sigmadigital.taxipro.view.TextWithIconView;

public class HelpFragment extends BaseNavigatorFragment implements View.OnClickListener {

    private ActionBarListener actionBarListener;

    public static HelpFragment newInstance(ActionBarListener listener) {
        HelpFragment fragment = new HelpFragment();
        fragment.setActionBarListener(listener);
        return fragment;
    }


    private void setActionBarListener(ActionBarListener actionBarListener) {
        this.actionBarListener = actionBarListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_help, container, false);

        if (actionBarListener != null) {
            actionBarListener.onSetTitle("помощь");
            actionBarListener.onShowBackButton(true);
        }

        TextWithIconView reference = v.findViewById(R.id.reference);
        reference.setTitle("Справка");
        reference.setIcon(R.drawable.ic_help);
        reference.setArrow();
        reference.setOnClickListener(this);

        TextWithIconView login = v.findViewById(R.id.login);
        login.setTitle("Авторизация");
        login.setIcon(R.drawable.ic_login);
        login.setArrow();
        login.setOnClickListener(this);

        TextWithIconView account = v.findViewById(R.id.account);
        account.setTitle("Аккаунт и платежи");
        account.setIcon(R.drawable.ic_bank_card);
        account.setArrow();
        account.setOnClickListener(this);

        TextWithIconView errors = v.findViewById(R.id.errors);
        errors.setTitle("Ошибки приложения");
        errors.setIcon(R.drawable.ic_error_small);
        errors.setArrow();
        errors.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reference: {

                break;
            }
            case R.id.login: {

                break;
            }
            case R.id.account: {

                break;
            }
            case R.id.errors: {

                break;
            }
        }
    }

}
