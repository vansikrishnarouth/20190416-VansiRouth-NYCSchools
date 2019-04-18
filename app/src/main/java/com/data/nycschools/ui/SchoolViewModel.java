package com.data.nycschools.ui;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.data.nycschools.R;
import com.data.nycschools.datamanager.RepoManager;
import com.data.nycschools.entity.SatScore;
import com.data.nycschools.entity.School;
import com.data.nycschools.utils.SnackBarMessage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Singular viewmodel is used to store the Schools data and update the SAT
 * scores whenever user request for a detail. Both fragments use the same
 * dataset to render views.
 */
public class SchoolViewModel extends ViewModel {

    public ObservableBoolean loading = new ObservableBoolean(false);
    public ObservableBoolean scoreLoading = new ObservableBoolean(false);
    public ObservableBoolean empty = new ObservableBoolean(true);
    SnackBarMessage snackBarMessage = new SnackBarMessage();
    private RepoManager repoManager;
    public ObservableList<School> schools = new ObservableArrayList<>();

    public SchoolViewModel(RepoManager repoManager){
        this.repoManager = repoManager;
    }

    public void loadSchoolList() {
        loading.set(true);
        repoManager.getSchoolList(new DataLoadCallback(){

            @Override
            public void onDataLoaded(Object object) {
                loading.set(false);
                if(object instanceof List) {
                    List<School> data = (List<School>) object;
                    Collections.sort(data, new Comparator<School>() {
                        @Override
                        public int compare(School school1, School school2) {
                            return school1.getSchoolName().compareTo(school2.getSchoolName());
                        }
                    });
                    schools.clear();
                    schools.addAll(data);
                    if(schools.size() > 0){
                        empty.set(false);
                    }
                }
            }

            @Override
            public void onDataLoadError(Throwable th) {
                loading.set(false);
                snackBarMessage.setValue(R.string.msg_data_load_failed);
            }
        });
    }

    public School individualSchool(int position) {
        return schools.get(position);
    }

    public void loadSatScoresFor(final int position) {
        scoreLoading.set(true);
        repoManager.loadSatScoresFor(schools.get(position).dbn, new DataLoadCallback(){

            @Override
            public void onDataLoaded(Object object) {
                scoreLoading.set(false);
                if(object instanceof SatScore[]) {
                    schools.get(position).satScores = (SatScore[]) object;
                    schools.get(position).notifyChanges();
                }
            }

            @Override
            public void onDataLoadError(Throwable th) {
                scoreLoading.set(false);
                snackBarMessage.setValue(R.string.msg_scores_load_failed);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(repoManager != null)
            repoManager.disposeSubscriptions();
    }
}
