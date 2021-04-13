package com.example.marketapp.dto;

public class RegisterResponseDTO {
    private String uuid;
    private String marketPublicKey;

    public RegisterResponseDTO() {

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMarketPublicKey() {
        return marketPublicKey;
    }

    public void setMarketPublicKey(String marketPublicKey) {
        this.marketPublicKey = marketPublicKey;
    }
}
