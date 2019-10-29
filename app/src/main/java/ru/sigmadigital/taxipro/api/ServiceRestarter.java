package ru.sigmadigital.taxipro.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ServiceRestarter extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            Log.e("Broadcast Listened", "Service tried to stop");
            this.context = context;
            handler.sendEmptyMessageDelayed(1, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                try {
                    Log.e("Broadcast Listened", "Service tried to start");
                    TaxiProService.startTaxiProService(context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    });

}
