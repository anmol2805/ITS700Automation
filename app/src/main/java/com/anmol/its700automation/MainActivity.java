package com.anmol.its700automation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth = FirebaseAuth.getInstance();
    EditText inputEmail,inputPassword;
    ProgressBar progressBar;
    Button btnLogin,btnReset;
    String sid,email,password;
    private static long back_pressed;
    String crypt = "https://us-central1-iiitcloud-e9d6b.cloudfunctions.net/cryptr?pass=";
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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sid = inputEmail.getText().toString();
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
}
