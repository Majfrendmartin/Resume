package com.wec.resume.presenter;


import android.support.annotation.NonNull;

import com.wec.resume.model.BaseResumeItem;
import com.wec.resume.view.MainActivityFragmentView;

public interface MainActivityFragmentPresenter extends FragmentPresenter<MainActivityFragmentView> {
    void onSectionClicked(@NonNull BaseResumeItem.ResumeItemType baseResumeItem);
}
