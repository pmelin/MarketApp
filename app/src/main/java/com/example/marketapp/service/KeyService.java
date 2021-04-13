package com.example.marketapp.service;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;


import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

public class KeyService {
    private static final int KEY_SIZE = 512;
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String KEY_ALGO = "RSA";
    private static final String SIGN_ALGO = "SHA256WithRSA";
    private static String KEY_NAME = "marketAppKey";


    /**
     * Retrieves the keys from androids keystore.
     */
    private static final KeyStore.Entry getKeyEntry() throws Exception {
        System.out.println("Retrieving keys from keystore...");
        KeyStore ks = KeyStore.getInstance(ANDROID_KEYSTORE);
        ks.load(null);
        return ks.getEntry(KEY_NAME, null);
    }

    /**
     * Encodes the bytes of a public key into a base64 string.
     */
    private static String getPublicKeyAsString(PublicKey publicKey) throws Exception{
        byte[] publicBytes = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(publicBytes);
    }

    /**
     * Generates a keypair and returns the public key as base64 string.
     */
    public static String generateAndStoreKeys(Context context){
        try {
            KeyStore.Entry entry = getKeyEntry();
            if (entry == null) {
                System.out.println("Keys not found. Generating new ones...");
                Calendar start = new GregorianCalendar();
                Calendar end = new GregorianCalendar();
                end.add(Calendar.YEAR, 20);

                KeyPairGenerator kgen = KeyPairGenerator.getInstance(KEY_ALGO, ANDROID_KEYSTORE);
                AlgorithmParameterSpec spec = new KeyPairGeneratorSpec.Builder(context)
                        .setKeySize(KEY_SIZE)
                        .setAlias(KEY_NAME)
                        .setSubject(new X500Principal("CN=" + KEY_NAME))
                        .setSerialNumber(BigInteger.valueOf(12121212))
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                kgen.initialize(spec);
                KeyPair kp = kgen.generateKeyPair();
                System.out.println("Keys generated successfully");
                return getPublicKeyAsString(kp.getPublic());
            }

            System.out.println("Keys already exist.");
            PublicKey publicKey = ((KeyStore.PrivateKeyEntry)entry).getCertificate().getPublicKey();
            return getPublicKeyAsString(publicKey);

        }
        catch (Exception ex) {
            throw new RuntimeException("Error generating / storing keys", ex);
        }
    }


}
