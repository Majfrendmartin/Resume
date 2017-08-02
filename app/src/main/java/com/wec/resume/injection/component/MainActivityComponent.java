package com.wec.resume.injection.component;
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

import com.wec.resume.injection.module.PresenterModule;
import com.wec.resume.injection.module.UsecaseModule;
import com.wec.resume.injection.scope.PerActivity;
import com.wec.resume.view.MainActivity;
import com.wec.resume.view.MainActivityFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class,
        modules = {PresenterModule.class, UsecaseModule.class})
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);

    void inject(MainActivityFragment mainActivityFragment);
}
