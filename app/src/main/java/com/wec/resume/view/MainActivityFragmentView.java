package com.wec.resume.view;


import android.support.annotation.NonNull;

import com.wec.resume.model.Section;
import com.wec.resume.model.Section.SectionType;

import java.util.Collection;

public interface MainActivityFragmentView extends View {
    void showList(@NonNull Collection<Section> sections);

    void navigateToDetails(@NonNull SectionType type, int position);

    void showNoInternetConnectionInfo();

    void showHeader(String title, String subtitle);
}
