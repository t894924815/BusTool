package com.aki.bustool.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chunr on 2016/5/25.
 */
public class ErrorStatus implements Parcelable{
    private boolean isError;
    private int returnCode;

    public ErrorStatus() {
    }

    public boolean getIsError() {
        return isError;
    }

    public ErrorStatus setIsError(boolean error) {
        isError = error;
        return this;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public ErrorStatus setReturnCode(int returnCode) {
        this.returnCode = returnCode;
        return this;
    }

    protected ErrorStatus(Parcel in) {
        isError = in.readByte() != 0;
        returnCode = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isError ? 1 : 0));
        dest.writeInt(returnCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ErrorStatus> CREATOR = new Creator<ErrorStatus>() {
        @Override
        public ErrorStatus createFromParcel(Parcel in) {
            return new ErrorStatus(in);
        }

        @Override
        public ErrorStatus[] newArray(int size) {
            return new ErrorStatus[size];
        }
    };
}
