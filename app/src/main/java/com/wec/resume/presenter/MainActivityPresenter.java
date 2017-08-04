package com.wec.resume.presenter;


import com.wec.resume.view.MainActivityView;

public interface MainActivityPresenter extends Presenter<MainActivityView> {
    void socialsButtonClicked();

    void githubButtonClicked();

    void linkedInButtonClicked();
}
