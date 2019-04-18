package com.data.nycschools.datamanager;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.databinding.ObservableList;

import com.data.nycschools.entity.SatScore;
import com.data.nycschools.entity.School;
import com.data.nycschools.ui.DataLoadCallback;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RepoManagerTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();
    @Mock
    RetrofitService retrofitService;
    @InjectMocks
    RepoManager repoManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }

    @Test
    public void testGetSchoolList_ThrowsError_ReturnsData() {
        DataLoadCallback callback = mock(DataLoadCallback.class);
        Throwable throwable = new Throwable("Exception");
        when(retrofitService.getSchoolList()).thenReturn(Observable.<List<School>>error(throwable));
        repoManager.getSchoolList(callback);
        verify(callback, atLeastOnce()).onDataLoadError(throwable);
        when(retrofitService.getSchoolList()).thenReturn(Observable.<List<School>>just(new ArrayList<School>()));
        repoManager.getSchoolList(callback);
        verify(callback).onDataLoaded(any());
    }

    @Test
    public void testLoadSatScoresFor_ThrowsError_ReturnsData() {
        String schoolId = "TEST123";
        DataLoadCallback callback = mock(DataLoadCallback.class);
        Throwable throwable = new Throwable("Exception");
        when(retrofitService.getSatScoreForSchoolId(schoolId)).thenReturn(Observable.<SatScore[]>error(throwable));
        repoManager.loadSatScoresFor(schoolId, callback);
        verify(callback, atLeastOnce()).onDataLoadError(throwable);
        when(retrofitService.getSatScoreForSchoolId(schoolId)).thenReturn(Observable.just(new SatScore[]{}));
        repoManager.loadSatScoresFor(schoolId, callback);
        verify(callback).onDataLoaded(any());
    }

    @After
    public void tearDown(){
        repoManager = null;
        retrofitService = null;
    }
}