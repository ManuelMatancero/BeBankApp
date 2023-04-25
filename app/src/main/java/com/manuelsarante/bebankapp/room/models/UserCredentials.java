package com.manuelsarante.bebankapp.room.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserCredentials {
    @PrimaryKey(autoGenerate = true)
    public int idUserCredentials;

    @ColumnInfo(name = "user")
    public String user;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "pin")
    public String pin;

    public int getIdUserCredentials() {
        return idUserCredentials;
    }

    public void setIdUserCredentials(int idUserCredentials) {
        this.idUserCredentials = idUserCredentials;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

}
