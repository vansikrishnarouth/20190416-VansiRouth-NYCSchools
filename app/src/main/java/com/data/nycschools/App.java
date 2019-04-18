package com.data.nycschools;

import android.app.Application;

import com.data.nycschools.di.AppComponent;
import com.data.nycschools.di.AppModule;
import com.data.nycschools.di.DaggerAppComponent;

public class App extends Application {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDi();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void initDi() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule())
                .build();
    }
}
