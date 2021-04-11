package com.example.marketapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.marketapp.model.APICalls;
import com.example.marketapp.model.Product;
import com.example.marketapp.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CartItemsActivity extends ListActivity {

    private static String cartID;
    private static  List ListOfProducts;
    private APICalls apiCalls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiCalls = new APICalls();
        setContentView(R.layout.past_transactions);
        cartID = getIntent().getStringExtra("CART_ID");
        getPastTransactions();
    }

    //api call to get past transactions
    public void getPastTransactions()
    {
        getProducts();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                ListOfProducts);

        setListAdapter(adapter);

    };

    public static void setListOfProducts(ArrayList<Product> allProducts) {
        ListOfProducts = allProducts;
    }


    public void getProducts()
    {
        APICalls.GetProducts getProducts = apiCalls.new GetProducts(cartID);
        Thread thr = new Thread(getProducts);
        thr.start();
    }
}