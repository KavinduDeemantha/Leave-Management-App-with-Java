package com.s22010068.leave_management_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.widget.Button;


public class FirstPageActivity extends AppCompatActivity implements LightSensorManager.LightSensorCallback {
    private LightSensorManager lightSensorManager;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstpage);

        sessionManager = new SessionManager(this);

//        boolean isLoggedIn = checkUserLoginStatus();
//        if (!isLoggedIn || isLoggedIn) {
//            Intent intent = new Intent(this, FirstPageActivity.class);
//            startActivity(intent);
//            finish();
//        }

        lightSensorManager = new LightSensorManager(this, this);

        Button clickToLogin = findViewById(R.id.clickToLogin);
        clickToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstPageActivity.this, LoginPageActivity.class);
                startActivity(intent);
            }
        });
    }

//    private boolean checkUserLoginStatus() {
//        return sessionManager.isLoggedIn();
//    }

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
            findViewById(R.id.firstPageLayout).setBackgroundColor(Color.BLACK);
        } else {
            findViewById(R.id.firstPageLayout).setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
        }
    }
}
