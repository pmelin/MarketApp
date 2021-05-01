package com.example.marketapp;

import android.app.ListActivity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.example.marketapp.repository.SettingsRepository;

import org.json.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class ShoppingCartActivity extends ListActivity implements AdapterView.OnItemSelectedListener  {

    private TextView lblTotal;
    private Voucher voucherSelected = null;

    private APICalls apiCalls;
    private String UserID;

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
        UserID = SettingsRepository.getUserUUID(ShoppingCartActivity.this);

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
        JSONObject jsonObj = new JSONObject();
        // user id
        jsonObj.put("user_id", UserID);
        // voucher if possible
        if (voucherSelected != null) {
            jsonObj.put("voucher_code", voucherSelected.getCode());
        }
        // total value of the cart
        jsonObj.put("total", lblTotal.getText());
        // json array with the id's of the products
        JSONArray jsonA = new JSONArray();
        ShoppingCart.getProducts().forEach( product -> jsonA.put(product.getId()));

        jsonObj.put("product_id" , jsonA);
        return jsonObj.toString() + "hash:" + prepareHash(jsonObj.toString());
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

    private void getAllVouchers() throws InterruptedException {
        APICalls.GetVouchers getVouchers = apiCalls.new GetVouchers(UserID);
        Thread thr = new Thread(getVouchers);
        thr.start();
        thr.join();
    }

    private String prepareHash(String jsonObjectString)
    {
        String hashValue = getHashValue(jsonObjectString);

        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        try {
            ks.load(null);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Entry entry = null;
        try {
            entry = ks.getEntry(Constants.keyname_real, null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        }
        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            Log.w(null,"Not an instance of a PrivateKeyEntry");
            return null;
        }

        byte[] tag = hashValue.getBytes(Charset.defaultCharset());
        byte[] finaltag = new byte[0];

        try {
            Cipher cipher = Cipher.getInstance(Constants.ENC_ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, ((KeyStore.PrivateKeyEntry) entry).getPrivateKey());
            finaltag = cipher.doFinal(tag);
        }
        catch (Exception e) {
        }

        //////////////////////////////////////////////////////////////////////////////////////////////
        // SO PARA TESTES A PARTIR DAQUI ////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////
        tag = new String("Ola teste").getBytes(StandardCharsets.UTF_8);
        String pubKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw3aneWngzELb6cx2F5Gu pyKBLTq2PEa3qvI4zfoCLJaa4TR2GcXI1db0l7SErWKw1LWsv5NJxqHu2pA1CuL8 MxcedTRhZt2ysKY3gcesj9x7QEUBKPGK9WtyzdztT5Rn2U0yEg+CLXYrHTeTul/N eHOKj+rs6TzRLc3UbyMXgaiQiLwmRyOE7V9uxaaxAxbiWtEqJwA8X9hQ5ACUo0dK qwWqBDCfIhhWCyyICMXuj6mZPqCD0Ms4lG8p1nHxuota95l6aNczos37Hmfl+sW1 l7Srda37Sf/OdZaHAywG6TuhXJsBcU5Xzz3iwuj7+RuwEyvShCXuc9zlDbVzSyqx jQIDAQAB";
        PublicKey publicKey = null;


        byte[] tagEncriptada = null;
        try {
            Cipher cipher = Cipher.getInstance(Constants.ENC_ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, ((KeyStore.PrivateKeyEntry) entry).getPrivateKey());
            tagEncriptada = cipher.doFinal(tag);
        }
        catch (Exception e) {
        }

        byte[] tagDecrypted = null;
        try {
            tagDecrypted = decryptByPublicKey(tagEncriptada, pubKeyString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(tagDecrypted == tag)
        {
            System.out.println("MILAGRE");
        }

        /*try {
            publicKey = getPublicKey(pubKeyString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] tagEncriptada = null;
        try {
            Cipher cipher = Cipher.getInstance(Constants.ENC_ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, ((KeyStore.PrivateKeyEntry) entry).getPrivateKey());
            tagEncriptada = cipher.doFinal(tag);
        }
        catch (Exception e) {
        }

        byte[] tagDecriptada = null;

        try {
            Cipher cipher = Cipher.getInstance(Constants.ENC_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            tagDecriptada = cipher.doFinal(tagEncriptada);
        }
        catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }

        String finalString = new String(tagDecriptada, StandardCharsets.ISO_8859_1);*/

         String encryptedhash = new String(finaltag);

        return  encryptedhash;
    }

    public static byte[] encryptByPrivateKey(byte[] data, String key)
            throws Exception {

        byte[] keyBytes = decryptBASE64(key);


        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);


        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    public static byte[] decryptByPublicKey(byte[] data, String key)
            throws Exception {

        byte[] keyBytes = decryptBASE64(key);


        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicKey = keyFactory.generatePublic(x509KeySpec);


        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.decode(key, Base64.DEFAULT);
    }

    private PublicKey getPublicKey(String pubKeyStr) throws Exception
    {
        byte[] publicBytes = Base64.decode(pubKeyStr, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        return pubKey;
    }


    private String getHashValue(String value)
    {
        // Encode string into SHA-256
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] encodedhash = digest.digest(
        value.getBytes(StandardCharsets.UTF_8));

        // now transform the byte array into hexadecimal so we can convert back to string

        StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
        for (int i = 0; i < encodedhash.length; i++) {
            String hex = Integer.toHexString(0xff & encodedhash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}