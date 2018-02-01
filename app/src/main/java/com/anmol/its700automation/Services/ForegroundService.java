package com.anmol.its700automation.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.anmol.its700automation.HomeActivity;
import com.anmol.its700automation.R;

/**
 * Created by anmol on 2/1/2018.
 */

public class ForegroundService extends Service {
    public static int FOREGROUND_SERVICE = 101;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ConnectivityManager cm = (ConnectivityManager)getBaseContext().getSystemService(getApplication().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
            while(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                Intent notificationIntent = new Intent(getApplicationContext(),HomeActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        notificationIntent, 0);
                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.ic_launcher);
                Notification notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Canopy Developers")
                        .setTicker("Canopy Developers")
                        .setContentText("This is ITS Automation!!!")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(
                                Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .build();
                startForeground(FOREGROUND_SERVICE,
                        notification);

            }


        }
    }
}
