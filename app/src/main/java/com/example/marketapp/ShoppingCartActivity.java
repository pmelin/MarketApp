package com.example.marketapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.marketapp.model.CartProduct;
import com.example.marketapp.model.ShoppingCart;
import com.example.marketapp.model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends ListActivity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.shopping_cart);

        List listItems = new ArrayList<String>();

        for (CartProduct cartProduct : ShoppingCart.getProducts()) {
            listItems.add(cartProduct.getProduct().getName());
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);

        setListAdapter(adapter);


        Button checkoutButton = (Button) findViewById(R.id.btnCheckout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent checkoutActivity = new Intent(ShoppingCartActivity.this, CheckoutActivity.class);
                startActivity(checkoutActivity);
            }
        });
    }
}