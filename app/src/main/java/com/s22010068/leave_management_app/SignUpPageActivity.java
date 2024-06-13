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

public class SignUpPageActivity extends AppCompatActivity implements LightSensorManager.LightSensorCallback {
    private LightSensorManager lightSensorManager;
    private DatabaseHelper databaseHelper;
    private EditText employeeNoText, usernameText, passwordText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        lightSensorManager = new LightSensorManager(this, this);
        databaseHelper = new DatabaseHelper(this);

        EditText employeeNoEditText = findViewById(R.id.employeeNoText);
        EditText usernameEditText = findViewById(R.id.usernameText);
        EditText passwordEditText = findViewById(R.id.passwordText);

        Button sign_up_Btn = findViewById(R.id.sign_up_btn);
        sign_up_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String employeeNo = employeeNoEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty() || employeeNo.isEmpty()) {
                    Toast.makeText(SignUpPageActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = databaseHelper.insertUser(employeeNo, username, password);
                    if (isInserted) {
                        Toast.makeText(SignUpPageActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        Intent signUpIntent = new Intent(SignUpPageActivity.this, SignInPageActivity.class);
                        startActivity(signUpIntent);
                        finish();
                    } else {
                        Toast.makeText(SignUpPageActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
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
            findViewById(R.id.signUpPageLayout).setBackgroundColor(Color.BLACK);
        } else {
            findViewById(R.id.signUpPageLayout).setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
        }
    }
}
