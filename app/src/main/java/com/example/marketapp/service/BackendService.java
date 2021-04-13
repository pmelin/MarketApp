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

public class BackendService {

    public static final String BACKEND_HOST = "http://10.0.2.2:3000";

    //returns a string of the json response
    private static final String postJson(Serializable request, String path) throws Exception{
        URL url = new URL(BACKEND_HOST + path);
        HttpURLConnection con = null;
        String json = null;

        try {
            //transforms the request DTO into a json string
            String jsonRequest = new Gson().toJson(request);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                //writes the json string into the request body
                byte[] input = jsonRequest.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            //reads the json response
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
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
        String jsonResponse = postJson(request, "/users");
        //transforms the response json string into the responseDTO
        return new Gson().fromJson(jsonResponse, RegisterResponseDTO.class);
    }
}
