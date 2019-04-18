package com.data.nycschools.ui;


public interface DataLoadCallback {

    void onDataLoaded(Object object);
    void onDataLoadError(Throwable th);
}
