package com.wec.resume.view;


import android.support.annotation.NonNull;

import com.wec.resume.model.BaseResumeItem;
import com.wec.resume.model.BaseResumeItem.ResumeItemType;

import java.util.Collection;

public interface MainActivityFragmentView extends View {
    void showList(@NonNull Collection<BaseResumeItem> strings);

    void navigateToDetails(@NonNull ResumeItemType type);
}
