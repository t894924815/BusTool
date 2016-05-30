package com.aki.bustool.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.services.core.LatLonPoint;

/**
 * Created by chunr on 2016/5/25.
 */
public class SimplePlaceMessage implements Parcelable{
    private LatLonPoint mLatLon;
    private String address;

    public SimplePlaceMessage() {
    }

    protected SimplePlaceMessage(Parcel in) {
        mLatLon = in.readParcelable(LatLonPoint.class.getClassLoader());
        address = in.readString();
    }

    public static final Creator<SimplePlaceMessage> CREATOR = new Creator<SimplePlaceMessage>() {
        @Override
        public SimplePlaceMessage createFromParcel(Parcel in) {
            return new SimplePlaceMessage(in);
        }

        @Override
        public SimplePlaceMessage[] newArray(int size) {
            return new SimplePlaceMessage[size];
        }
    };

    public LatLonPoint getmLatLon() {
        return mLatLon;
    }

    public SimplePlaceMessage setmLatLon(LatLonPoint mLatLon) {
        this.mLatLon = mLatLon;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public SimplePlaceMessage setAddress(String address) {
        this.address = address;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mLatLon, i);
        parcel.writeString(address);
    }
}
