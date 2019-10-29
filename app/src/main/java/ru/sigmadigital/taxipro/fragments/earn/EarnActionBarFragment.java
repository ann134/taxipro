package ru.sigmadigital.taxipro.fragments.earn;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.fragments.BaseNavigatorFragment;

public class EarnActionBarFragment extends BaseNavigatorFragment {

    private static ConstraintLayout actionBarContainer;
    private static TextView actionBarTitle;
    private static ImageView backButton;


    public static EarnActionBarFragment newInstance() {
        return new EarnActionBarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_income_action_bar, null);

        actionBarContainer = v.findViewById(R.id.ab_income);
        actionBarTitle = v.findViewById(R.id.tv_title);
        backButton = v.findViewById(R.id.imv_back_button);
        backButton.setVisibility(View.GONE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        replaceCurrentFragmentWith(getFragmentManager(), R.id.fragments_income_container, EarnFragment.newInstance(), false);

        return v;
    }


    static void setActionBarTitle(String title) {
        actionBarTitle.setText(title);
    }

    static void setBackButtonVisible(boolean visible) {
        if (visible) {
            backButton.setVisibility(View.VISIBLE);
        } else {
            backButton.setVisibility(View.GONE);
        }
    }

    static void setBackgroundTransparent(boolean transparent) {
        if (transparent) {
            actionBarContainer.setBackgroundColor(App.getAppContext().getResources().getColor(R.color.transparent));
            actionBarTitle.setTextColor(App.getAppContext().getResources().getColor(R.color.white));
            setBackButtonColor(R.color.white);
        } else {
            actionBarContainer.setBackgroundColor(App.getAppContext().getResources().getColor(R.color.colorAccent));
            actionBarTitle.setTextColor(App.getAppContext().getResources().getColor(R.color.textGrayDark));
            setBackButtonColor(R.color.textGrayDark);
        }
    }

    private static void setBackButtonColor(int color) {
        Drawable d = App.getAppContext().getResources().getDrawable(R.drawable.ic_back);
        d.setColorFilter(App.getAppContext().getResources().getColor(color), PorterDuff.Mode.SRC_IN);
        backButton.setImageDrawable(d);
    }

}
