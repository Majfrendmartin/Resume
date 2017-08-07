package com.wec.resume.presenter;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wec.resume.model.Section.SectionType;
import com.wec.resume.model.usecase.FetchSectionByTypeUsecase;
import com.wec.resume.view.DetailsActivityView;

public class DetailsActivityPresenterImpl extends AbstractPresenter<DetailsActivityView> implements DetailsActivityPresenter {

    private final FetchSectionByTypeUsecase fetchSectionByTypeUsecase;
    private SectionType sectionType;

    public DetailsActivityPresenterImpl(FetchSectionByTypeUsecase fetchSectionByTypeUsecase) {
        this.fetchSectionByTypeUsecase = fetchSectionByTypeUsecase;
    }

    @Override
    public void setSectionType(@NonNull SectionType sectionType) {
        this.sectionType = sectionType;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        fetchSectionByTypeUsecase.setType(sectionType);
        fetchSectionByTypeUsecase.execute()
                .subscribe(section -> {
                    if (isViewBounded()) {
                        getView().showSectionDetails(section.getTitle(), section.getCover());
                    }
                });
    }
}
