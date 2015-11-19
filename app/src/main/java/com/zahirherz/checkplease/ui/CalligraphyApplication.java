package com.zahirherz.checkplease.ui;

import android.app.Application;

import com.zahirherz.checkplease.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by zahirh on 11/13/15.
 */
public class CalligraphyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
