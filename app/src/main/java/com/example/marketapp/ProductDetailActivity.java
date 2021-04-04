package com.example.marketapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marketapp.model.Product;
import com.example.marketapp.model.ShoppingCart;

public class ProductDetailActivity extends AppCompatActivity {

    void setLabelValue(int id, Object value) {
        TextView view = findViewById(id);
        view.setText(String.valueOf(value));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        // Get the Intent that started this activity and extract the product
        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra(ScanProductCodeActivity.EXTRA_MESSAGE);

        // fills the product details on the labels
        setLabelValue(R.id.lblDetailId, product.getId());
        setLabelValue(R.id.lblDetailName, product.getName());
        setLabelValue(R.id.lblDetailPrice, product.getPrice() + "€ / ");

        // adds the product to to the cart
        Button button = (Button) findViewById(R.id.btnAddToCart);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    ShoppingCart.addProduct(product);
                    Toast.makeText(ProductDetailActivity.this, product.getName() + " added to the shopping cart!", Toast.LENGTH_SHORT).show();

                    // goes back to the main menu
                    Intent i= new Intent(ProductDetailActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
