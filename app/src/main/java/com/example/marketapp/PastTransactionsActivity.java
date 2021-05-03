package com.example.marketapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.marketapp.model.APICalls;
import com.example.marketapp.model.Transaction;
import com.example.marketapp.repository.SettingsRepository;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PastTransactionsActivity extends ListActivity {

    private static String UserID;
    private static  List<Transaction> listAllCarts;
    private APICalls apiCalls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserID = SettingsRepository.getUserUUID(PastTransactionsActivity.this);
        apiCalls = new APICalls();
        setContentView(R.layout.past_transactions);
        getPastTransactions();
    }

    //api call to get past transactions
    public void getPastTransactions() {
        try {
            getAllCars();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (listAllCarts.size() == 0)
        {
            TextView tv = findViewById(R.id.EmptyListTextView);
            tv.setText("The list is empty , you don't have past carts yet");
            return;
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