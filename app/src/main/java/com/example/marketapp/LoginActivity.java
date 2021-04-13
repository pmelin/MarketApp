package com.example.marketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marketapp.repository.SettingsRepository;

public class LoginActivity extends AppCompatActivity {

    EditText userPassword, username;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        userPassword = (EditText)findViewById(R.id.userPassword);
        username = (EditText)findViewById(R.id.username);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean verifiesFields = checkMandatoryFields();
                if (verifiesFields) {

                    boolean isLoginValid = SettingsRepository.validateCredentials(username.getText().toString(), userPassword.getText().toString(), LoginActivity.this);

                    if (!isLoginValid) {
                        Toast.makeText(getApplicationContext(), "Invalid credentials.",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent homeActivity = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(homeActivity);
                }
            }
        });
    }

    private boolean checkMandatoryFields() {
        if (username.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "The username field is mandatory.",Toast.LENGTH_SHORT).show();
            return false;
        } else if (userPassword.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "The password field is mandatory.",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}