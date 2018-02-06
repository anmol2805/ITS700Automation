package com.anmol.its700automation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.anmol.its700automation.Services.ForegroundService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final String jobtag = "tag";
    TextView textView;
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
            textView = (TextView)findViewById(R.id.textView);
//            URLConnection con = null;
//            try {
//                con = new URL( "http://14.139.198.171/api/hibi" ).openConnection();
//                System.out.println( "orignal url: " + con.getURL() );
//                con.connect();
//                System.out.println( "connected url: " + con.getURL() );
//                InputStream is = con.getInputStream();
//                System.out.println( "redirected url: " + con.getURL() );
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            request request = new request(){

                @Override
                protected void onPostExecute(URL url) {
                    super.onPostExecute(url);
                    String reurl = String.valueOf(url);
                    textView.setText(reurl);
                }
            };
            request.execute();
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
//                    .setConstraints(Constraint.ON_ANY_NETWORK)
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
    public static class request extends AsyncTask<Void, Void, URL> {

        @Override
        protected URL doInBackground(Void... voids) {
            URLConnection con = null;
            try {
                con = new URL( "http://14.139.198.171/api/hibi" ).openConnection();
                System.out.println( "orignal url: " + con.getURL() );
                con.connect();
                System.out.println( "connected url: " + con.getURL() );
                InputStream is = con.getInputStream();
                System.out.println( "redirected url: " + con.getURL() );
                is.close();
                return con.getURL();
            } catch (IOException e) {
                e.printStackTrace();


            }
            return null;
        }
    }

}

