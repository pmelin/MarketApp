package com.example.marketapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.marketapp.model.CartProduct;
import com.example.marketapp.model.ShoppingCart;

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
    }
}