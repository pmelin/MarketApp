package com.example.marketapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.marketapp.model.Voucher;
import com.example.marketapp.model.APICalls;
import com.example.marketapp.repository.SettingsRepository;

import java.util.ArrayList;
import java.util.List;


public class VoucherActivity extends ListActivity {

    private static String UserID;

    private APICalls apiCalls;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        UserID = SettingsRepository.getUserUUID(VoucherActivity.this);
        apiCalls = new APICalls();
        setContentView(R.layout.voucher);
        getAvailableVouchers();
    }


    //api call to get available vouchers
    public void getAvailableVouchers() {

        try {
            getAllVouchers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (apiCalls.voucherListAPI.size() == 0)
        {
            LinearLayout lin = (LinearLayout) findViewById(R.id.linearLayout2);
            EditText et = new EditText(this);
            et.setText("The list is empty , you don't have vouchers yet");
            et.setMinLines(1);
            et.setMaxLines(3);
            lin.addView(et);
        } else {
            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    apiCalls.voucherListAPI);

            setListAdapter(adapter);
        }
    };

    public void getAllVouchers() throws InterruptedException {
        APICalls.GetVouchers getVouchers = apiCalls.new GetVouchers(UserID);
        Thread thr = new Thread(getVouchers);
        thr.start();
        thr.join();
    }

}