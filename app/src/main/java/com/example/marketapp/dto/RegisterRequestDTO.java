package com.example.marketapp.dto;

import java.io.Serializable;

public class RegisterRequestDTO implements Serializable {
    private String name;
    private String nickName;
    private String userPublicKey;
    private String cardNumber;
    private String cardExpirationDate;
    private String cardCvv;

    public RegisterRequestDTO(String name, String nickName, String userPublicKey,
                              String cardNumber, String cardExpirationDate, String cardCvv) {
        this.name = name;
        this.nickName = nickName;
        this.userPublicKey = userPublicKey;
        this.cardNumber = cardNumber;
        this.cardExpirationDate = cardExpirationDate;
        this.cardCvv = cardCvv;
    }

    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    public String getUserPublicKey() {
        return userPublicKey;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardExpirationDate() {
        return cardExpirationDate;
    }

    public String getCardCvv() {
        return cardCvv;
    }
}
