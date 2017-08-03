package com.wec.resume.injection.module;



import android.app.Application;
import android.content.SharedPreferences;

import com.wec.resume.injection.scope.PerApplication;
import com.wec.resume.model.repository.FakeRepositoryImpl;
import com.wec.resume.model.repository.NetworkAccessController;
import com.wec.resume.model.repository.NetworkAccessControllerImpl;
import com.wec.resume.model.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    @Provides
    @PerApplication
    public Repository provideRepository(Application application, SharedPreferences preferences, NetworkAccessController networkAccessController){
        return new FakeRepositoryImpl(application, preferences, networkAccessController);
    }

    @Provides
    @PerApplication
    public NetworkAccessController provideNetworkAccessController(Application application){
        return new NetworkAccessControllerImpl(application);
    }

}
