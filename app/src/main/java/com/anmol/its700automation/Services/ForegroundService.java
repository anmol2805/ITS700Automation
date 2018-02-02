package com.anmol.its700automation.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.ConstraintAnchor;

import com.anmol.its700automation.HomeActivity;
import com.anmol.its700automation.R;
import com.google.android.gms.drive.TransferPreferences;

/**
 * Created by anmol on 2/1/2018.
 */

public class ForegroundService extends Service {
    public static int FOREGROUND_SERVICE = 101;
    ConnectivityManager connectivityManager;
    ConnectivityManager.NetworkCallback networkCallback;
    BroadcastReceiver connectivityChange;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), networkCallback = new ConnectivityManager.NetworkCallback(){
                // -Snip-
            });
        }else{
            registerReceiver(connectivityChange = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    handleConnectivityChange(!intent.hasExtra("noConnectivity"), intent.getIntExtra("networkType", -1));
                }
            }, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork == null) {

        }else{
            Intent notificationIntent = new Intent(getApplicationContext(),HomeActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                    notificationIntent, 0);
            final Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.mipmap.ic_launcher);
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Canopy Developers")
                    .setContentText("ITS Automation")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();
            startForeground(FOREGROUND_SERVICE,
                    notification);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();





    }
    private void handleConnectivityChange(NetworkInfo networkInfo){
        // Calls handleConnectivityChange(boolean connected, int type)
    }

    private void handleConnectivityChange(boolean connected, int type){
        // Calls handleConnectivityChange(boolean connected, ConnectionType connectionType)
    }

    private void handleConnectivityChange(boolean connected, ConstraintAnchor.ConnectionType connectionType){
        // Logic based on the new connection
    }
    private enum ConnectionType{
        MOBILE,WIFI,VPN,OTHER;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(networkCallback != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)connectivityManager.unregisterNetworkCallback(networkCallback);
        else if(connectivityChange != null)unregisterReceiver(connectivityChange);
    }
}
