package com.data.nycschools.utils;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.data.nycschools.datamanager.RepoManager;
import com.data.nycschools.ui.SchoolViewModel;


public class ViewModelFactory implements ViewModelProvider.Factory{

    private RepoManager repoManager;

    public ViewModelFactory(RepoManager repoManager) {
        this.repoManager = repoManager;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(SchoolViewModel.class))
            return (T) new SchoolViewModel(repoManager);

        throw new IllegalArgumentException("Class type is not one of the listed ones.");
    }
}
