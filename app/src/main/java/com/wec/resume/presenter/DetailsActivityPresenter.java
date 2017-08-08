package com.wec.resume.presenter;


import android.support.annotation.NonNull;

import com.wec.resume.model.Section;
import com.wec.resume.view.DetailsActivityView;

public interface DetailsActivityPresenter extends Presenter<DetailsActivityView> {
    void setSectionType(@NonNull Section.SectionType sectionType);

    void homeButtonClicked();
}
