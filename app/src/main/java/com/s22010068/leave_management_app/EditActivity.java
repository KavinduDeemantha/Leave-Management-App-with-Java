package com.s22010068.leave_management_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class EditActivity extends AppCompatActivity implements LightSensorManager.LightSensorCallback {
    private LightSensorManager lightSensorManager;
    DatabaseHelper myDB;
    EditText leaveDateEdit, leaveReasonEdit;
    Button editLeaveOkBtn;
    String leaveNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editleave);

        lightSensorManager = new LightSensorManager(this, this);
        myDB = new DatabaseHelper(this);

        leaveDateEdit = findViewById(R.id.LeaveDate);
        leaveReasonEdit = findViewById(R.id.leaveReason);
        editLeaveOkBtn = findViewById(R.id.editLeaveOk);

        leaveNo = getIntent().getStringExtra("leaveNo");

        editLeaveOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newLeaveDate = leaveDateEdit.getText().toString();
                String newReason = leaveReasonEdit.getText().toString();

                if (newLeaveDate.isEmpty() || newReason.isEmpty()) {
                    Toast.makeText(EditActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean isUpdated = myDB.updateData(leaveNo, newLeaveDate, newReason);
                if (isUpdated) {
                    Toast.makeText(EditActivity.this, "Data updated", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditActivity.this, RequestLeaveActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditActivity.this, "Data not updated", Toast.LENGTH_LONG).show();
                }
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
            findViewById(R.id.editPageLayout).setBackgroundColor(Color.BLACK);
        } else {
            findViewById(R.id.editPageLayout).setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
        }
    }
}

