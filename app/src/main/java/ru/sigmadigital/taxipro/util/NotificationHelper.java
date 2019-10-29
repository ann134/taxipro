package ru.sigmadigital.taxipro.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.activities.MainActivity;
import ru.sigmadigital.taxipro.activities.OrderActivity;
import ru.sigmadigital.taxipro.models.Order;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class NotificationHelper {


    /*public void notification(){

        Intent notificationIntent = new Intent(App.getAppContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getValue(App.getAppContext(),
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

       // Resources res = this.getResources();

        // до версии Android 8.0 API 26
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(contentIntent)
                // обязательные настройки
                .setSmallIcon(R.drawable.ic_launcher_cat)
                //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle("Напоминание")
                //.setContentText(res.getString(R.string.notifytext))
                .setContentText("Пора покормить кота") // Текст уведомления
                // необязательные настройки
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.hungrycat)) // большая
                // картинка
                //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                .setTicker("Последнее китайское предупреждение!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true); // автоматически закрыть уведомление после нажатия

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Альтернативный вариант
        // NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFY_ID, builder.build());

    }*/


    public static void createSimpleNotification(String title) {

       /* Intent notificationIntent = new Intent(App.getAppContext());
        //notificationIntent.putExtra("order",  order);

        PendingIntent contentIntent = PendingIntent.getActivity(App.getAppContext(),
                0,
                PendingIntent.FLAG_CANCEL_CURRENT);

         .setContentIntent(contentIntent)*/

        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getAppContext(), "notifycation_channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText("___")


                .setAutoCancel(true);

        Notification notification = builder.build();


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(App.getAppContext());

        /* NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);*/
        notificationManager.notify(2, notification);

    }


    public static void createDriverNotification(Order.DriverOrder order) {

        Intent notificationIntent = new Intent(App.getAppContext(), OrderActivity.class);
        notificationIntent.putExtra("order", order);

        PendingIntent contentIntent = PendingIntent.getActivity(App.getAppContext(),
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getAppContext(), "notifycation_channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Order")
                .setContentText(order.getRouteItems().get(0).getTitle())

                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        Notification notification = builder.build();


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(App.getAppContext());

        /* NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);*/
        notificationManager.notify(1, notification);

    }


}
