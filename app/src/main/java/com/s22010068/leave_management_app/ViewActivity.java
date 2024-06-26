package com.s22010068.leave_management_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ViewActivity extends AppCompatActivity implements LightSensorManager.LightSensorCallback {
    TextView leaveNoTextView, leaveDateTextView, reasonTextView;
    private LightSensorManager lightSensorManager;
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.viewleave);

            leaveNoTextView = findViewById(R.id.leaveNoText);
            leaveDateTextView = findViewById(R.id.leaveDateText);
            reasonTextView = findViewById(R.id.leaveReasonText);

            String leaveNo = getIntent().getStringExtra("leaveNo");
            String leaveDate = getIntent().getStringExtra("leaveDate");
            String reason = getIntent().getStringExtra("reason");

            leaveNoTextView.setText(leaveNo);
            leaveDateTextView.setText(leaveDate);
            reasonTextView.setText(reason);

            lightSensorManager = new LightSensorManager(this, this);

            ImageButton homeBtn = findViewById(R.id.home_btn);
            homeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addLeaveIntent = new Intent(ViewActivity.this, DashboardActivity.class);
                    startActivity(addLeaveIntent);
                }
            });

            ImageButton addLeaveBtn = findViewById(R.id.add_leave_btn);
            addLeaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addLeaveIntent = new Intent(ViewActivity.this, RequestLeaveActivity.class);
                    startActivity(addLeaveIntent);
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
        TextView viewLeaveNo = findViewById(R.id.viewLeaveNo);
        TextView viewLeaveDate = findViewById(R.id.viewLeaveDate);
        TextView viewLeaveReason = findViewById(R.id.viewLeaveReason);
        TextView leaveNoText = findViewById(R.id.leaveNoText);
        TextView leaveDateText = findViewById(R.id.leaveDateText);
        TextView leaveReasonText = findViewById(R.id.leaveReasonText);
        if (lux < 1000) {
            findViewById(R.id.viewLeavePageLayout).setBackgroundColor(Color.BLACK);
            viewLeaveNo.setTextColor(Color.WHITE);
            viewLeaveDate.setTextColor(Color.WHITE);
            viewLeaveReason.setTextColor(Color.WHITE);
            leaveNoText.setTextColor(Color.YELLOW);
            leaveDateText.setTextColor(Color.YELLOW);
            leaveReasonText.setTextColor(Color.YELLOW);
        } else {
            findViewById(R.id.viewLeavePageLayout).setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
        }
    }
}
