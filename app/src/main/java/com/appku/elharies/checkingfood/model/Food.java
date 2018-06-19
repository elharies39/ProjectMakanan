package com.appku.elharies.checkingfood.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by El Haries on 6/6/2018.
 */
@IgnoreExtraProperties
public class Food implements Serializable, Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.namaMakanan);
        dest.writeString(this.key);
        dest.writeString(this.tglKadaluarsa);
    }

    protected Food(Parcel in) {
        this.namaMakanan = in.readString();
        this.key = in.readString();
        this.tglKadaluarsa = in.readString();
    }

    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel source) {
            return new Food(source);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}
