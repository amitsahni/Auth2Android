package com.auth;

import android.app.Application;
import android.content.Context;

import webconnect.com.webconnect.ApiConfiguration;

public class Configuration {
    private Context context;
    private boolean isDebug = true;

    public Configuration(Context context) {
        this.context = context;
    }

    public Configuration debug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    public void config() {
        new ApiConfiguration.Builder((Application) context.getApplicationContext())
                .debug(isDebug).config();
    }

}
