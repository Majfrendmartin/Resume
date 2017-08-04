package com.wec.resume.presenter;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wec.resume.model.usecase.FetchBioUsecase;
import com.wec.resume.model.usecase.UpdateResumeUsecase;
import com.wec.resume.view.MainActivityView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenterImpl extends AbstractPresenter<MainActivityView> implements MainActivityPresenter {

    private static final String TITLE_PLACEHOLDER = "%s %s";
    private final UpdateResumeUsecase updateResumeUsecase;
    private final FetchBioUsecase fetchBioUsecase;
    private Disposable updateResumeDisposable;
    private Disposable fetchBioDisposable;

    public MainActivityPresenterImpl(FetchBioUsecase fetchBioUsecase, UpdateResumeUsecase updateResumeUsecase) {
        this.fetchBioUsecase = fetchBioUsecase;
        this.updateResumeUsecase = updateResumeUsecase;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        if (isViewBounded()) {
            getView().showSplashScreen();
        }

        updateResume();
    }

    private void updateResume() {
        if (updateResumeDisposable != null && !updateResumeDisposable.isDisposed()) {
            updateResumeDisposable.dispose();
        }

        updateResumeDisposable = updateResumeUsecase
                .execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resume -> {
                    if (isViewBounded()) {
                        getView().hideSplashScreen();
                    }
                    handleFetchBio();
                }, throwable -> {
                    if (isViewBounded()) {
                        getView().showCouldNoteLoadDataErrorMessage();
                    }
                });
    }

    private void handleFetchBio() {
        if (fetchBioDisposable != null && !fetchBioDisposable.isDisposed()) {
            fetchBioDisposable.dispose();
        }

        fetchBioDisposable = fetchBioUsecase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bio -> {
                    if (isViewBounded()) {
                        final MainActivityView view = getView();
                        view.setTitle(String.format(TITLE_PLACEHOLDER, bio.getName(), bio.getSurname()));
                        view.setAvatar(bio.getAvatar());
                    }
                });
    }
}
