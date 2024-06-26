package com.s22010068.leave_management_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class DashboardActivity extends AppCompatActivity implements LightSensorManager.LightSensorCallback {
    private LightSensorManager lightSensorManager;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        initializeLeaveCount();

        lightSensorManager = new LightSensorManager(this, this);
        sessionManager = new SessionManager(this);

        ImageButton addLeaveBtn = findViewById(R.id.add_leave_btn);
        Button logOutBtn = findViewById(R.id.logOutBtn);
        addLeaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addLeaveIntent = new Intent(DashboardActivity.this, RequestLeaveActivity.class);
                startActivity(addLeaveIntent);
            }
        });
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logoutUser();
                Intent logOutIntent = new Intent(DashboardActivity.this, FirstPageActivity.class);
                startActivity(logOutIntent);
                finish();
            }
        });

        updateLeaveCount();
    }
    protected void onResume() {
        super.onResume();
        lightSensorManager.start();
        updateLeaveCount();
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
        TextView leave_count = findViewById(R.id.leaveCount);
        if (lux < 1000) {
            findViewById(R.id.dashboardPageLayout).setBackgroundColor(Color.BLACK);
            leave_count.setTextColor(Color.WHITE);
        } else {
            findViewById(R.id.dashboardPageLayout).setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
        }
    }
    private void updateLeaveCount() {
        SharedPreferences sharedPreferences = getSharedPreferences("LeavePrefs", MODE_PRIVATE);
        int availableLeaves = sharedPreferences.getInt("leaveCount", 0); // Default to 0 if not set
        TextView leaveCountTextView = findViewById(R.id.leaveCount);
        leaveCountTextView.setText(String.valueOf(availableLeaves));
    }

    private void initializeLeaveCount() {
        SharedPreferences sharedPreferences = getSharedPreferences("LeavePrefs", MODE_PRIVATE);
        if (!sharedPreferences.contains("leaveCount")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("leaveCount", 10); // Maximum leaves user can take is 10
            editor.apply();
        }
    }
}
