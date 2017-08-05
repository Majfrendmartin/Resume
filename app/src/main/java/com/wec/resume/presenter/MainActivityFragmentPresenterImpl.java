package com.wec.resume.presenter;


import android.os.Bundle;

import com.wec.resume.view.MainActivityFragmentView;

import java.util.Arrays;

public class MainActivityFragmentPresenterImpl extends AbstractFragmentPresenter<MainActivityFragmentView>
        implements MainActivityFragmentPresenter {

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        if (isViewBounded()) {
            getView().showList(Arrays.asList("AAA", "BBB"));
        }
    }
}
