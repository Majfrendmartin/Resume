package com.wec.resume.injection.module;



import com.wec.resume.injection.scope.PerApplication;
import com.wec.resume.model.repository.FakeRepositoryImpl;
import com.wec.resume.model.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    @Provides
    @PerApplication
    public Repository provideRepository(){
        return new FakeRepositoryImpl();
    }

}
