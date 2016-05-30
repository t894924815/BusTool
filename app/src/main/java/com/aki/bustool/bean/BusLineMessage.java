package com.aki.bustool.bean;

import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.core.LatLonPoint;
import java.util.*;

/**
 * Created by chunr on 2016/5/15.
 */
public class BusLineMessage {
    private String BusLineName;
    private String BusCompany;
    private String BusLineId;
    private String BusLineType;
    private String TerminalStation;
    private String OriginatingStation;
    private float BasicPrice;
    private List<LatLonPoint> Bounds;
    private List<LatLonPoint> DirectionsCoordinates;
    private List<BusStationItem> busStations;
    private String Distance;
    private Date FirstBusTime;
    private Date LastBusTime;
    private float TotalPrice;

    private List<String> DirectionsCoordinatesName;

    public BusLineMessage() {
    }

    public BusLineMessage setBusLineName(String busLineName) {
        BusLineName = busLineName;
        return this;
    }

    public BusLineMessage setBusStations(List<BusStationItem> busStations){
        this.busStations = busStations;
        return this;
    }

    public BusLineMessage setBusCompany(String busCompany) {
        BusCompany = busCompany;
        return this;
    }

    public BusLineMessage setBusLineId(String busLineId) {
        BusLineId = busLineId;
        return this;
    }

    public BusLineMessage setBusLineType(String busLineType) {
        BusLineType = busLineType;
        return this;
    }

    public BusLineMessage setTerminalStation(String terminalStation) {
        TerminalStation = terminalStation;
        return this;
    }

    public BusLineMessage setOriginatingStation(String originatingStation) {
        OriginatingStation = originatingStation;
        return this;
    }

    public BusLineMessage setBasicPrice(float basicPrice) {
        BasicPrice = basicPrice;
        return this;
    }

    public BusLineMessage setBounds(List<LatLonPoint> bounds) {
        Bounds = bounds;
        return this;
    }

    public BusLineMessage setDirectionsCoordinates(List<LatLonPoint> directionsCoordinates) {
        DirectionsCoordinates = directionsCoordinates;
        return this;
    }

    public BusLineMessage setDistance(String distance) {
        Distance = distance;
        return this;
    }

    public BusLineMessage setFirstBusTime(Date firstBusTime) {
        FirstBusTime = firstBusTime;
        return this;
    }

    public BusLineMessage setLastBusTime(Date lastBusTime) {
        LastBusTime = lastBusTime;
        return this;
    }

    public BusLineMessage setTotalPrice(float totalPrice) {
        TotalPrice = totalPrice;
        return this;
    }

    public float getTotalPrice() {
        return TotalPrice;
    }

    public List<BusStationItem> getBusStations() {
        return busStations;
    }

    public String getBusLineType() {
        return BusLineType;
    }

    public String getBusLineName() {
        return BusLineName;
    }

    public String getBusCompany() {
        return BusCompany;
    }

    public String getBusLineId() {
        return BusLineId;
    }

    public float getBasicPrice() {
        return BasicPrice;
    }

    public String getOriginatingStation() {
        return OriginatingStation;
    }

    public String getTerminalStation() {
        return TerminalStation;
    }

    public List<LatLonPoint> getBounds() {
        return Bounds;
    }

    public List<LatLonPoint> getDirectionsCoordinates() {
        return DirectionsCoordinates;
    }

    public String getDistance() {
        return Distance;
    }

    public Date getFirstBusTime() {
        return FirstBusTime;
    }

    public Date getLastBusTime() {
        return LastBusTime;
    }

    public List<String> getDirectionsCoordinatesName() {
        return DirectionsCoordinatesName;
    }
}
