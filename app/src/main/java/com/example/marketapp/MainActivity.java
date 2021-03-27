package com.example.marketapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.marketapp.model.ShoppingCart;

public class MainActivity extends AppCompatActivity {
    private boolean isCameraPermissionGranted = false;

    // android api to request camera permission
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                // handles the result of the user choice
                isCameraPermissionGranted = isGranted;
                if (isCameraPermissionGranted) {
                    // user allowed camera access, so we show the menu
                    showMainMenu();
                } else {
                    // user denied camera access
                    showCameraPermissionRequiredScreen();
                }
            });

    private void requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA);
    }

    private void showCameraPermissionRequiredScreen() {
        setContentView(R.layout.camera_permission_denied);

        // user clicked on the allow camera access button
        Button button = (Button) findViewById(R.id.btnAllowCameraAccess);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                requestCameraPermission();
            }
        });
    }

    private void showMainMenu() {
        setContentView(R.layout.main_screen);

        Button scanProductButton = (Button) findViewById(R.id.btnScanProduct);
        scanProductButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent scanProductCodeActivity = new Intent(MainActivity.this, ScanProductCodeActivity.class);
                startActivity(scanProductCodeActivity);
            }
        });

        Button checkoutButton = (Button) findViewById(R.id.btnShoppingCart);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent shoppingCartActivity = new Intent(MainActivity.this, ShoppingCartActivity.class);
                startActivity(shoppingCartActivity);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // launches on the activity startup
        super.onCreate(savedInstanceState);

        this.isCameraPermissionGranted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED;

        if (isCameraPermissionGranted) {
            showMainMenu();
        } else {
            requestCameraPermission();
        }

    }
}