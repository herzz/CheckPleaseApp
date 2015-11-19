package com.zahirherz.checkplease.taxinfo;

import android.util.Log;

public class Tax {
    private double mRate;
    private static final String TAG = Tax.class.getSimpleName();
    public double getRate() {
        Log.d(TAG, mRate + "");
        return mRate;
    }

    public void setRate(double rate) {
        mRate = rate/100;
    }

}
