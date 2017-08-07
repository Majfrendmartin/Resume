package com.wec.resume.injection.module;


import com.wec.resume.injection.scope.PerActivity;
import com.wec.resume.model.usecase.FetchBioUsecase;
import com.wec.resume.model.usecase.FetchSectionsUsecase;
import com.wec.resume.model.usecase.UpdateResumeUsecase;
import com.wec.resume.presenter.MainActivityFragmentPresenter;
import com.wec.resume.presenter.MainActivityFragmentPresenterImpl;
import com.wec.resume.presenter.MainActivityPresenter;
import com.wec.resume.presenter.MainActivityPresenterImpl;

import org.greenrobot.eventbus.EventBus;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    @PerActivity
    MainActivityPresenter provideMainActivityPresenter(FetchBioUsecase fetchBioUsecase,
                                                              UpdateResumeUsecase updateResumeUsecase) {
        return new MainActivityPresenterImpl(fetchBioUsecase, updateResumeUsecase);
    }

    @Provides
    @PerActivity
    MainActivityFragmentPresenter provideMainActivityFragmentPresenter(
            FetchSectionsUsecase fetchSectionsUsecase, EventBus eventBus) {
        return new MainActivityFragmentPresenterImpl(fetchSectionsUsecase, eventBus);
    }
}
