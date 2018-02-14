package com.wec.resume;

import android.app.Application;
import android.support.annotation.NonNull;

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

    protected void setupInjector() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(getApplicationModule())
                    .build();
        }
    }

    @NonNull
    protected ApplicationModule getApplicationModule() {
        return new ApplicationModule(this);
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}

