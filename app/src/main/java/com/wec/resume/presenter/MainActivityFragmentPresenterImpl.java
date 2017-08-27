package com.wec.resume.presenter;


import android.os.Bundle;
import android.support.annotation.NonNull;

import com.wec.resume.model.Section.SectionType;
import com.wec.resume.model.event.ResumeUpdatedEvent;
import com.wec.resume.model.repository.NetworkConnectionInfo;
import com.wec.resume.model.usecase.FetchBioUsecase;
import com.wec.resume.model.usecase.FetchSectionsUsecase;
import com.wec.resume.view.MainActivityFragmentView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityFragmentPresenterImpl extends AbstractFragmentPresenter<MainActivityFragmentView>
        implements MainActivityFragmentPresenter {

    private static final String TITLE_PLACEHOLDER = "%s %s";

    private final EventBus eventBus;
    private final FetchSectionsUsecase fetchSectionsUsecase;
    private final NetworkConnectionInfo networkConnectionInfo;
    private final FetchBioUsecase fetchBioUsecase;
    private Disposable fetchSectionsDisposable;
    private Disposable fetchBioDisposable;

    public MainActivityFragmentPresenterImpl(FetchSectionsUsecase fetchSectionsUsecase,
                                             EventBus eventBus,
                                             NetworkConnectionInfo networkConnectionInfo,
                                             FetchBioUsecase fetchBioUsecase) {
        this.fetchSectionsUsecase = fetchSectionsUsecase;
        this.eventBus = eventBus;
        this.networkConnectionInfo = networkConnectionInfo;
        this.fetchBioUsecase = fetchBioUsecase;
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }

        if (!networkConnectionInfo.hasInternetConnection()) {
            getView().showNoInternetConnectionInfo();
        }
    }

    private void cleanUpDisposable() {
        if (fetchSectionsDisposable != null && !fetchSectionsDisposable.isDisposed()) {
            fetchSectionsDisposable.dispose();
        }

        if (fetchBioDisposable != null && !fetchBioDisposable.isDisposed()) {
            fetchBioDisposable.dispose();
        }
    }

    @Override
    public void onDestroyView() {
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
        cleanUpDisposable();
        super.onDestroyView();
    }

    @Subscribe(sticky = true)
    public void onResumeUpdated(@NonNull ResumeUpdatedEvent resumeUpdatedEvent) {
        cleanUpDisposable();
        fetchSectionsDisposable = fetchSectionsUsecase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (isViewBounded()) {
                        getView().showList(result);
                    }
                }, Throwable::printStackTrace);

        fetchBioDisposable = fetchBioUsecase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (isViewBounded()) {
                        getView().showHeader(String.format(TITLE_PLACEHOLDER,
                                result.getName(), result.getSurname()), result.getDescription());
                    }
                }, Throwable::printStackTrace);
    }

    @Override
    public void onSectionClicked(@NonNull SectionType baseResumeItem, int position) {
        if (isViewBounded()) {
            getView().navigateToDetails(baseResumeItem, position);
        }
    }
}
