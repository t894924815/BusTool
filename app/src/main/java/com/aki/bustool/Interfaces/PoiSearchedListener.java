package com.aki.bustool.Interfaces;

import com.aki.bustool.bean.ErrorStatus;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import java.util.*;

/**
 * Created by chunr on 2016/5/25.
 */
public interface PoiSearchedListener {
    public void onGetPoiMessage(List<PoiItem> poiItems, String poiTitle, LatLonPoint addressPoint, ErrorStatus status);
}
