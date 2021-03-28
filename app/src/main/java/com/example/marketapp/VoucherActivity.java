package com.example.marketapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.marketapp.model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class VoucherActivity extends ListActivity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.voucher);
        getAvailableVouchers();

    }

    //api call to get available vouchers
    public void getAvailableVouchers() {
        List listVouchers = new ArrayList<Voucher>();

        listVouchers.add("voucher 1");
        listVouchers.add("voucher 2");
        listVouchers.add("voucher 3");


        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listVouchers);

        setListAdapter(adapter);
    };
}