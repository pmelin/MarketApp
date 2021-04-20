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

import com.example.marketapp.model.APICalls;
import com.example.marketapp.model.ShoppingCart;
import com.example.marketapp.model.Voucher;

import org.json.*;

import java.util.ArrayList;

public class ShoppingCartActivity extends ListActivity implements AdapterView.OnItemSelectedListener  {

    private TextView lblTotal;
    private Voucher voucherSelected = null;

    private APICalls apiCalls;
    private static String UserID = "1";

    private void calculateValues() {
        int voucherDiscount = voucherSelected == null ? 0 : voucherSelected.getDiscount();
        lblTotal.setText(String.valueOf(ShoppingCart.getTotal(voucherDiscount) + "€"));
    }

    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.shopping_cart);
        lblTotal = (TextView) findViewById(R.id.lblShoppingCartTotal);
        apiCalls = new APICalls();

        try {
            getAllVouchers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


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
        ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, apiCalls.voucherListAPI);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        voucherSelected = (Voucher) spinner.getSelectedItem();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                voucherSelected = (Voucher) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                voucherSelected = null;
            }
        });

        Button orderButton = (Button) findViewById(R.id.btnOrder);
        orderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ShoppingCart.isEmpty()) {
                    Toast.makeText(ShoppingCartActivity.this, "The shopping cart is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ShoppingCartActivity.this, QRCodeCartActivity.class);
                    try {
                        intent.putExtra("JSON_TO_CONVERT" , GetInformationReadyToQrCode());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    startActivity(intent);
                }
            }
        });

        calculateValues();
    }

    private String GetInformationReadyToQrCode() throws JSONException
    {
        JSONObject jo = new JSONObject();
        // user id
        jo.put("user_id", UserID);
        // voucher if possible
        if (voucherSelected != null) {
            jo.put("voucher_id", voucherSelected.getId());
        }
        // total value of the cart
        jo.put("total", lblTotal.getText());
        // json array with the id's of the products
        JSONArray jsonA = new JSONArray();
        ShoppingCart.getProducts().forEach( product -> jsonA.put(product.getId()));

        jo.put("product_id" , jsonA);

        System.out.println(jo.toString());
        return jo.toString();
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

    public void getAllVouchers() throws InterruptedException {
        APICalls.GetVouchers getVouchers = apiCalls.new GetVouchers(UserID);
        Thread thr = new Thread(getVouchers);
        thr.start();
        thr.join();
    }
}