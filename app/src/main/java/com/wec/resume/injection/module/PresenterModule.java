package com.wec.resume.injection.module;



import com.wec.resume.injection.scope.PerActivity;
import com.wec.resume.model.usecase.FetchBioUsecase;
import com.wec.resume.presenter.MainActivityFragmentPresenter;
import com.wec.resume.presenter.MainActivityFragmentPresenterImpl;
import com.wec.resume.presenter.MainActivityPresenter;
import com.wec.resume.presenter.MainActivityPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    @PerActivity
    public MainActivityPresenter provideMainActivityPresenter(FetchBioUsecase fetchBioUsecase) {
        return new MainActivityPresenterImpl(fetchBioUsecase);
    }

    @Provides
    @PerActivity
    public MainActivityFragmentPresenter provideMainActivityFragmentPresenter() {
        return new MainActivityFragmentPresenterImpl();
    }
}
