package com.aki.bustool.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.text.SimpleDateFormat;


/**
 * Created by chunr on 2016/4/30.
 */
public class LocationMessage implements Parcelable{
    private int locationType;//获取当前定位结果来源，如网络定位结果，详见定位类型表
    private double latitude;//获取纬度
    private double longitude;//获取经度
    private float accuracy;//获取精度信息
    //                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(aMapLocation.getTime());
//                df.format(date);//定位时间
    private String date;
    private String address;//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
    private String country;//国家信息
    private String province;//省信息
    private String city;//城市信息
    private String district;//城区信息
    private String street;//街道信息
    private String streetNum;//街道门牌号信息
    private String cityCode;//城市编码
    private String AdCode;//地区编码
    private String aoiName;//获取当前定位点的AOI信息

    public LocationMessage() {
    }

    protected LocationMessage(Parcel in) {
        locationType = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        accuracy = in.readFloat();
        date = in.readString();
        address = in.readString();
        country = in.readString();
        province = in.readString();
        city = in.readString();
        district = in.readString();
        street = in.readString();
        streetNum = in.readString();
        cityCode = in.readString();
        AdCode = in.readString();
        aoiName = in.readString();
    }

    public static final Creator<LocationMessage> CREATOR = new Creator<LocationMessage>() {
        @Override
        public LocationMessage createFromParcel(Parcel in) {
            return new LocationMessage(in);
        }

        @Override
        public LocationMessage[] newArray(int size) {
            return new LocationMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(locationType);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeFloat(accuracy);
        parcel.writeString(date);
        parcel.writeString(address);
        parcel.writeString(country);
        parcel.writeString(province);
        parcel.writeString(city);
        parcel.writeString(district);
        parcel.writeString(street);
        parcel.writeString(streetNum);
        parcel.writeString(cityCode);
        parcel.writeString(AdCode);
        parcel.writeString(aoiName);
    }

    public LocationMessage setAoiName(String aoiName) {
        this.aoiName = aoiName;
        return this;
    }

    public LocationMessage setAdCode(String adCode) {
        AdCode = adCode;
        return this;
    }

    public LocationMessage setStreetNum(String streetNum) {
        this.streetNum = streetNum;
        return this;
    }

    public LocationMessage setDistrict(String district) {
        this.district = district;
        return this;
    }

    public LocationMessage setCountry(String country) {
        this.country = country;
        return this;
    }

    public LocationMessage setLocationType(int locationType) {
        this.locationType = locationType;
        return this;
    }

    public LocationMessage setDate(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        this.date = df.format(date);
        return this;
    }

    public LocationMessage setAddress(String address) {
        this.address = address;
        return this;
    }

    public LocationMessage setAccuracy(float accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public LocationMessage setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public LocationMessage setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public LocationMessage setProvince(String province) {
        this.province = province;
        return this;
    }

    public LocationMessage setCity(String city) {
        this.city = city;
        return this;
    }

    public LocationMessage setStreet(String street) {
        this.street = street;
        return this;
    }

    public int getLocationType() {
        return locationType;
    }

    public String getAdCode() {
        return AdCode;
    }

    public String getAoiName() {
        return aoiName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public String getStreet() {
        return street;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public LocationMessage setCityCode(String cityCode) {
        this.cityCode = cityCode;
        return this;
    }
}
