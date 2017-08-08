package com.wec.resume.presenter;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wec.resume.model.Section;
import com.wec.resume.model.Section.SectionType;
import com.wec.resume.model.usecase.FetchSectionByTypeUsecase;
import com.wec.resume.view.DetailsActivityFragmentView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsActivityFragmentPresenterImpl extends AbstractFragmentPresenter<DetailsActivityFragmentView> implements DetailsActivityFragmentPresenter {
    private final FetchSectionByTypeUsecase fetchSectionByTypeUsecase;
    private Disposable fetchByTypeDisposable;
    private SectionType type;

    public DetailsActivityFragmentPresenterImpl(FetchSectionByTypeUsecase fetchSectionByTypeUsecase) {
        this.fetchSectionByTypeUsecase = fetchSectionByTypeUsecase;
    }

    @Override
    public void onItemClicked(int position) {
        if (isViewBounded()) {
            getView().showItemDetails(position);
        }
    }

    @Override
    public void setType(SectionType type) {
        this.type = type;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        cleanupDisposable();

        fetchSectionByTypeUsecase.setType(type);
        fetchByTypeDisposable = fetchSectionByTypeUsecase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Section::getItems)
                .subscribe(items -> {
                    if (isViewBounded()) {
                        getView().showList(items, type.ordinal());
                    }
                });

    }

    @Override
    public void onDestroy() {
        cleanupDisposable();
        super.onDestroy();
    }

    private void cleanupDisposable() {
        if (fetchByTypeDisposable != null && !fetchByTypeDisposable.isDisposed()) {
            fetchByTypeDisposable.dispose();
        }
    }
}
