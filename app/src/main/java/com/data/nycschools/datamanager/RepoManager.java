package com.data.nycschools.datamanager;

import com.data.nycschools.entity.SatScore;
import com.data.nycschools.entity.School;
import com.data.nycschools.ui.DataLoadCallback;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RepoManager {

    private RetrofitService retrofitService;

    public RepoManager(RetrofitService retrofitService){
        this.retrofitService = retrofitService;
    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void addDisposableObserver(Disposable disposable) {
        if(compositeDisposable.isDisposed())
            compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(disposable);
    }

    public void disposeSubscriptions() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void getSchoolList(final DataLoadCallback callback) {
        addDisposableObserver(retrofitService.getSchoolList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<School>>() {
                    @Override
                    public void accept(List<School> schools) throws Exception {
                        callback.onDataLoaded(schools);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callback.onDataLoadError(throwable);
                    }
                }));

    }

    public void loadSatScoresFor(String schoolId, final DataLoadCallback callback) {
        addDisposableObserver(retrofitService.getSatScoreForSchoolId(schoolId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SatScore[]>() {
                    @Override
                    public void accept(SatScore[] scores) throws Exception {
                        callback.onDataLoaded(scores);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callback.onDataLoadError(throwable);
                    }
                }));

    }
}
