package ru.sigmadigital.taxipro.activities;

import android.os.Bundle;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.fragments.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        loadFragment(SettingsFragment.newInstance(), "dhrdh", false);
    }

    @Override
    protected int getFragmentContainer() {
        return R.id.settings_container;
    }
}
