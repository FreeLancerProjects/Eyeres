package com.appzone.eyeres.share;

import android.app.Application;
import android.content.Context;

import com.appzone.eyeres.local_manager.LocalManager;

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalManager.updateResources(base,LocalManager.getLanguage(base)));


    }
}
