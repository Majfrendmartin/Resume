package com.wec.resume.presenter;

import android.os.Bundle;

import com.wec.resume.view.View;

/**
 * Created by Majfrendmartin on 2017-08-05.
 */

public interface FragmentPresenter<T extends View> extends Presenter<T> {
    void onViewCreated(Bundle savedInstanceState);

    void onCreateView(Bundle savedInstanceState);
}
