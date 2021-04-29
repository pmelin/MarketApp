package com.example.marketapp.service;


import com.example.marketapp.dto.RegisterRequestDTO;
import com.example.marketapp.dto.RegisterResponseDTO;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class BackendService {

    public static final String BACKEND_HOST = "https://mesw-market-api.herokuapp.com";

    private static HttpURLConnection sendPostRequest(URL url, String jsonRequest) throws Exception{
       // System.out.println("URL: " + url.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            //writes the json string into the request body
            byte[] input = jsonRequest.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        return con;
    }

    private static boolean isRedirect(int status) {
        return (status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == 308
                || status == HttpURLConnection.HTTP_MOVED_PERM
                || status == HttpURLConnection.HTTP_SEE_OTHER);
    }

    //returns a string of the json response
    private static final String postJson(Serializable request, String path) throws Exception{
        URL url = new URL(BACKEND_HOST + path);
        HttpURLConnection con = null;
        String json = null;

        try {
            //transforms the request DTO into a json string
            String jsonRequest = new Gson().toJson(request);
            con = sendPostRequest(url, jsonRequest);

            while(isRedirect(con.getResponseCode())) {
                String redirectUrl = con.getHeaderField("Location");

                try {
                    // closes the previous request
                    con.disconnect();
                } catch(Exception ignored){}

                // sends the request to the redirected URL
                con = sendPostRequest(new URL(redirectUrl), jsonRequest);
            }

            if (con.getResponseCode() != 200) {
                throw new Exception("Received HTTP response code: " + con.getResponseCode());
            }
            //reads the json response
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                json = response.toString();
            }
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return json;
    }

    public static RegisterResponseDTO register(RegisterRequestDTO request) throws Exception {
        String jsonResponse = postJson(request, "/user/");
        //transforms the response json string into the responseDTO
        return new Gson().fromJson(jsonResponse, RegisterResponseDTO.class);
    }
}
