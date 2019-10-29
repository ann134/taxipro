package ru.sigmadigital.taxipro.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.api.MyLocationListener;
import ru.sigmadigital.taxipro.api.TaxiProService;
import ru.sigmadigital.taxipro.util.SettingsHelper;


public class SplashActivity extends BaseActivity implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_CODE = 960;
    private static final int SETTINGS_REQUEST_CODE = 960;

    TextView textWarning;
    TextView settings;
    TextView locationWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textWarning = findViewById(R.id.text_permissions);
        settings = findViewById(R.id.text_link);
        locationWarning = findViewById(R.id.text_location);
        settings.setOnClickListener(this);

        requestPermissionsIfNeed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", "onResume");
    }

    public void requestPermissionsIfNeed() {
        if (checkAllPermissions()){
            checkGps();
        } else {
            requestPermissionS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_CODE){
            Log.e("onRequestPermissions", "onRequestPermissions");
            permissionsResult();
        }
    }

    public void permissionsResult() {
        Log.e("checkAllPermiionsResult", "checkAllPermissionsResult");

        if (checkAllPermissions()) {
            checkGps();
        } else {
            showWarningPermissions();
        }
    }


    private void checkGps(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TaxiProService.startTaxiProService(App.getAppContext());
            }
        }, 1000);


        MyLocationListener.getInstance();

        final LiveData<Boolean> liveDataProvider = MyLocationListener.getInstance().getProviderEnabledLiveData();
        liveDataProvider.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean enabled) {
                if (enabled) {
                    start();
                } else {
                    showWarningLocation();
                }
            }
        });

    }

    private void start() {
        textWarning.setVisibility(View.GONE);
        settings.setVisibility(View.GONE);
        locationWarning.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SettingsHelper.getToken() != null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 1000);
    }

    public void showWarningPermissions() {
        textWarning.setVisibility(View.VISIBLE);
        settings.setVisibility(View.VISIBLE);
    }

    public void showWarningLocation() {
        textWarning.setVisibility(View.GONE);
        settings.setVisibility(View.GONE);
        locationWarning.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        goToSettings();
    }

    private void goToSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + App.getAppContext().getPackageName()));
        startActivityForResult(intent, SETTINGS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_REQUEST_CODE) {
            permissionsResult();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public boolean checkAllPermissions() {
        return isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) &&
                isPermissionGranted(Manifest.permission.CAMERA) &&
                isPermissionGranted(Manifest.permission.CALL_PHONE) &&
                isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(App.getAppContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissionS() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        }, PERMISSIONS_REQUEST_CODE);
    }
}



