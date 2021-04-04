package com.example.marketapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.marketapp.model.Voucher;
import java.util.ArrayList;
import java.util.List;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VoucherActivity extends ListActivity {
    private String DatabaseAddress = null;
    private List listVouchers = new ArrayList<Voucher>();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.voucher);
        getAvailableVouchers();

    }

    //api call to get available vouchers
    public void getAvailableVouchers() {

        listVouchers.add("voucher 1");
        listVouchers.add("voucher 2");
        listVouchers.add("voucher 3");

        getAllVouchers();

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listVouchers);

        setListAdapter(adapter);
    };

    public void getAllVouchers()
    {
        GetUsers getUsers = new GetUsers(DatabaseAddress);
        Thread thr = new Thread(getUsers);
        thr.start();
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        catch (IOException e) {
            return e.getMessage();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    return e.getMessage();
                }
            }
        }
        return response.toString();
    }

    private class GetUsers implements Runnable {
        String address = null;

        GetUsers(String baseAddress) {
            address = baseAddress;
        }

        @Override
        public void run() {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL("http://" + address + ":8701/Rest/users");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setUseCaches (false);

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == 200) {
                    String response = readStream(urlConnection.getInputStream());
                    Log.d("Log: ",response);
                    listVouchers.add(new Voucher(null, null));
                }
                else
                    Log.d("Log: ", String.valueOf(responseCode));
            }
            catch (Exception e) {
                Log.d("Log: ", e.toString());
            }
            finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
        }
    }
}