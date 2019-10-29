package ru.sigmadigital.taxipro.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
/*import android.util.Log;*/

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

  /*  private static Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
    }*/

    //old load fragments
    protected int getFragmentContainer() {
        return 0;
    }

    protected void loadFragment(Fragment fragment, String title, boolean stack) {
        loadFragment(fragment, title, stack, getFragmentContainer());
    }

    protected void loadFragment(Fragment fragment, String title, boolean stack, int container) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment);
        if (stack) fragmentTransaction.addToBackStack(title);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*//super.onBackPressed();
        *//*if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            this.finish();
        } else {*//*
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().executePendingTransactions();*/
    }



    //new replace fragments
    private static boolean isFragmentInBackstack(final FragmentManager fragmentManager, final String fragmentTagName) {
        for (int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++) {
            if (fragmentTagName.equals(fragmentManager.getBackStackEntryAt(entry).getClass().getSimpleName())) {
                return true;
            }
        }
        return false;
    }

    public void replaceCurrentFragmentWith(FragmentManager manager, int container, Fragment fragment, boolean addToBackStack) {
        final String tag = fragment.getClass().getSimpleName();
        //Log.e("isFragmentInBackstack", tag + "   " + isFragmentInBackstack(manager, tag));
        if (isFragmentInBackstack(manager, tag)) {
            try {
                manager.popBackStackImmediate(tag, 0);

            } catch (IllegalStateException ignored) {
                // There's no way to avoid getting this if saveInstanceState has already been called.
            }
        } else {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(container, fragment, tag);
            if (addToBackStack) transaction.addToBackStack(tag);
            transaction.commitAllowingStateLoss();
        }
    }


    /*public static Context getBaseActivityContext(){
        return context;

    }*/



    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
            int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(BaseActivity.this);

            if(heightDiff <= 300){
                onHideKeyboard();

                Intent intent = new Intent("KeyboardWillHide");
                broadcastManager.sendBroadcast(intent);
            } else {
                int keyboardHeight = heightDiff - contentViewTop;
                onShowKeyboard(keyboardHeight);

                Intent intent = new Intent("KeyboardWillShow");
                intent.putExtra("KeyboardHeight", keyboardHeight);
                broadcastManager.sendBroadcast(intent);
            }
        }
    };

    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;

    protected void onShowKeyboard(int keyboardHeight) {}
    protected void onHideKeyboard() {}

    protected void attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return;
        }

        rootLayout = (ViewGroup) findViewById(R.id.fl_root);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);

        keyboardListenersAttached = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (keyboardListenersAttached) {
            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
        }
    }
}


