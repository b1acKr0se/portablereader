package com.framgia.nguyenthanhhai.portablereader.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class App extends Application {
    private RefWatcher mRefWatcher;

    @Override public void onCreate() {
        super.onCreate();
        mRefWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        App app = (App) context.getApplicationContext();
        return app.mRefWatcher;
    }
}
