package com.s22010068.leave_management_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class DeleteActivity extends AppCompatActivity implements LightSensorManager.LightSensorCallback {
    private LightSensorManager lightSensorManager;
    DatabaseHelper myDB;
    String leaveNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deleteleave);
        lightSensorManager = new LightSensorManager(this, this);
        myDB = new DatabaseHelper(this);

        leaveNo = getIntent().getStringExtra("leaveNo");

        Button yesBtn = findViewById(R.id.yesBtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isDeleted = myDB.deleteData(leaveNo);
                if (isDeleted) {
                    Toast.makeText(DeleteActivity.this, "Data deleted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DeleteActivity.this, LeaveDeletedActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(DeleteActivity.this, "Data not deleted", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button noBtn = findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeleteActivity.this, RequestLeaveActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
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
            findViewById(R.id.deletePageLayout).setBackgroundColor(Color.BLACK);
        } else {
            findViewById(R.id.deletePageLayout).setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
        }
    }
}

