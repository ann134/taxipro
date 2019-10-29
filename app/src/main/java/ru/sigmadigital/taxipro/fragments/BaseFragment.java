package ru.sigmadigital.taxipro.fragments;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

@SuppressLint("ValidFragment")
public abstract class BaseFragment extends Fragment {

    protected void loadFragment(Fragment fragment, String title, boolean stack) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.replace(getFragmentContainer(),
                    fragment);
            if (stack) fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();
        }
    }

    protected void loadFragment(Fragment fragment, String title, boolean stack, int fragmentContainer) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.replace(fragmentContainer,
                    fragment);
            if (stack) fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();
        }
    }

    protected abstract int getFragmentContainer();
}
