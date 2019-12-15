package com.example.deteksi;

import android.os.Parcel;
import android.os.Parcelable;

public class detail implements Parcelable {
    private String packageName;
    private String realClass;
    private String detectClass;

    public detail(String packageName, String realClass, String detectClass){
        this.packageName = packageName;
        this.realClass = realClass;
        this.detectClass = detectClass;
    }

    public String getPackageName(){
        return packageName;
    }

    public String getRealClass(){
        return realClass;
    }

    public String getDetectClass(){
        return detectClass;
    }

    public detail(Parcel source) {
        packageName = source.readString();
        realClass = source.readString();
        detectClass = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(packageName);
        dest.writeString(realClass);
        dest.writeString(detectClass);
    }

    public static final Creator<detail> CREATOR = new Creator<detail>() {
        @Override
        public detail createFromParcel(Parcel source) {
            return new detail(source);
        }

        @Override
        public detail[] newArray(int size) {
            return new detail[size];
        }
    };
}
