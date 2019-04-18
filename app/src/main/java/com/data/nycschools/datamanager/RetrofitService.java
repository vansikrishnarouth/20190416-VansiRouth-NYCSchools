package com.data.nycschools.datamanager;

import com.data.nycschools.entity.SatScore;
import com.data.nycschools.entity.School;
import com.data.nycschools.utils.Constants;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET(Constants.ENDPOINT_SCHOOL_LIST)
    Observable<List<School>> getSchoolList();

    @GET(Constants.ENDPOINT_SAT_SCORE)
    Observable<SatScore[]> getSatScoreForSchoolId(@Query("dbn") String schoolId);
}
