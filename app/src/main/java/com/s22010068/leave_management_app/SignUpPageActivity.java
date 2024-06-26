package com.s22010068.leave_management_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SignUpPageActivity extends AppCompatActivity implements LightSensorManager.LightSensorCallback {
    private LightSensorManager lightSensorManager;
//    private DatabaseHelper databaseHelper;
    private UserManager userManager;
    private EditText employeeNoText, usernameText, passwordText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        lightSensorManager = new LightSensorManager(this, this);
//        databaseHelper = new DatabaseHelper(this);
        userManager = new UserManager(this);

        employeeNoText = findViewById(R.id.employeeNoText);
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);

        Button sign_up_Btn = findViewById(R.id.sign_up_btn);
        sign_up_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String employeeNo = employeeNoText.getText().toString();
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                if (username.isEmpty() || password.isEmpty() || employeeNo.isEmpty()) {
                    Toast.makeText(SignUpPageActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = userManager.registerUser(employeeNo, username, password);
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
        TextView employeeNumber = findViewById(R.id.employeeNumber);
        TextView enter_username = findViewById(R.id.enter_username);
        TextView enter_pwd = findViewById(R.id.enter_pwd);
        EditText employeeNoText = findViewById(R.id.employeeNoText);
        EditText usernameText = findViewById(R.id.usernameText);
        EditText passwordText = findViewById(R.id.passwordText);
        if (lux < 1000) {
            findViewById(R.id.signUpPageLayout).setBackgroundColor(Color.BLACK);
            employeeNumber.setTextColor(Color.WHITE);
            enter_username.setTextColor(Color.WHITE);
            enter_pwd.setTextColor(Color.WHITE);
            employeeNoText.setTextColor(Color.YELLOW);
            usernameText.setTextColor(Color.YELLOW);
            passwordText.setTextColor(Color.YELLOW);
        } else {
            findViewById(R.id.signUpPageLayout).setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
        }
    }
}
