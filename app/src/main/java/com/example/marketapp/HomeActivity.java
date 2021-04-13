package com.example.marketapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity {
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

        setContentView(R.layout.home_screen);

        Button scanProductButton = (Button) findViewById(R.id.btnScanProduct);
        scanProductButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent scanProductCodeActivity = new Intent(HomeActivity.this, ScanProductCodeActivity.class);
                startActivity(scanProductCodeActivity);
            }
        });

        Button checkoutButton = (Button) findViewById(R.id.btnShoppingCart);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent shoppingCartActivity = new Intent(HomeActivity.this, ShoppingCartActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.retrieveVouchers:
                startActivity(new Intent(this, VoucherActivity.class));
                return true;
            case R.id.pastTransactions:
                startActivity(new Intent(this, PastTransactionsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}