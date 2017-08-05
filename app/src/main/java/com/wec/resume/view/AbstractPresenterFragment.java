package com.wec.resume.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wec.resume.ResumeApplication;
import com.wec.resume.injection.component.ApplicationComponent;
import com.wec.resume.presenter.FragmentPresenter;

import javax.inject.Inject;

/**
 * Created by Majfrendmartin on 2017-08-05.
 */

public abstract class AbstractPresenterFragment<T extends FragmentPresenter<? extends View>> extends Fragment {
    @Inject
    T presenter;

    /**
     * Presenter call needs to be done after injection, that's why extra method is required.
     *
     * @param savedInstanceState
     */
    public void onCreateAfterInjection(@Nullable final Bundle savedInstanceState) {
        assert presenter != null;
        presenter.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @NonNull
    protected ApplicationComponent getApplicationComponent() {
        return ((ResumeApplication) getActivity().getApplication()).getApplicationComponent();
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final android.view.View view = super.onCreateView(inflater, container, savedInstanceState);
        presenter.onCreateView(savedInstanceState);
        return view;
    }
}
