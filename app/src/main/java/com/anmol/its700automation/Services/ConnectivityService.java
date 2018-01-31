package com.anmol.its700automation.Services;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

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
        Toast.makeText(getApplicationContext(),"Job started",Toast.LENGTH_SHORT).show();
        new Notificatontask().execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    public class Notificatontask extends AsyncTask<Void,Void,Void>{




        @Override
        protected Void doInBackground(Void... voids) {
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

            return null;
        }


    }

}
