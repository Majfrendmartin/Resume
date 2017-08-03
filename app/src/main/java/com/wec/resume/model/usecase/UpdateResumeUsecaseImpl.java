package com.wec.resume.model.usecase;


import android.support.annotation.NonNull;

import com.wec.resume.model.Resume;
import com.wec.resume.model.repository.Repository;

import io.reactivex.Observable;

public class UpdateResumeUsecaseImpl implements UpdateResumeUsecase {

    private Repository repository;

    public UpdateResumeUsecaseImpl(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public Observable<Resume> execute() {
        return repository.loadUpdatedResume();
    }
}
