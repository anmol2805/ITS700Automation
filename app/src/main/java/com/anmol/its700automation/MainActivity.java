package com.anmol.its700automation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth = FirebaseAuth.getInstance();
    EditText inputEmail,inputPassword;
    ProgressBar progressBar;
    Button btnLogin,btnReset;
    String sid,email,password;
    private static long back_pressed;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Switch rc,ac,en;
    String crypt = "https://us-central1-iiitcloud-e9d6b.cloudfunctions.net/cryptr?pass=";
    Boolean autoconnect = false,remainconnect = false,enablenotif = false;
    Logout logout;
    String magictoken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        inputEmail = (EditText) findViewById(R.id.userid);
        inputPassword = (EditText) findViewById(R.id.passwd);
        progressBar = (ProgressBar) findViewById(R.id.prgbr);
        progressBar.setVisibility(View.GONE);
        btnLogin = (Button) findViewById(R.id.save);
        rc = (Switch)findViewById(R.id.remainconnected);
        ac = (Switch)findViewById(R.id.autoconnect);
        en = (Switch)findViewById(R.id.enblenotif);
        if(!isWifiConnected()){
            Toast.makeText(this,"Please connect to Wifi!!!",Toast.LENGTH_LONG).show();
        }
        logout = new Logout(){
            @Override
            protected void onPostExecute(URL url) {
                super.onPostExecute(url);
                String reurl = String.valueOf(url);

            }
        };
        logout.execute();
        rc.setChecked(false);
        ac.setChecked(false);
        en.setChecked(false);
        rc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    remainconnect = true;
                }
                else{
                    remainconnect = false;
                }
            }
        });
        ac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    autoconnect = true;
                }
                else{
                    autoconnect = false;
                }
            }
        });
        en.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    enablenotif = true;
                }
                else{
                    enablenotif = false;
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWifiConnected()){
                    logout.execute();
                    if(isMagicTokenAccessible()){

                    }

                }
                else{
                    Toast.makeText(MainActivity.this,"Please connect to ITS!!!",Toast.LENGTH_LONG).show();
                }
                sid = inputEmail.getText().toString();
                sid = sid.toLowerCase();
                email = sid + "@iiit-bh.ac.in";
                password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter login ID!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.INVISIBLE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        //inputPassword.setError(getString(R.string.minimum_password));
                                    } else {

                                        auth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        // If sign in fails, display a message to the user. If sign in succeeds
                                                        // the auth state listener will be notified and logic to handle the
                                                        // signed in user can be handled in the listener.
                                                        if (!task.isSuccessful()) {
                                                            Toast.makeText(MainActivity.this, "Authentication failed.Check your Network Connection",
                                                                    Toast.LENGTH_SHORT).show();
                                                        } else {

                                                            try {
                                                                String encode  = URLEncoder.encode(password,"UTF-8");
                                                                StringRequest str = new StringRequest(Request.Method.POST, crypt + encode, new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        Map<String,Object> map = new HashMap<>();
                                                                        map.put("sid",sid);
                                                                        map.put("pass",response);
                                                                        map.put("enablenotif",enablenotif);
                                                                        map.put("remainconnect",remainconnect);
                                                                        map.put("autoconnect",autoconnect);
                                                                        db.collection("users").document(auth.getCurrentUser().getUid())
                                                                                .set(map);

                                                                    }
                                                                }, new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {

                                                                    }
                                                                });
                                                                Mysingleton.getInstance(MainActivity.this).addToRequestqueue(str);

                                                            } catch (UnsupportedEncodingException e) {
                                                                e.printStackTrace();
                                                            }


                                                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                            //overridePendingTransition(R.anim.still,R.anim.slide_in_up);
                                                        }
                                                    }
                                                });
                                    }
                                } else {


                                    try {
                                        String encode = URLEncoder.encode(password,"UTF-8");

                                        StringRequest str = new StringRequest(Request.Method.POST, crypt + encode, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Map<String,Object> map = new HashMap<>();
                                                map.put("sid",sid);
                                                map.put("pass",response);
                                                map.put("enablenotif",enablenotif);
                                                map.put("remainconnect",remainconnect);
                                                map.put("autoconnect",autoconnect);
                                                db.collection("users").document(auth.getCurrentUser().getUid())
                                                        .set(map);

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        });
                                        Mysingleton.getInstance(MainActivity.this).addToRequestqueue(str);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }



                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                    //overridePendingTransition(R.anim.still,R.anim.slide_in_up);




                                }
                            }
                        });
            }
        });


    }
    @Override
    public void onBackPressed() {
        if(back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            //overridePendingTransition(R.anim.still,R.anim.slide_out_down);
        }else {
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
    public static class Itsrequest extends AsyncTask<Void, Void, URL> {

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
    public static class Logout extends AsyncTask<Void,Void,URL>{
        @Override
        protected URL doInBackground(Void... voids) {
            URLConnection con = null;
            try {
                con = new URL( "http://172.16.1.11:1000/logout?060b0a0200050d26" ).openConnection();
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


    public Boolean isWifiConnected() {
        Boolean networkstatus = false;
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting ()) {
            networkstatus = true;
        } else if (mobile.isConnectedOrConnecting ()) {
            networkstatus = false;
        } else {
            networkstatus = false;
        }
        return networkstatus;
    }
    public Boolean isMagicTokenAccessible(){
        final Boolean[] tokenstatus = {false};
        Itsrequest itsrequest = new Itsrequest(){
            @Override
            protected void onPostExecute(URL url) {
                super.onPostExecute(url);
                String reurl = String.valueOf(url);
                if(reurl.contains("http://14.139.198.171/api/hibi")){
                    logout.execute();
                    Toast.makeText(MainActivity.this,"Please connect to ITS network",Toast.LENGTH_LONG).show();
                    tokenstatus[0] = false;
                }
                else{
                    magictoken = reurl.substring(reurl.lastIndexOf("?")+1);

                    tokenstatus[0] = true;
                }


            }
        };
        itsrequest.execute();
        return tokenstatus[0];
    }



}
