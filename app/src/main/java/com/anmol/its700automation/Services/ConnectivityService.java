package com.anmol.its700automation.Services;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.anmol.its700automation.HomeActivity;
import com.anmol.its700automation.R;
import com.firebase.jobdispatcher.JobParameters;
import com.google.android.gms.drive.TransferPreferences;

/**
 * Created by anmol on 1/26/2018.
 */

public class ConnectivityService extends com.firebase.jobdispatcher.JobService {

    Backgroundtask backgroundtask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        Toast.makeText(getApplicationContext(),"Job started",Toast.LENGTH_SHORT).show();

        new Backgroundtask().execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }
    public class Backgroundtask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            int k = 60;
            while(k!=0){

                Toast.makeText(getApplicationContext(),String.valueOf(k),Toast.LENGTH_SHORT).show();
                k--;
            }
            return null;
        }
    }

}
