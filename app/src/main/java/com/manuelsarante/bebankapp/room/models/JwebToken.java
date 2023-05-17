package com.manuelsarante.bebankapp.room.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class JwebToken {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idJsonWebToken")
    public int idJsonWebToken;
    @ColumnInfo(name = "jsonWebToken")
    public String jsonWebToken;

    @ColumnInfo(name="expirationDate")
    public String expirationDate;

    public int getIdJsonWebToken() {
        return idJsonWebToken;
    }

    public void setIdJsonWebToken(int idJsonWebToken) {
        this.idJsonWebToken = idJsonWebToken;
    }

    public String getJsonWebToken() {
        return jsonWebToken;
    }

    public void setJsonWebToken(String jsonWebToken) {
        this.jsonWebToken = jsonWebToken;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
