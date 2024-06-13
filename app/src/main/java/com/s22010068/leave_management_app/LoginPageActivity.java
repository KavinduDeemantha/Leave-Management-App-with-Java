package com.s22010068.leave_management_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LoginPageActivity extends AppCompatActivity implements LightSensorManager.LightSensorCallback {
    private LightSensorManager lightSensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        lightSensorManager = new LightSensorManager(this, this);

        Button signInBtn = findViewById(R.id.signInButton);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(LoginPageActivity.this, SignInPageActivity.class);
                startActivity(signInIntent);
            }
        });
        Button signUpBtn = findViewById(R.id.signUpButton);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(LoginPageActivity.this, SignUpPageActivity.class);
                startActivity(signUpIntent);
            }
        });
    }
    protected void onResume() {
        super.onResume();
        lightSensorManager.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lightSensorManager.stop();
    }
    @Override
    public void onLightSensorChanged(float lux) {
        adjustBrightness(lux);
    }
    private void adjustBrightness(float lux) {
        if (lux < 1000) {
            findViewById(R.id.loginPageLayout).setBackgroundColor(Color.BLACK);
        } else {
            findViewById(R.id.loginPageLayout).setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
        }
    }
}