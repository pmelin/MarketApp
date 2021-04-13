package com.example.marketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marketapp.dto.RegisterRequestDTO;
import com.example.marketapp.dto.RegisterResponseDTO;
import com.example.marketapp.repository.SettingsRepository;
import com.example.marketapp.service.BackendService;
import com.example.marketapp.service.KeyService;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {

    //To Do: rule for password.

    private String getValue(int id) {
        return ((EditText) findViewById(id)).getText().toString();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SettingsRepository.isUserRegistered(RegistrationActivity.this)) {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        setContentView(R.layout.activity_registration);

        Button btnRegister = (Button) findViewById(R.id.btnRegister);

        EditText expirationDate=(EditText) findViewById(R.id.userCardExpirationDate);
        expirationDate.setInputType(InputType.TYPE_NULL);

        expirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(RegistrationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                expirationDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        btnRegister.setOnClickListener(v -> {

            String name = getValue(R.id.username);
            String nickName = getValue(R.id.userNickname);
            String password = getValue(R.id.userPassword);
            String cardNumber = getValue(R.id.userCardNumber);
            String cardCvv = getValue(R.id.userCardCVV);
            String cardExpirationDate = getValue(R.id.userCardExpirationDate);

            boolean isFormValid = checkMandatoryFields(name, nickName, cardNumber, cardExpirationDate,
                    cardCvv, password);

            if (isFormValid) {
                String userPublicKey = KeyService.generateAndStoreKeys(RegistrationActivity.this);

                RegisterRequestDTO request = new RegisterRequestDTO(name, nickName, userPublicKey,
                        cardNumber, cardExpirationDate, cardCvv);

                Thread register = new Thread(() -> {
                    try {
                        RegisterResponseDTO response = BackendService.register(request);
                        //save the user data on the App's Preferences
                        SettingsRepository.saveRegistration(response.getUuid(), nickName, password, response.getMarketPublicKey(), RegistrationActivity.this);

                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error registering user: "
                                + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                register.start();
            }
        });
    }

    private boolean checkMandatoryFields(String name, String nickName, String cardNumber, String cardExpirationDate,
                                         String cardCvv, String password) {
        if (name.isEmpty()) {
            System.out.println(name);
            Toast.makeText(getApplicationContext(), "The name field is mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (nickName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "The nickname field is mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "The password field is mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (cardNumber.isEmpty()) {
            Toast.makeText(getApplicationContext(), "The card number field is mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (cardExpirationDate.isEmpty()) {
            Toast.makeText(getApplicationContext(), "The card expiration date field is mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (cardCvv.isEmpty()) {
            Toast.makeText(getApplicationContext(), "The card cvv field is mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}