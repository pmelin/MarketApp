package com.example.marketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.*;
import com.example.marketapp.model.Product;
import com.example.marketapp.repository.SettingsRepository;
import com.google.zxing.Result;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

import javax.crypto.Cipher;

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
                       String encTag = result.getText();
                       System.out.println(encTag);
                       System.out.println(hexStringToByteArray(encTag));
                       //decrypts the qrcode
                       decTag(hexStringToByteArray(encTag));

                   } catch(Exception e) {
                       e.printStackTrace();
                       Toast.makeText(ScanProductCodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
                }
            });
        }
    };

    /* s must be an even-length string. */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    String byteArrayToHex(byte[] ba) {
        StringBuilder sb = new StringBuilder(ba.length * 2);
        for(byte b: ba)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

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

    private PublicKey getPublicKey() throws Exception {
        String pemString = SettingsRepository.getMarketPublicKey(ScanProductCodeActivity.this).replace("-----BEGIN PUBLIC KEY-----\n", "");
        pemString = pemString.replace("-----END PUBLIC KEY-----", "");
        byte[] publicBytes = Base64.decode(pemString, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        return pubKey;
    }

    void decTag(byte [] encTag) throws Exception {
        byte[] clearTag;

        Cipher cipher = Cipher.getInstance(Constants.ENC_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, this.getPublicKey());
        clearTag = cipher.doFinal(encTag);


        ByteBuffer tag = ByteBuffer.wrap(clearTag);
        int tId = tag.getInt();
        UUID id = new UUID(tag.getLong(), tag.getLong());
        int euros = tag.getInt();
        int cents = tag.getInt();
        byte[] bName = new byte[tag.get()];
        tag.get(bName);
        String name = new String(bName, StandardCharsets.ISO_8859_1);

        String text = "DecTag (" + clearTag.length + "):\n" + byteArrayToHex(clearTag) + "\n\n" +
                "ID: " + id.toString() + "\n" +
                "Name: " + name + "\n" +
                "Price: €" + euros + "." + cents;


        Product product = new Product(id, name, Double.valueOf(euros));

        // sends to the product detail screen
        Intent intent = new Intent(ScanProductCodeActivity.this, ProductDetailActivity.class);
        intent.putExtra(EXTRA_MESSAGE, product);
        startActivity(intent);
    }
}