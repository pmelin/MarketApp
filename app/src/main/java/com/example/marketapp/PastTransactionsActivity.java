package com.example.marketapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.marketapp.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PastTransactionsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_transactions);
        getPastTransactions();
    }

    //api call to get past transactions
    public void getPastTransactions() {
        List listPastTransactions = new ArrayList<Transaction>();

        listPastTransactions.add("Transaction 1");
        listPastTransactions.add("Transaction 2");
        listPastTransactions.add("Transaction 3");


        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listPastTransactions);

        setListAdapter(adapter);
    };
}