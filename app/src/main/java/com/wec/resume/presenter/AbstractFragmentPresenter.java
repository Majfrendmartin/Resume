package com.wec.resume.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wec.resume.view.View;

/**
 * Created by Majfrendmartin on 2017-08-05.
 */

public class AbstractFragmentPresenter<T extends View> implements FragmentPresenter<T> {

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
    public void bindView(T view) {
        this.view = view;
    }

    @Override
    public void unbindView() {
        view = null;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {

    }

    @Override
    public void onDestroy() {

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
    public void onSaveInstanceState(Bundle bundle) {

    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {

    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {

    }

    @Override
    public void onDestroyView() {

    }


}
