package com.s22010068.leave_management_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class RequestLeaveActivity extends AppCompatActivity implements LightSensorManager.LightSensorCallback {
    private LightSensorManager lightSensorManager;
    DatabaseHelper myDB;
    EditText enterLeaveNo, enterLeaveDate, enterReason, editLeaveNo;
    Button submitBtn, viewBtn, editBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requestleave);

        lightSensorManager = new LightSensorManager(this, this);

        myDB = new DatabaseHelper(this);

        enterLeaveNo = findViewById(R.id.enter_leave_no);
        enterLeaveDate = findViewById(R.id.enter_leave_date);
        enterReason = findViewById(R.id.enter_reason);
        submitBtn = findViewById(R.id.submit_leave);
        editLeaveNo = findViewById(R.id.edit_leave_no);
        viewBtn = findViewById(R.id.view_leave);
        editBtn = findViewById(R.id.edit_leave);
        deleteBtn = findViewById(R.id.delete_leave);

        ImageButton homeBtn = findViewById(R.id.home_btn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addLeaveIntent = new Intent(RequestLeaveActivity.this, DashboardActivity.class);
                startActivity(addLeaveIntent);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("LeavePrefs", MODE_PRIVATE);
                int availableLeaves = sharedPreferences.getInt("leaveCount", 0);

                if (availableLeaves == 0) {
                    Toast.makeText(RequestLeaveActivity.this, "Maximum leave count reached. You cannot request more than 10 leaves", Toast.LENGTH_LONG).show();
                    return;
                }
                String leaveNo = enterLeaveNo.getText().toString();
                String leaveDate = enterLeaveDate.getText().toString();
                String reason = enterReason.getText().toString();

                if (leaveNo.isEmpty() || leaveDate.isEmpty() || reason.isEmpty()) {
                    Toast.makeText(RequestLeaveActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean isInserted = myDB.insertData(leaveNo, leaveDate, reason);
                if (isInserted) {
                    Toast.makeText(RequestLeaveActivity.this, "Data inserted", Toast.LENGTH_LONG).show();

                    // Decrease leave count by 1
                    decreaseLeaveCount();

                    // Navigate back to DashboardActivity
                    Intent dashboardIntent = new Intent(RequestLeaveActivity.this, DashboardActivity.class);
                    startActivity(dashboardIntent);
                    finish();
                } else {
                    Toast.makeText(RequestLeaveActivity.this, "Data not inserted", Toast.LENGTH_LONG).show();
                    Log.e("RequestLeaveActivity", "Data insertion failed");
                }
            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String leaveNo = editLeaveNo.getText().toString();
                if (leaveNo.isEmpty()) {
                    Toast.makeText(RequestLeaveActivity.this, "Please enter Leave NO", Toast.LENGTH_LONG).show();
                    return;
                }

                Cursor cursor = myDB.getLeaveDetails(leaveNo);
                if (cursor != null && cursor.moveToFirst()) {
                    String leaveDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_3));
                    String reason = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_4));
                    Intent viewLeaveIntent = new Intent(RequestLeaveActivity.this, ViewActivity.class);
                    viewLeaveIntent.putExtra("leaveNo", leaveNo);
                    viewLeaveIntent.putExtra("leaveDate", leaveDate);
                    viewLeaveIntent.putExtra("reason", reason);
                    startActivity(viewLeaveIntent);
                    cursor.close();
                } else {
                    Toast.makeText(RequestLeaveActivity.this, "No leave found with that Leave NO", Toast.LENGTH_LONG).show();
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String leaveNo = editLeaveNo.getText().toString();
                if (leaveNo.isEmpty()) {
                    Toast.makeText(RequestLeaveActivity.this, "Please enter Leave NO", Toast.LENGTH_LONG).show();
                    return;
                }

                Cursor cursor = myDB.getLeaveDetails(leaveNo);
                if (cursor != null && cursor.moveToFirst()) {
                    String leaveDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_3));
                    String reason = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_4));
                    Intent editLeaveIntent = new Intent(RequestLeaveActivity.this, EditActivity.class);
                    editLeaveIntent.putExtra("leaveNo", leaveNo);
                    editLeaveIntent.putExtra("leaveDate", leaveDate);
                    editLeaveIntent.putExtra("reason", reason);
                    startActivity(editLeaveIntent);
                    cursor.close();
                } else {
                    Toast.makeText(RequestLeaveActivity.this, "No leave found with that number", Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String leaveNo = editLeaveNo.getText().toString();
                if (leaveNo.isEmpty()) {
                    Toast.makeText(RequestLeaveActivity.this, "Please enter Leave NO", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    Intent deleteIntent = new Intent(RequestLeaveActivity.this, DeleteActivity.class);
                    deleteIntent.putExtra("leaveNo", leaveNo);
                    startActivity(deleteIntent);
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
            findViewById(R.id.requestLeavePageLayout).setBackgroundColor(Color.BLACK);
        } else {
            findViewById(R.id.requestLeavePageLayout).setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
        }
    }
    private void increaseLeaveCount() {
        SharedPreferences sharedPreferences = getSharedPreferences("LeavePrefs", MODE_PRIVATE);
        int currentLeaveCount = sharedPreferences.getInt("leaveCount", 0);
        if (currentLeaveCount < 10) { // Prevent increasing beyond 10
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("leaveCount", currentLeaveCount + 1);
            editor.apply();
        }
    }

    private void decreaseLeaveCount() {
        SharedPreferences sharedPreferences = getSharedPreferences("LeavePrefs", MODE_PRIVATE);
        int currentLeaveCount = sharedPreferences.getInt("leaveCount", 0);
        if (currentLeaveCount > 0) { // Prevent decreasing below 0
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("leaveCount", currentLeaveCount - 1);
            editor.apply();
        }
    }
}



