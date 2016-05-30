package com.aki.bustool.utils;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.*;

/**
 * Created by chunr on 2016/5/15.
 */


public class ParcelableMap implements Parcelable {



    private Map<String,Object> map;


    public ParcelableMap() {
        map = new HashMap<String,Object>();
    }


    protected ParcelableMap(Parcel in) {
    }

    public static final Creator<ParcelableMap> CREATOR = new Creator<ParcelableMap>() {
        @Override
        public ParcelableMap createFromParcel(Parcel in) {
            return new ParcelableMap(in);
        }

        @Override
        public ParcelableMap[] newArray(int size) {
            return new ParcelableMap[size];
        }
    };

    public Object get(Object key) {
        return map.get(key);
    }

    public void put(String key,Object value) {
        map.put(key,value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
