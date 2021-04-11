package com.example.marketapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.marketapp.model.Voucher;
import com.example.marketapp.model.APICalls;
import java.util.ArrayList;
import java.util.List;


public class VoucherActivity extends ListActivity {

    private static String UserID = "1";
    private static List vouchers;
    private APICalls apiCalls;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        apiCalls = new APICalls();
        setContentView(R.layout.voucher);
        getAvailableVouchers();
    }

    public static void setVoucherList(ArrayList<Voucher> listVouchers) {
        vouchers = listVouchers;
    }

    //api call to get available vouchers
    public void getAvailableVouchers() {

         getAllVouchers();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                vouchers);

        setListAdapter(adapter);
    };

    public void getAllVouchers()
    {
        APICalls.GetVouchers getVouchers = apiCalls.new GetVouchers(UserID);
        Thread thr = new Thread(getVouchers);
        thr.start();
    }

}