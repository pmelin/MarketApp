package com.example.marketapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.marketapp.model.ShoppingCart;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;


public class QRCodeCartActivity extends AppCompatActivity
{

    ImageView qrCodeIv;
    TextView errorTv;
    EditText edMessage;
    public final static int IMAGE_SIZE=540;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_cart);

        Button orderButton = (Button) findViewById(R.id.bt_generate);

        orderButton.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View v) {
                ShoppingCart.removeAllProduct();
                Intent intent = new Intent(QRCodeCartActivity.this , HomeActivity.class);
                startActivity(intent);
           }
        });

                String jsonObjectString = getIntent().getStringExtra("JSON_TO_CONVERT");
        qrCodeIv = findViewById(R.id.img_qr_code);
        errorTv = findViewById(R.id.tv_error);
        Thread thr = new Thread(new convertToQR(jsonObjectString));
        thr.start();
    }

    class convertToQR implements Runnable
    {
        String content;

        convertToQR(String value) {
          content = value;
          errorTv.setText("");
        }
         @Override
        public void run() {
          final Bitmap bitmap;

          bitmap = encodeAsBitmap(content);
          runOnUiThread(()->qrCodeIv.setImageBitmap(bitmap));
        }
      }

    private Bitmap encodeAsBitmap(String str) {
        BitMatrix result;

        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, IMAGE_SIZE, IMAGE_SIZE, null);
        }
        catch (Exception exc) {
            runOnUiThread(()->errorTv.setText(exc.getMessage()));
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int line = 0; line < h; line++) {
            int offset = line * w;
            for (int col = 0; col < w; col++) {
                pixels[offset + col] = result.get(col, line) ? getResources().getColor(R.color.colorPrimary):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
