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

import com.wec.resume.view.View;

public abstract class AbstractPresenter<T extends View> implements Presenter <T> {
    private T view;

    @Override
    public T getView() {
        return view;
    }

    @Override
    public boolean isViewBounded() {
        return view != null;
    }

    @Override
    public void bindView(final T view) {
        this.view = view;
    }

    @Override
    public void onCreate(@Nullable final Bundle bundle) {

    }

    @Override
    public void unbindView() {
        view = null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onSaveInstanceState(final Bundle bundle) {

    }

    @Override
    public void onDestroy() {

    }
}
