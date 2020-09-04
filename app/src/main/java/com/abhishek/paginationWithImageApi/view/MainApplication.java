package com.abhishek.paginationWithImageApi.view;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

/**
 * Created by Abhishek Garg on 04/09/20 - https://www.linkedin.com/in/abhishekgarg727/
 */
public class MainApplication extends MultiDexApplication {


    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }
}
