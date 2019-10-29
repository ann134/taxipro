package ru.sigmadigital.taxipro.fragments.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;

public class ProfileNavigatorFragment extends BaseNavigatorFragment implements View.OnClickListener{

    private TextView actionBarTitle;
    private ImageView backButton;
    private ImageView trashButton;


    private ActionBarListener listener = new ActionBarListener() {
        @Override
        public void onSetTitle(String title) {
            setTitle(title);
        }
        @Override
        public void onShowBackButton(boolean show) {
            showBackButton(show);
        }
        @Override
        public void onShowTrashButton(boolean show) {
            showTrashButton(show);
        }
    };


    public static ProfileNavigatorFragment newInstance() {
        return new ProfileNavigatorFragment();
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.navigator_profile, null);

        actionBarTitle = v.findViewById(R.id.tv_title);
        backButton = v.findViewById(R.id.imv_back_button);
        trashButton = v.findViewById(R.id.imv_trash);
        backButton.setOnClickListener(this);
        trashButton.setOnClickListener(this);


        replaceCurrentFragmentWith(getFragmentManager(), R.id.profile_container, ProfileFragment.newInstance(listener), false);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back_button: {
                if (getActivity() != null)
                    getActivity().onBackPressed();
                break;
            }
            case R.id.imv_trash: {

                break;
            }
        }
    }



    private void setTitle(String title){
        actionBarTitle.setText(title);
    }

    private void showBackButton(boolean show){
        if (show) {
            backButton.setVisibility(View.VISIBLE);
        } else {
            backButton.setVisibility(View.GONE);
        }
    }

    private void showTrashButton(boolean show){
        if (show) {
            trashButton.setVisibility(View.VISIBLE);
        } else {
            trashButton.setVisibility(View.GONE);
        }
    }




    /*@Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("listener", listener);
    }*/

}
