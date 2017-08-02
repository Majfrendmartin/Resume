package com.wec.resume.model.usecase;




import android.support.annotation.NonNull;

import com.wec.resume.model.Bio;
import com.wec.resume.model.repository.Repository;

import io.reactivex.Observable;

public class FetchBioUsecaseImpl implements FetchBioUsecase {

    private Repository repository;

    public FetchBioUsecaseImpl(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public Observable<Bio> execute() {
        return repository.getBio();
    }
}
