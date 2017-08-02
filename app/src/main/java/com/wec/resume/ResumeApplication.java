package com.wec.resume;

import android.app.Application;

import com.wec.resume.injection.component.ApplicationComponent;
import com.wec.resume.injection.component.DaggerApplicationComponent;
import com.wec.resume.injection.module.ApplicationModule;


public class ResumeApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setupInjector();
    }

    private void setupInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
