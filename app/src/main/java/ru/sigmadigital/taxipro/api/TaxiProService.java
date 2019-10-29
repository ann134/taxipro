package ru.sigmadigital.taxipro.api;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.activities.MainActivity;
import ru.sigmadigital.taxipro.models.DriverPosition;
import ru.sigmadigital.taxipro.models.my.Header;


public class TaxiProService extends Service {

    //public static boolean isServiceRun = false;
    public static TaxiProService service;
    public static boolean needKill = false;


    public static void startTaxiProService(Context context) {
        Log.e("startTaxiProService", "startTaxiProService");
        Intent intent = new Intent(context, TaxiProService.class);
        context.startService(intent);
    }

    /*public static void stopTaxiProService(Context context) {
        Log.e("stopTaxiProService", "stopTaxiProService");
        needKill = true;
        Intent intent = new Intent(context, TaxiProService.class);
        intent.putExtra("killbyme", true);
        context.stopService(intent);
    }*/


    private WebSocketClient client;
    boolean canOpenConnection = true;

    private boolean run = true;
    private Thread sendTread;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("onCreate", "serviceOnCreate");

        service = this;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }






    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "notifycation_channel";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setSound(null, null);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
        startForeground(2, getPush());
    }


    private Notification getPush() {
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getValue(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);

        StringBuilder text = new StringBuilder();
        Error order = null;
        if (order != null) {
            /*@SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy mm:HH");
            try{
                *//*Date d = sdf.parse(order.getDate());
                if(d != null){
                    long time = d.getTime();
                    long t = System.currentTimeMillis() - time;
                    long h = t / 1000 / 60 / 60;
                    String hS = h > 9 ? h + "" :  "0" +h;
                    long m = (t - h * 1000 * 60 * 60) / 1000 / 60;
                    String mS = m > 9 ? m + "" : "0" + m;
                    text.append(getString(R.string.in_delivery));
                    text.append(": ").append(hS).append(":").append(mS).append("\n\n");*//*

                }
            }catch (Exception e){e.printStackTrace();}
            text.append(order.getAddress());*/
        } else {
            text.append(App.getAppContext().getString(R.string.serviseRun));
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "notifycation_channel");
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(App.getAppContext().getString(R.string.app_name))
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
//                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentText(text.toString())
                    .setOnlyAlertOnce(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(text.toString()))
                    .build();
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(App.getAppContext().getString(R.string.app_name))
                    .setCategory(Notification.CATEGORY_SERVICE)
//                    .setContentIntent(pendingIntent)
                    .setContentText(text.toString())
                    .setOnlyAlertOnce(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(text.toString()))
                    .build();
        } else {
            notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(App.getAppContext().getString(R.string.app_name))
//                    .setContentIntent(pendingIntent)
                    .setOnlyAlertOnce(true)
                    .setContentText(text.toString())
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(text.toString()))
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .build();
        }
        return notification;
    }







    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onStartCommand", "onStartCommand");

        openConnection();

        run = true;
        if (sendTread == null){
            startSenderTread();
        }


        //sendMyLocation();
        //isServiceRun = true;

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "serviceOnDestroy");

        if (client != null) {
            canOpenConnection = false;
            client.close();
            Log.e("client != null", "client.close()");
        }

        run = false;
        //isServiceRun = false;

        if (!needKill) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, ServiceRestarter.class);
            this.sendBroadcast(broadcastIntent);
        }
    }


    public static boolean isConnectionOpen = false;

    private void openConnection() {
        try {
            client = new WebSocketClient(new URI("ws://staging-api.taxipro.su/driver.api")) {


                @Override
                public void onMessage(String message) {
                    Log.d("onMessage", message);
                    Receiver.getInstance().parse(message);
                }

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.e("onOpen", "onOpen");
                    isConnectionOpen = true;

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.e("onClose", code + reason);
                    isConnectionOpen = false;
                    canOpenConnection = true;
                    if (canOpenConnection) {
                        openConnection();
                    }
                }

                @Override
                public void onError(Exception ex) {
                    Log.e("onError", ex.getMessage());
                    ex.printStackTrace();
                }
            };
            client.connect();
            //Log.e("clientCreated", "clientCreated");

        } catch (URISyntaxException ex) {
            Log.e("clientCreated", "URISyntaxException" + ex.getMessage());
            ex.printStackTrace();
        }
    }


    private void startSenderTread() {
        /*if(sendTread != null){
            sendTread.interrupt();
        }*/
         sendTread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    //Log.d("run", "run");
                    if (!Sender.getInstance().getQueueRequests().isEmpty()) {
                        if (service != null && client.isOpen()) {
                            //Log.e("try to send", Sender.getInstance().getQueueRequests().get(0)+"");
                            service.sendRequest(Sender.getInstance().getQueueRequests().get(0));
                            Sender.getInstance().removeFromQueueRequests(0);
                        } else {

                        }
                    }
                }
            }
        });
        sendTread.start();
        Log.e("startSenderTread", "startSenderTread");
    }

    public void sendRequest(String request) {
        Log.d("send", request);
        client.send(request);
    }





    //send location

    ScheduledFuture<?> scheduledFuture;

    public static void startSendMyLocation(){
        service.sendMyLocation();
    }

    public static void stopSendMyLocation(){
        if (service.scheduledFuture != null)
        service.scheduledFuture.cancel(true);
    }


    public void sendMyLocation() {
       scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                    Log.e("sendLocation", "sendLocation");

                    Header.Request headerRequest = Sender.getInstance().makeHeader("driverPosition.position");
                    DriverPosition.Position position = new DriverPosition.Position(MyLocationListener.getInstance().getLocation());

                    StringBuilder requestBilder = new StringBuilder();
                    requestBilder.append(headerRequest.toJson());
                    requestBilder.append("\n");
                    requestBilder.append(position.toJson());


                    if (service != null && client.isOpen()) {
                        //Log.e("try to send", Sender.getInstance().getQueueRequests().get(0)+"");
                        service.sendRequest(requestBilder.toString());
                    } else {

                    }
            }
        }, 0, 30, TimeUnit.SECONDS);

    }


}
