package com.anmol.its700automation.Services;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.anmol.its700automation.HomeActivity;
import com.anmol.its700automation.R;
import com.firebase.jobdispatcher.JobParameters;

/**
 * Created by anmol on 1/26/2018.
 */

public class ConnectivityService extends com.firebase.jobdispatcher.JobService {
    public static int FOREGROUND_SERVICE = 101;


    @Override
    public boolean onStartJob(JobParameters job) {
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.infinity);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Truiton Music Player")
                .setTicker("Truiton Music Player")
                .setContentText("My Music")
                .setSmallIcon(R.drawable.infinity)
                .setLargeIcon(
                        Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        startForeground(FOREGROUND_SERVICE,
                notification);


        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }
}
