package com.wec.resume.presenter;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.wec.resume.model.BaseItem;
import com.wec.resume.model.ModalPair;
import com.wec.resume.model.Section.SectionType;
import com.wec.resume.model.usecase.FetchSectionByTypeUsecase;
import com.wec.resume.view.DetailsActivityFragmentView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsActivityFragmentPresenterImpl extends AbstractFragmentPresenter<DetailsActivityFragmentView> implements DetailsActivityFragmentPresenter {
    private final FetchSectionByTypeUsecase fetchSectionByTypeUsecase;
    private Disposable fetchByTypeDisposable;
    private SectionType type;
    private List<ModalPair<BaseItem, Boolean>> itemSelection;

    public DetailsActivityFragmentPresenterImpl(FetchSectionByTypeUsecase fetchSectionByTypeUsecase) {
        this.fetchSectionByTypeUsecase = fetchSectionByTypeUsecase;
    }

    @Override
    public void onItemClicked(int position) {
        if (isViewBounded()) {
            final boolean isPositionValid = position >= 0 && position < itemSelection.size();

            if (!isPositionValid) {
                return;
            }
            final ModalPair<BaseItem, Boolean> item = itemSelection.get(position);
            item.second = !item.second;
            getView().showItemDetails(position, item.second);
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
                .subscribe(section -> {
                    if (isViewBounded()) {
                        itemSelection =
                                (List<ModalPair<BaseItem, Boolean>>) Stream.of(section.getItems())
                                        .withoutNulls()
                                        .map(o -> ModalPair.create(o, false))
                                        .collect(Collectors.toList());
                        getView().showList(section.getTitle(), itemSelection, type.ordinal());
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
