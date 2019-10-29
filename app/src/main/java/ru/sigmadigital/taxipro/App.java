package ru.sigmadigital.taxipro;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import ru.sigmadigital.taxipro.activities.BaseActivity;
import ru.sigmadigital.taxipro.util.SettingsHelper;

public class App extends Application {

    private static App context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static void showToast(int textId){
        Message ms = new Message();
        ms.what = 1;
        ms.arg1 = textId;
        context.handler.sendMessage(ms);
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                Toast.makeText(App.context, msg.arg1, Toast.LENGTH_LONG).show();
            }
            return false;
        }
    });

    public static void setTextSize(Context activityContext){
        int sizeType = SettingsHelper.getTextSize();

        float scale = 0;
        switch (sizeType){
            case 1:
                scale = 1.0f;
                break;
            case 2:
                scale = 1.15f;
                break;
            case 3:
                scale = 1.3f;
                break;
        }

        Resources res = activityContext.getResources();
        Configuration configuration = new Configuration(res.getConfiguration());
        configuration.fontScale = scale;
        res.updateConfiguration(configuration, res.getDisplayMetrics());
    }




    public static Context getAppContext() {
        return context;
    }

}
