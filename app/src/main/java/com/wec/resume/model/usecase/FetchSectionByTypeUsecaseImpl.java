package com.wec.resume.model.usecase;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wec.resume.model.Section;
import com.wec.resume.model.Section.SectionType;
import com.wec.resume.model.repository.Repository;

import io.reactivex.Observable;

public class FetchSectionByTypeUsecaseImpl implements FetchSectionByTypeUsecase {

    @Nullable
    private SectionType sectionType;

    private Repository repository;

    public FetchSectionByTypeUsecaseImpl(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public Observable<Section> execute() {
        return sectionType == null ?
                Observable.error(new NullPointerException("Section type not provided")) :
                repository.getSectionByType(sectionType);
    }

    @Override
    public void setType(@NonNull SectionType sectionType) {
        this.sectionType = sectionType;
    }
}
