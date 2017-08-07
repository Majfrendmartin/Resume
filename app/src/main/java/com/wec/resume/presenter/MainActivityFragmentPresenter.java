package com.wec.resume.presenter;


import android.support.annotation.NonNull;

import com.wec.resume.model.Section;
import com.wec.resume.view.MainActivityFragmentView;

public interface MainActivityFragmentPresenter extends FragmentPresenter<MainActivityFragmentView> {
    void onSectionClicked(@NonNull Section.SectionType baseResumeItem, int position);
}
