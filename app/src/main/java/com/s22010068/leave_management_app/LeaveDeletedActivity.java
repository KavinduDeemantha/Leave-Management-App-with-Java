package com.s22010068.leave_management_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LeaveDeletedActivity extends AppCompatActivity implements LightSensorManager.LightSensorCallback {
    private LightSensorManager lightSensorManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leavedeleted);

        lightSensorManager = new LightSensorManager(this, this);

        Button dashboardBtn = findViewById(R.id.dashboard_btn);
        dashboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dashboardIntent = new Intent(LeaveDeletedActivity.this, DashboardActivity.class);
                startActivity(dashboardIntent);
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
        TextView leave_deleted = findViewById(R.id.leave_deleted);
        if (lux < 1000) {
            findViewById(R.id.leaveDeletedPageLayout).setBackgroundColor(Color.BLACK);
            leave_deleted.setTextColor(Color.WHITE);
        } else {
            findViewById(R.id.leaveDeletedPageLayout).setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
        }
    }
}
