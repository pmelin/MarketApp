package com.example.marketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.*;
import com.example.marketapp.model.Product;
import com.google.zxing.Result;

// Scanning library: https://github.com/yuriy-budiyev/code-scanner
public class ScanProductCodeActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.marketapp.SCANNED_PRODUCT";

    private CodeScanner mCodeScanner;

    // handles the value of the decoded QR code
    private final DecodeCallback decodeCallback = new DecodeCallback() {
        public void onDecoded(@NonNull final Result result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   try {
                       // builds the product from the QR code
                       Product product = Product.fromQrCode(result.getText());
                       // sends to the product detail screen
                       Intent intent = new Intent(ScanProductCodeActivity.this, ProductDetailActivity.class);
                       intent.putExtra(EXTRA_MESSAGE, product);
                       startActivity(intent);
                   } catch(Exception e) {
                       Toast.makeText(ScanProductCodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_product);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(this.decodeCallback);
        scannerView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}