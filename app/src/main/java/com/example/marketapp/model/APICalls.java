package com.example.marketapp.model;

import android.util.Log;

import com.example.marketapp.CartItemsActivity;
import com.example.marketapp.PastTransactionsActivity;
import com.example.marketapp.VoucherActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.*;

public class APICalls {

    public List voucherListAPI;

    private void GetVouchersList(String jsonString) throws JSONException {
        ArrayList<Voucher> voucherList = new ArrayList<>();
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray arr = obj.getJSONArray("data");

        for (int i = 0; i < arr.length(); i++)
        {
            JSONObject jsonObject = arr.getJSONObject(i);
            long voucherID = jsonObject.getLong("id");
            String voucherCODE = jsonObject.getString("code");
            int discount = jsonObject.getInt("discount");
            voucherList.add(new Voucher((long) voucherID, voucherCODE, discount));
        }

        voucherListAPI = voucherList;
    }

    private void GetAllCartsList(String jsonString) throws JSONException {
        ArrayList<Transaction> cartsList = new ArrayList<>();
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray arr = obj.getJSONArray("data");

        for (int i = 0; i < arr.length(); i++)
        {
            String voucherValue = arr.getJSONObject(i).getString("discount");
            String date = arr.getJSONObject(i).getString("added_on");
            long cartID = arr.getJSONObject(i).getLong("id");
            long value = arr.getJSONObject(i).getLong("total");

            cartsList.add(new Transaction(voucherValue, cartID, value, date));
        }

        PastTransactionsActivity.setAllCarsList(cartsList);
    }

    private void GetProductsInsideCart(String jsonString) throws JSONException {
        ArrayList<Product> productsList = new ArrayList<>();
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray arr = obj.getJSONArray("data");

        for (int i = 0; i < arr.length(); i++)
        {
            //Long productID = arr.getJSONObject(i).getLong("voucher_id");
            String name = arr.getJSONObject(i).getString("title");
            Double value = arr.getJSONObject(i).getDouble("price");

            productsList.add(new Product(UUID.randomUUID(), name, value));
        }

        CartItemsActivity.setListOfProducts(productsList);
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

    //-----------------------------------------------------------------------------
    // internal classes to deal with the REST operations

    public class GetVouchers implements Runnable {
        String address = "https://mesw-market-api.herokuapp.com/voucher/";

        String userID;

        public GetVouchers(String userID) {
            this.userID = userID;
        }

        @Override
        public void run() {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(address + userID);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setUseCaches (false);

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == 200) {
                    String response = readStream(urlConnection.getInputStream());
                    Log.d("Log: ",response);
                    GetVouchersList(response);
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

    public class GetAllCarts implements Runnable {
        String address = "https://mesw-market-api.herokuapp.com/cart/summary/";
        String userID;

        public GetAllCarts(String userID) {
            this.userID = userID;
        }

        @Override
        public void run() {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(address + userID);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setUseCaches (false);

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == 200) {
                    String response = readStream(urlConnection.getInputStream());
                    Log.d("Log: ",response);
                    GetAllCartsList(response);
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


    public class GetProducts implements Runnable {
        String address = "https://mesw-market-api.herokuapp.com/cart/detail/";
        String cartID;

        public GetProducts(String cartID) {
            this.cartID = cartID;
        }

        @Override
        public void run() {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(address + cartID);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setUseCaches (false);

                int responseCode = urlConnection.getResponseCode();
                if(responseCode == 200) {
                    String response = readStream(urlConnection.getInputStream());
                    Log.d("Log: ",response);
                    GetProductsInsideCart(response);
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
