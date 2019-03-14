package com.samalex.slucapstone;

import android.app.Application;

public class BoozymeterApplication extends Application {
    private boolean isDebug = false;

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

}
