package ru.sigmadigital.taxipro.activities;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.api.TaxiProService;
import ru.sigmadigital.taxipro.fragments.login.LoginFragment;

public class LoginActivity extends BaseActivity {

    private Toolbar toolbar;
    private ConstraintLayout yellowToolbar;
    TextView tittleToolbar;
    private OnKeyboardWatcher watcher;

    private boolean nextActivityExpected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TaxiProService.stopSendMyLocation();

        toolbar = findViewById(R.id.my_action_bar);
        ImageView btnBackLayout = findViewById(R.id.back_button);
        setSupportActionBar(toolbar);
        btnBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        yellowToolbar = findViewById(R.id.second_action_bar);
        ImageView btnBackSecondLayout = findViewById(R.id.imv_back_button);
        tittleToolbar = findViewById(R.id.tv_title);
        //setSupportActionBar(yellowToolbar);
        btnBackSecondLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        yellowToolbar.setVisibility(View.GONE);

        attachKeyboardListeners();

        loadFragment(LoginFragment.newInstance(LoginFragment.TYPE_LOGIN), getResources().getString(R.string.app_name), true);
    }

    public void hideToolbar(){
        toolbar.setVisibility(View.INVISIBLE);
    }

    public void showToolbar(){
        toolbar.setVisibility(View.VISIBLE);
    }

    public void hideBackBtnToolbar(){
        ImageView btnBackLayout = findViewById(R.id.back_button);
        btnBackLayout.setVisibility(View.INVISIBLE);
    }
    public void showBackBtnToolbar(){
        ImageView btnBackLayout = findViewById(R.id.back_button);
        btnBackLayout.setVisibility(View.VISIBLE);
    }

    public void showSecondToolbar(){
        //toolbar.setVisibility(View.GONE);
        yellowToolbar.setVisibility(View.VISIBLE);
    }

    public void setTittleToolbar(String text){
        tittleToolbar.setText(text);
    }


    @Override
    protected int getFragmentContainer() {
        return R.id.fragment_container;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(getSupportFragmentManager().getFragments().size() < 1){
//            finish();
//        }

    }

    @Override
    protected void onShowKeyboard(int keyboardHeight) {
        if(watcher != null){
            watcher.keyboardOpen();
        }
    }

    @Override
    protected void onHideKeyboard() {
        if(watcher != null){
            watcher.keyboardClose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* if(!nextActivityExpected){
            TaxiProService.stopTaxiProService(App.getAppContext());
        }*/
    }

    public void setNextActivityExpected(boolean nextActivityExpected) {
        this.nextActivityExpected = nextActivityExpected;
    }

    public OnKeyboardWatcher getWatcher() {
        return watcher;
    }

    public void setWatcher(OnKeyboardWatcher watcher) {
        this.watcher = watcher;
    }

    public interface OnKeyboardWatcher{
        void keyboardOpen();
        void keyboardClose();
    }
}
