package com.anmol.its700automation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth = FirebaseAuth.getInstance();
    EditText inputEmail,inputPassword;
    ProgressBar progressBar;
    Button btnLogin,btnReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        inputEmail = (EditText) findViewById(R.id.userid);
        inputPassword = (EditText) findViewById(R.id.passwd);
        progressBar = (ProgressBar) findViewById(R.id.prgbr);

        btnLogin = (Button) findViewById(R.id.save);

        //btnReset = (Button) findViewById(R.id.btn_reset_password);



        //Get Firebase auth instance


    }
}
