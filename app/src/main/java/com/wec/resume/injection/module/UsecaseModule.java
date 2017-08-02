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
import com.wec.resume.model.repository.Repository;
import com.wec.resume.model.usecase.FetchBioUsecase;
import com.wec.resume.model.usecase.FetchBioUsecaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class UsecaseModule {
    @Provides
    @PerActivity
    public FetchBioUsecase provideUpdateItemQuantityUsecase(Repository repository) {
        return new FetchBioUsecaseImpl(repository);
    }
}
