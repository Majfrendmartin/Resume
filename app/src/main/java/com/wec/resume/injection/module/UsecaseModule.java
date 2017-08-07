package com.wec.resume.injection.module;


import com.wec.resume.injection.scope.PerActivity;
import com.wec.resume.model.repository.Repository;
import com.wec.resume.model.usecase.FetchBioUsecase;
import com.wec.resume.model.usecase.FetchBioUsecaseImpl;
import com.wec.resume.model.usecase.FetchSectionsUsecase;
import com.wec.resume.model.usecase.FetchSectionsUsecaseImpl;
import com.wec.resume.model.usecase.UpdateResumeUsecase;
import com.wec.resume.model.usecase.UpdateResumeUsecaseImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class UsecaseModule {

    @Provides
    @PerActivity
    FetchBioUsecase provideUpdateItemQuantityUsecase(Repository repository) {
        return new FetchBioUsecaseImpl(repository);
    }

    @Provides
    @PerActivity
    UpdateResumeUsecase provideUpdateResumeUsecase(Repository repository) {
        return new UpdateResumeUsecaseImpl(repository);
    }

    @Provides
    @PerActivity
    FetchSectionsUsecase provideFetchSectionsUsecase(Repository repository) {
        return new FetchSectionsUsecaseImpl(repository);
    }
}
