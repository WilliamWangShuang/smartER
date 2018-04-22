package com.example.william.starter_mobile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import smartER.Factories.LoginFactorial;
import smartER.webservice.SmartERUserWebservice;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvUsername = (TextView)findViewById(R.id.login_username);
                TextView tvPwd = (TextView)findViewById(R.id.password);
                LoginFactorial loginFactorial = new LoginFactorial(LoginActivity.this, tvUsername, tvPwd);
                loginFactorial.execute();
            }
        });
    }

}
