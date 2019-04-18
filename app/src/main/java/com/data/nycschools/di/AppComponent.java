package com.data.nycschools.di;

import com.data.nycschools.App;
import com.data.nycschools.ui.MainActivity;

import dagger.Component;

@Component(modules = {AppModule.class})
public interface AppComponent{

    void inject(App app);
    void inject(MainActivity mainActivity);
}
