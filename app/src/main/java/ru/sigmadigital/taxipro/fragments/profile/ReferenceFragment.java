package ru.sigmadigital.taxipro.fragments.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;


public class ReferenceFragment extends BaseNavigatorFragment implements View.OnClickListener {

    private ActionBarListener actionBarListener;

    public static ReferenceFragment newInstance(ActionBarListener listener) {
        ReferenceFragment fragment = new ReferenceFragment();
        fragment.setActionBarListener(listener);
        return fragment;
    }

    private void setActionBarListener(ActionBarListener actionBarListener) {
        this.actionBarListener = actionBarListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.fragment_reference, null);

        if (actionBarListener != null) {
            actionBarListener.onSetTitle("справка");
            actionBarListener.onShowBackButton(true);
        }

        TextView title1 = v.findViewById(R.id.title1);
        TextView title2 = v.findViewById(R.id.title2);

        TextView text1 = v.findViewById(R.id.text1);
        TextView text2 = v.findViewById(R.id.text2);

        title1.setText("авторизация");
        title2.setText("регистрация");

        text1.setText("для того чтобы начать работу в приложении необходимо зарегестрироваться или авторизоваться по вашему номеру телефона и так далее");
        text2.setText("для того чтобы начать работу в приложении необходимо зарегестрироваться или авторизоваться по вашему номеру телефона и так далее");

        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call_name: {

                break;
            }
        }
    }

}
