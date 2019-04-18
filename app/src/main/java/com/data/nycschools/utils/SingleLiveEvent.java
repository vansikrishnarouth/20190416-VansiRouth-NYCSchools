package com.data.nycschools.utils;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

/*
 * Single event mutable live data to prevent automatic
 * callbacks to change events. Only a setValue() or cal()
 * will trigger the change event.
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {

    String TAG = "SingleLiveEvent";
    private AtomicBoolean mPending = new AtomicBoolean(false);

    @MainThread
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer<T> observer) {
        if(hasActiveObservers()){
            Log.w(TAG, "Multiple observers found. but last one notified.");
        }
        super.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                if(mPending.compareAndSet(true, false)){
                    observer.onChanged(t);
                }
            }
        });
    }

    @MainThread
    @Override
    public void setValue(T value) {
        mPending.set(true);
        super.setValue(value);
    }

    @MainThread
    private void call(){
        setValue(null);
    }
}
