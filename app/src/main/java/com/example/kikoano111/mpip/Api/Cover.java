package com.example.kikoano111.mpip.Api;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kikoano111 on 24/6/2018.
 */
@Entity
public class Cover implements Parcelable {
    @SerializedName("url")
    @Expose
    @PrimaryKey
    private String url;
    protected Cover(Parcel in) {
        url = in.readString();
    }
    public Cover(){

    }
    public static final Creator<Cover> CREATOR = new Creator<Cover>() {
        @Override
        public Cover createFromParcel(Parcel in) {
            return new Cover(in);
        }

        @Override
        public Cover[] newArray(int size) {
            return new Cover[size];
        }
    };

    public String getUrl() {
        return "https:"+url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }
}
