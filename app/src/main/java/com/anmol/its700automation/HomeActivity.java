package com.anmol.its700automation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.anmol.its700automation.Services.ConnectivityService;
import com.anmol.its700automation.Services.ForegroundService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final String jobtag = "tag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(auth.getCurrentUser()==null){
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
//        else if(!auth.getCurrentUser().isEmailVerified()){
//            showalert();
//        }
        else {
            setContentView(R.layout.activity_home);
            Intent intent = new Intent(getApplicationContext(),ForegroundService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            }
            else {
                startService(intent);
            }

//            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(HomeActivity.this));
//            Job job = dispatcher.newJobBuilder()
//                    .setService(ConnectivityService.class)
//                    .setLifetime(Lifetime.FOREVER)
//                    .setTag(jobtag)
//                    .setReplaceCurrent(true)
//                    .setConstraints(Constraint.ON_UNMETERED_NETWORK)
//                    .build();
//            dispatcher.mustSchedule(job);
        }
    }

    private void showalert() {
        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle("Verify your E-mail");
                    alertDialog.setMessage("We have sent you a verification email at " +auth.getCurrentUser().getEmail());
                    //alertDialog.setIcon(R.drawable.admin);
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Already Verified", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(auth.getCurrentUser().isEmailVerified()){
                                alertDialog.dismiss();
                            }
                            else {
                                showalert();
                            }
                        }
                    });
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Resend E-mail", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showalert();
                            Toast.makeText(HomeActivity.this,"We have sent you a verification email at " +auth.getCurrentUser().getEmail(),Toast.LENGTH_LONG).show();

                        }
                    });
                    alertDialog.show();
                }
                else{
                    Toast.makeText(HomeActivity.this,"Failed to send Verification link", Toast.LENGTH_LONG).show();

                }
            }
        });
    }


}
