package com.data.nycschools.utils;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

public class SnackBarMessage extends SingleLiveEvent<Object> {

    public void observe(LifecycleOwner owner, final SnackBarObserver observer) {
        super.observe(owner, new Observer<Object>() {
            @Override
            public void onChanged(@Nullable Object message) {
                if(message == null)
                    return;
                if(message instanceof String)
                    observer.onNewMessage( (String) message);
                else
                    observer.onNewMessage((int) message);
            }
        });
    }

    //Snackbar messages could be plain strings or string resource ids
    public interface SnackBarObserver {
        void onNewMessage(@StringRes int snackBarMessageResourceId);

        void onNewMessage(String snackBarMessage);
    }
}
