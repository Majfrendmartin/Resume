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


import com.wec.resume.injection.scope.PerActivity;
import com.wec.resume.model.usecase.FetchBioUsecase;
import com.wec.resume.presenter.MainActivityFragmentPresenter;
import com.wec.resume.presenter.MainActivityFragmentPresenterImpl;
import com.wec.resume.presenter.MainActivityPresenter;
import com.wec.resume.presenter.MainActivityPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    @PerActivity
    public MainActivityPresenter provideMainActivityPresenter(FetchBioUsecase fetchBioUsecase) {
        return new MainActivityPresenterImpl(fetchBioUsecase);
    }

    @Provides
    @PerActivity
    public MainActivityFragmentPresenter provideMainActivityFragmentPresenter() {
        return new MainActivityFragmentPresenterImpl();
    }
}
