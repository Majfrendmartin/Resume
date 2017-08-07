package com.wec.resume.injection.module;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wec.resume.ResumeApplication;
import com.wec.resume.injection.scope.PerApplication;

import org.greenrobot.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
    private final ResumeApplication application;

    public ApplicationModule(ResumeApplication application) {
        this.application = application;
    }

    @Provides
    @PerApplication
    Application provideApplication() {
        return application;
    }

    @Provides
    @PerApplication
    ResumeApplication provideResumeApplication() {
        return application;
    }

    @Provides
    @PerApplication
    SharedPreferences provideSharedPreferences(Application application) {
        return application.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Provides
    @PerApplication
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @PerApplication
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("dd-MM-yyyy")
                .create();
    }
}
