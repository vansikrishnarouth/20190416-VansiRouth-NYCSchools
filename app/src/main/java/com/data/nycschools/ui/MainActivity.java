package com.data.nycschools.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.data.nycschools.App;
import com.data.nycschools.R;
import com.data.nycschools.utils.Constants;
import com.data.nycschools.utils.SnackBarMessage;
import com.data.nycschools.utils.Utils;
import com.data.nycschools.utils.ViewModelFactory;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements
        SchoolListFragment.FragmentInteractionListener,
        SchoolDetailFragment.FragmentInteractionListener {

    SchoolViewModel viewModel;
    @Inject
    ViewModelFactory viewModelFactory;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.contentLayout);
        inject();
        viewModel = obtainViewModel();
        setupSnackBar();
        loadParentFragment();
    }

    private void inject() {
        ((App) getApplication()).getAppComponent().inject(this);
    }

    private SchoolViewModel obtainViewModel() {
        return ViewModelProviders.of(this, viewModelFactory).get(SchoolViewModel.class);
    }

    private void loadParentFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentLayout, new SchoolListFragment(),  Constants.TAG_LIST)
                .addToBackStack(Constants.TAG_LIST)
                .commit();
    }

    private void setupSnackBar(){
        viewModel.snackBarMessage.observe(this, new SnackBarMessage.SnackBarObserver() {
            @Override
            public void onNewMessage(int snackBarMessageResourceId) {
                if(view != null){
                    Utils.showShortSnack(view, getString(snackBarMessageResourceId));
                }
            }

            @Override
            public void onNewMessage(String snackBarMessage) {
                if(view != null){
                    Utils.showShortSnack(view, snackBarMessage);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 1)
            finish();
        else
            getSupportFragmentManager().popBackStack();
    }

    @Override
    public void openSchoolDetail(int position) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_left, R.anim.hold_up)
                .add(R.id.contentLayout, SchoolDetailFragment.newInstance(position), Constants.TAG_DETAIL)
                .addToBackStack(Constants.TAG_DETAIL)
                .commit();
    }

    @Override
    public SchoolViewModel getViewModel() {
        return viewModel;
    }

}
