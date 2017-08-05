package com.wec.resume.model.usecase;

import android.support.annotation.NonNull;

import com.wec.resume.model.BaseResumeItem;
import com.wec.resume.model.repository.Repository;

import java.util.Collection;

import io.reactivex.Observable;

/**
 * Created by Majfrendmartin on 2017-08-05.
 */

public class FetchSectionsUsecaseImpl implements FetchSectionsUsecase {

    private final Repository repository;

    public FetchSectionsUsecaseImpl(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public Observable<Collection<BaseResumeItem>> execute() {
        return repository.getSections();
    }
}
