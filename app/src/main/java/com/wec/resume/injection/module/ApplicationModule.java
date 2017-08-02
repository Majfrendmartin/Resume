package com.wec.resume.injection.module;



import android.app.Application;

import com.wec.resume.ResumeApplication;
import com.wec.resume.injection.scope.PerApplication;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final ResumeApplication application;

    public ApplicationModule(ResumeApplication application) {
        this.application = application;
    }

    @Provides
    @PerApplication
    public Application provideApplication() {
        return application;
    }

    @Provides
    @PerApplication
    public ResumeApplication provideResumeApplication() {
        return application;
    }


}
