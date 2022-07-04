package com.ringolatechapps.ringonotes;

import java.util.Date;

public class dataModel {
    String mFileName;
    String mData;
    Date date;

    public dataModel(String mFileName, String mData, Date date) {
        this.mFileName = mFileName;
        this.mData = mData;
        this.date = date;
    }

    public String getmFileName() {
        return mFileName;
    }

    public String getmData() {
        return mData;
    }

    public Date getDate() {
        return date;
    }
}
