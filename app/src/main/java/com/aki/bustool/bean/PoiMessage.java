package com.aki.bustool.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.poisearch.IndoorData;
import com.amap.api.services.poisearch.SubPoiItem;

import java.util.List;

/**
 * Created by chunr on 2016/5/13.
 */
public class PoiMessage implements Parcelable{


    private String businessArea;
    private String adName;
    private String cityName;
    private String provinceName;
    private String typeDes;
    private String tel;
    private String adCode;
    private String poiId;
    private int distance;
    private String title;
    private String snippet;
    private LatLonPoint latLonPoint;
    private String cityCode;
    private LatLonPoint enter;
    private LatLonPoint exit;
    private String website;
    private String postcode;
    private String email;
    private String direction;
    private boolean isIndoorMap;
    private String provinceCode;
    private String parkingType;
    private List<SubPoiItem> subPois;
    private IndoorData indoorData;


    public PoiMessage() {
    }

    protected PoiMessage(Parcel in) {
        businessArea = in.readString();
        adName = in.readString();
        cityName = in.readString();
        provinceName = in.readString();
        typeDes = in.readString();
        tel = in.readString();
        adCode = in.readString();
        poiId = in.readString();
        distance = in.readInt();
        title = in.readString();
        snippet = in.readString();
        latLonPoint = in.readParcelable(LatLonPoint.class.getClassLoader());
        cityCode = in.readString();
        enter = in.readParcelable(LatLonPoint.class.getClassLoader());
        exit = in.readParcelable(LatLonPoint.class.getClassLoader());
        website = in.readString();
        postcode = in.readString();
        email = in.readString();
        direction = in.readString();
        isIndoorMap = in.readByte() != 0;
        provinceCode = in.readString();
        parkingType = in.readString();
        subPois = in.createTypedArrayList(SubPoiItem.CREATOR);
        indoorData = in.readParcelable(IndoorData.class.getClassLoader());
    }

    public static final Creator<PoiMessage> CREATOR = new Creator<PoiMessage>() {
        @Override
        public PoiMessage createFromParcel(Parcel in) {
            return new PoiMessage(in);
        }

        @Override
        public PoiMessage[] newArray(int size) {
            return new PoiMessage[size];
        }
    };

    public String getBusinessArea() {
        return businessArea;
    }

    public PoiMessage setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
        return this;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public PoiMessage setProvinceName(String provinceName) {
        this.provinceName = provinceName;
        return this;
    }

    public String getAdName() {
        return adName;
    }

    public PoiMessage setAdName(String adName) {
        this.adName = adName;
        return this;
    }

    public String getCityName() {
        return cityName;
    }

    public PoiMessage setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public String getTypeDes() {
        return typeDes;
    }

    public PoiMessage setTypeDes(String typeDes) {
        this.typeDes = typeDes;
        return this;
    }

    public String getTel() {
        return tel;
    }

    public PoiMessage setTel(String tel) {
        this.tel = tel;
        return this;
    }

    public String getAdCode() {
        return adCode;
    }

    public PoiMessage setAdCode(String adCode) {
        this.adCode = adCode;
        return this;
    }

    public String getPoiId() {
        return poiId;
    }

    public PoiMessage setPoiId(String poiId) {
        this.poiId = poiId;
        return this;
    }

    public int getDistance() {
        return distance;
    }

    public PoiMessage setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PoiMessage setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSnippet() {
        return snippet;
    }

    public PoiMessage setSnippet(String snippet) {
        this.snippet = snippet;
        return this;
    }

    public LatLonPoint getLatLonPoint() {
        return latLonPoint;
    }

    public PoiMessage setLatLonPoint(LatLonPoint latLonPoint) {
        this.latLonPoint = latLonPoint;
        return this;
    }

    public LatLonPoint getEnter() {
        return enter;
    }

    public PoiMessage setEnter(LatLonPoint enter) {
        this.enter = enter;
        return this;
    }

    public String getCityCode() {
        return cityCode;
    }

    public PoiMessage setCityCode(String cityCode) {
        this.cityCode = cityCode;
        return this;
    }

    public LatLonPoint getExit() {
        return exit;
    }

    public PoiMessage setExit(LatLonPoint exit) {
        this.exit = exit;
        return this;
    }

    public String getWebsite() {
        return website;
    }

    public PoiMessage setWebsite(String website) {
        this.website = website;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PoiMessage setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPostcode() {
        return postcode;
    }

    public PoiMessage setPostcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public PoiMessage setDirection(String direction) {
        this.direction = direction;
        return this;
    }

    public boolean isIndoorMap() {
        return isIndoorMap;
    }

    public PoiMessage setIndoorMap(boolean indoorMap) {
        isIndoorMap = indoorMap;
        return this;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public PoiMessage setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
        return this;
    }

    public String getParkingType() {
        return parkingType;
    }

    public PoiMessage setParkingType(String parkingType) {
        this.parkingType = parkingType;
        return this;
    }

    public List<SubPoiItem> getSubPois() {
        return subPois;
    }

    public PoiMessage setSubPois(List<SubPoiItem> subPois) {
        this.subPois = subPois;
        return this;
    }

    public IndoorData getIndoorData() {
        return indoorData;
    }

    public PoiMessage setIndoorData(IndoorData indoorData) {
        this.indoorData = indoorData;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(businessArea);
        parcel.writeString(adName);
        parcel.writeString(cityName);
        parcel.writeString(provinceName);
        parcel.writeString(typeDes);
        parcel.writeString(tel);
        parcel.writeString(adCode);
        parcel.writeString(poiId);
        parcel.writeInt(distance);
        parcel.writeString(title);
        parcel.writeString(snippet);
        parcel.writeParcelable(latLonPoint, i);
        parcel.writeString(cityCode);
        parcel.writeParcelable(enter, i);
        parcel.writeParcelable(exit, i);
        parcel.writeString(website);
        parcel.writeString(postcode);
        parcel.writeString(email);
        parcel.writeString(direction);
        parcel.writeByte((byte) (isIndoorMap ? 1 : 0));
        parcel.writeString(provinceCode);
        parcel.writeString(parkingType);
        parcel.writeTypedList(subPois);
        parcel.writeParcelable(indoorData, i);
    }
}
