package com.aki.bustool.utils;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chunr on 2016/5/15.
 */


public class SerializableMap implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String,Object> map;


    public SerializableMap() {
        map = new HashMap<String,Object>();
    }


    public Object get(Object key) {
        return map.get(key);
    }

    public void put(String key,Object value) {
        map.put(key,value);
    }

}
