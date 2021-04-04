package com.example.marketapp;

import android.app.ListActivity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marketapp.model.ShoppingCart;
import com.example.marketapp.model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends ListActivity implements AdapterView.OnItemSelectedListener  {
    private void calculateValues() {
        TextView lblTotal = (TextView) findViewById(R.id.lblShoppingCartTotal);
        lblTotal.setText(String.valueOf(ShoppingCart.getTotal() + "€"));
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.shopping_cart);

        ProductListAdapter adapter = new ProductListAdapter(this,
                new ArrayList<>(ShoppingCart.getProducts()));

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                // reloads the shopping cart totals when the list of products changes
                calculateValues();
            }
        });
        setListAdapter(adapter);

        Spinner spinner = findViewById(R.id.voucher_spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> listVouchersNames = new ArrayList<>();

        for (Voucher vouchers : ShoppingCart.getVouchers()) {
            listVouchersNames.add(vouchers.getName());
        }

        ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listVouchersNames);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);

        Button orderButton = (Button) findViewById(R.id.btnOrder);
        orderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ShoppingCart.isEmpty()) {
                    Toast.makeText(ShoppingCartActivity.this, "The shopping cart is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        calculateValues();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}