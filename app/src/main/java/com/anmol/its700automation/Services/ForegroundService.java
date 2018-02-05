package com.anmol.its700automation.Services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.v4.app.NotificationCompat;

import com.anmol.its700automation.HomeActivity;
import com.anmol.its700automation.R;
import com.google.android.gms.drive.TransferPreferences;

/**
 * Created by anmol on 2/1/2018.
 */

public class ForegroundService extends Service {
    public static int FOREGROUND_SERVICE = 101;
    String id = "xyz";
    String name = "xyz";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//            createChannel(notificationManager);
//            Notification.Builder builder = new Notification.Builder(this,id)
//                    .setContentTitle("this is notification")
//                    .setContentText("notification")
//                    .setAutoCancel(true);
//
//            Notification notification = builder.build();
//            startForeground(1, notification);
//
//        } else {
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                    .setContentTitle(getString(R.string.app_name))
//                    .setContentText("notification")
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setAutoCancel(true);
//
//            Notification notification = builder.build();
//
//            startForeground(1, notification);
//        }
//        return START_STICKY;
//    }


    @Override
    public void onCreate() {
        super.onCreate();
        Intent notificationIntent = new Intent(getApplicationContext(),HomeActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setChannelId(id)
                    .setContentTitle("Canopy Developers")
                    .setContentText("ITS Automation")
                    .setSmallIcon(R.drawable.ic_media_pause_light)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();
            startForeground(FOREGROUND_SERVICE,notification);
            //notificationManager.notify(FOREGROUND_SERVICE,notification);
            createChannel(notificationManager);

        }




    }
    @TargetApi(26)
    private void createChannel(NotificationManager notificationManager) {

        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel mChannel = new NotificationChannel(id,name,importance);
        //mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(mChannel);
    }

}
