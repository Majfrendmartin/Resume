package com.wec.resume.presenter;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wec.resume.model.usecase.FetchBioUsecase;
import com.wec.resume.view.MainActivityView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenterImpl extends AbstractPresenter<MainActivityView> implements MainActivityPresenter {

    public static final String TITLE_PLACEHOLDER = "%s %s";
    private FetchBioUsecase fetchBioUsecase;

    public MainActivityPresenterImpl(FetchBioUsecase fetchBioUsecase) {
        this.fetchBioUsecase = fetchBioUsecase;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        fetchBioUsecase.execute()
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
