package com.manuelsarante.bebankapp.dto;

public class Messages {

    private String message;

    public Messages(){};

    public Messages(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
