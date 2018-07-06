package com.appku.elharies.checkingfood.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by El Haries on 6/6/2018.
 */
@IgnoreExtraProperties
public class Food implements Serializable {
    private String namaMakanan;
    private String key;
    private String tglKadaluarsa;

    public String getTglKadaluarsa() {
        return tglKadaluarsa;
    }

    public void setTglKadaluarsa(String tglKadaluarsa) {
        this.tglKadaluarsa = tglKadaluarsa;
    }

    public Food(String namaMakanan, String tglKadaluarsa) {

        this.namaMakanan = namaMakanan;
        this.tglKadaluarsa = tglKadaluarsa;
    }

    public Food() {
    }

    public String getNamaMakanan() {
        return namaMakanan;
    }

    public void setNamaMakanan(String namaMakanan) {
        this.namaMakanan = namaMakanan;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Food{" +
                "namaMakanan='" + namaMakanan + '\'' +
                '}';
    }
}
