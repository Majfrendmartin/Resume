package com.wec.resume.view;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wec.resume.ResumeApplication;
import com.wec.resume.injection.component.ApplicationComponent;
import com.wec.resume.presenter.Presenter;

import javax.inject.Inject;

public abstract class AbstractPresenterActivity<T extends Presenter<? extends View>> extends AppCompatActivity {
    @Inject
    T presenter;

    /**
     * Presenter call needs to be done after injection, that's why extra method is required.
     *
     * @param savedInstanceState
     */
    protected void onCreateAfterInjection(@Nullable final Bundle savedInstanceState) {
        assert presenter != null;
        presenter.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        presenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @NonNull
    protected ApplicationComponent getApplicationComponent() {
        return ((ResumeApplication) getApplication()).getApplicationComponent();
    }
}
