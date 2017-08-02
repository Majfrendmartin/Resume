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

public interface Presenter<T extends View> {
    T getView();

    boolean isViewBounded();

    void bindView(final T view);

    void unbindView();

    void onCreate(@Nullable Bundle bundle);

    void onDestroy();

    void onStart();

    void onStop();

    void onResume();

    void onPause();

    void onSaveInstanceState(final Bundle bundle);
}
