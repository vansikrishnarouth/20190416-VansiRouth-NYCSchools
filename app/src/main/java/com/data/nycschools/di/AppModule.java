package com.data.nycschools.di;


import com.data.nycschools.datamanager.RepoManager;
import com.data.nycschools.datamanager.RetrofitService;
import com.data.nycschools.utils.Constants;
import com.data.nycschools.utils.ViewModelFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Provides
    public Retrofit provideRetrofit(){
        return new Retrofit.Builder()
                .client(new OkHttpClient().newBuilder().build())
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    public RetrofitService provideRetrofitService(Retrofit retrofit){
        return retrofit.create(RetrofitService.class);
    }

    @Provides
    public RepoManager provideRepoManager(RetrofitService retrofitService){
        return new RepoManager(retrofitService);
    }

    @Provides
    public ViewModelFactory provideViewModelFactory(RepoManager repoManager){
        return new ViewModelFactory(repoManager);
    }
}
