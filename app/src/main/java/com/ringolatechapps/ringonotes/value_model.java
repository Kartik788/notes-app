package com.ringolatechapps.ringonotes;

import java.util.Date;

public class value_model {
    String mdata;
    Date mDate;

    public value_model(String mdata, Date mDate) {
        this.mdata = mdata;
        this.mDate = mDate;
    }

    public String getMdata() {
        return mdata;
    }

    public Date getmDate() {
        return mDate;
    }
}
