package com.example.kikoano111.mpip.Api;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by kikoano111 on 23/6/2018.
 */

@Entity
public class GameDetail implements Parcelable {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @NonNull
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("first_release_date")
    @Expose
    private long date;

    @SerializedName("cover")
    @Expose
    @Embedded
    private Cover cover;

    @SerializedName("summary")
    @Expose
    private String summary;

    public GameDetail(){

    }
    protected GameDetail(Parcel in) {
        id = in.readString();
        name = in.readString();
        cover = in.readParcelable(Cover.class.getClassLoader());
        date = in.readLong();
        summary = in.readString();
    }

    public static final Creator<GameDetail> CREATOR = new Creator<GameDetail>() {
        @Override
        public GameDetail createFromParcel(Parcel in) {
            return new GameDetail(in);
        }

        @Override
        public GameDetail[] newArray(int size) {
            return new GameDetail[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeParcelable(cover,flags);
        dest.writeLong(date);
        dest.writeString(summary);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
