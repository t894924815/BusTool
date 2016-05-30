package com.aki.bustool.Interfaces;

import com.aki.bustool.bean.ErrorStatus;
import com.aki.bustool.bean.LocationMessage;
import com.amap.api.services.core.LatLonPoint;

/**
 * Created by chunr on 2016/5/8.
 */
public interface GecoderTransfirmListener {
    public void onGetLat(LatLonPoint latLonPoint, ErrorStatus status);
    public void onGetAddr(String address, ErrorStatus status);
}
