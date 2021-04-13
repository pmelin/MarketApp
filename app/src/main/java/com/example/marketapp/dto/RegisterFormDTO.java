package com.example.marketapp.dto;

public class RegisterFormDTO extends RegisterRequestDTO {
    private String password;


    public RegisterFormDTO(String name, String nickName, String userPublicKey, String cardNumber, String cardExpirationDate, String cardCvv, String password) {
        super(name, nickName, userPublicKey, cardNumber, cardExpirationDate, cardCvv);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
