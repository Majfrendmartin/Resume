package com.wec.resume.presenter;


import android.os.Bundle;

import com.wec.resume.model.event.ResumeUpdatedEvent;
import com.wec.resume.model.usecase.FetchSectionsUsecase;
import com.wec.resume.view.MainActivityFragmentView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityFragmentPresenterImpl extends AbstractFragmentPresenter<MainActivityFragmentView>
        implements MainActivityFragmentPresenter {

    private final EventBus eventBus;
    private final FetchSectionsUsecase fetchSectionsUsecase;
    private Disposable fetchSectionsDisposable;

    public MainActivityFragmentPresenterImpl(FetchSectionsUsecase fetchSectionsUsecase, EventBus eventBus) {
        this.fetchSectionsUsecase = fetchSectionsUsecase;
        this.eventBus = eventBus;
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void cleanUpDisposable() {
        if (fetchSectionsDisposable != null && !fetchSectionsDisposable.isDisposed()) {
            fetchSectionsDisposable.dispose();
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
    public void onResumeUpdated(ResumeUpdatedEvent resumeUpdatedEvent) {
        cleanUpDisposable();

        fetchSectionsDisposable = fetchSectionsUsecase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        baseResumeItems -> {
                            if (isViewBounded()) {
                                getView().showList(baseResumeItems);
                            }
                        }, Throwable::printStackTrace);
    }
}
