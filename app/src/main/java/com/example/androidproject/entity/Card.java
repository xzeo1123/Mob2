package com.example.androidproject.entity;

public class Card {
    private int cardID;
    private String cardCode;
    private float value;

    public Card(int cardID, String cardCode, float value) {
        this.cardID = cardID;
        this.cardCode = cardCode;
        this.value = value;
    }

    public Card() {
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
