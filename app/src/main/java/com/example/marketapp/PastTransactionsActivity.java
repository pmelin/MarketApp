package com.example.marketapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.marketapp.model.APICalls;
import com.example.marketapp.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PastTransactionsActivity extends ListActivity {

    private static String UserID = "1";
    private static  List<Transaction> listAllCarts;
    private APICalls apiCalls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiCalls = new APICalls();
        setContentView(R.layout.past_transactions);
        getPastTransactions();
    }

    //api call to get past transactions
    public void getPastTransactions()
    {
        try {
            getAllCars();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter<Transaction>(this,
                android.R.layout.simple_list_item_1,
                listAllCarts);

        setListAdapter(adapter);

        // Create logic to deal with clicking in a cart and opening new activity with cart products
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PastTransactionsActivity.this, CartItemsActivity.class);
                intent.putExtra("CART_ID" , listAllCarts.get(position).getCarID());
                startActivity(intent);
            }
        });


    };

    public static void setAllCarsList(ArrayList<Transaction> allCarts) {
        listAllCarts = allCarts;
    }


    public void getAllCars() throws InterruptedException {
        APICalls.GetAllCarts getAllCarts = apiCalls.new GetAllCarts(UserID);
        Thread thr = new Thread(getAllCarts);
        thr.start();
        thr.join();
    }
}