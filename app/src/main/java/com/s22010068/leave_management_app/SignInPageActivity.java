package com.s22010068.leave_management_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SignInPageActivity extends AppCompatActivity implements LightSensorManager.LightSensorCallback {
    private LightSensorManager lightSensorManager;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private EditText usernameEditText, passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        lightSensorManager = new LightSensorManager(this, this);
        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);

        Button sign_in_Btn = findViewById(R.id.signInBtn);
        sign_in_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (databaseHelper.validateUser(username, password)) {
                    SharedPreferences sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", username);
                    editor.apply();
                    sessionManager.createLoginSession(username);

                    Intent signInIntent = new Intent(SignInPageActivity.this, DashboardActivity.class);
                    startActivity(signInIntent);
                    finish();
                } else if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignInPageActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(SignInPageActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
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
        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);
        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        if (lux < 1000) {
            findViewById(R.id.signInPageLayout).setBackgroundColor(Color.BLACK);
            username.setTextColor(Color.WHITE);
            password.setTextColor(Color.WHITE);
            editTextUsername.setTextColor(Color.YELLOW);
            editTextPassword.setTextColor(Color.YELLOW);
        } else {
            findViewById(R.id.signInPageLayout).setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundColor));
        }
    }
}
