package com.wec.resume.injection.module;
/*
 * Avaya Inc. - Proprietary (Restricted)
 * Solely for authorized persons having a need to know
 * pursuant to Company instructions.
 *
 * Copyright 2017. Avaya Inc. All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc.
 * The copyright notice above does not evidence any actual
 * or intended publication of such source code.
 */


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
