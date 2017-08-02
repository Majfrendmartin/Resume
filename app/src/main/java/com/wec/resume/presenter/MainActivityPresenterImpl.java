package com.wec.resume.presenter;
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

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wec.resume.model.usecase.FetchBioUsecase;
import com.wec.resume.view.MainActivityView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenterImpl extends AbstractPresenter<MainActivityView> implements MainActivityPresenter {

    public static final String TITLE_PLACEHOLDER = "%s %s";
    private FetchBioUsecase fetchBioUsecase;

    public MainActivityPresenterImpl(FetchBioUsecase fetchBioUsecase) {
        this.fetchBioUsecase = fetchBioUsecase;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        fetchBioUsecase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bio -> {
                    if (isViewBounded()) {
                        final MainActivityView view = getView();
                        view.setTitle(String.format(TITLE_PLACEHOLDER, bio.getName(), bio.getSurname()));
                        view.setAvatar(bio.getAvatar());
                    }
                });
    }
}
