package com.wec.resume.presenter;


import com.wec.resume.model.Social.Type;
import com.wec.resume.view.MainActivityView;

public interface MainActivityPresenter extends Presenter<MainActivityView> {
    void socialsButtonClicked();

    void onButtonClicked(Type type);
}
