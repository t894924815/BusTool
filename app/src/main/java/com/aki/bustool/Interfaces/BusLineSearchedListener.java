package com.aki.bustool.Interfaces;

import com.aki.bustool.bean.ErrorStatus;
import com.amap.api.services.busline.BusLineItem;
import java.util.*;

/**
 * Created by chunr on 2016/5/26.
 */
public interface BusLineSearchedListener {
    public void getBusLineMessage(List<BusLineItem> busLineItems, ErrorStatus errorStatus);
    public void getManyBusLineMessage(List<List<BusLineItem>> busLineItemsList);
}
