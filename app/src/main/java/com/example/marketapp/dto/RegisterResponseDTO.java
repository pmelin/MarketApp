package com.example.marketapp.dto;

public class RegisterResponseDTO {
    private String uuid;
    private String mktKey;

    public RegisterResponseDTO() {

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMktKey() {
        return mktKey;
    }

    public void setMktKey(String mktKey) {
        this.mktKey = mktKey;
    }
}
