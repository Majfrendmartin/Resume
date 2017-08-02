package com.wec.resume.model.usecase;
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



import android.support.annotation.NonNull;

import com.wec.resume.model.Bio;
import com.wec.resume.model.repository.Repository;

import io.reactivex.Observable;

public class FetchBioUsecaseImpl implements FetchBioUsecase {

    private Repository repository;

    public FetchBioUsecaseImpl(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public Observable<Bio> execute() {
        return repository.getBio();
    }
}
