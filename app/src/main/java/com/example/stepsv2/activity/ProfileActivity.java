package com.example.stepsv2.activity;

import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.stepsv2.R;
import com.example.stepsv2.login_register.LoginActivity;
import com.example.stepsv2.login_register.SQLiteHandler;
import com.example.stepsv2.login_register.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button btnLogout = findViewById(R.id.btnLogout);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
