package com.wec.resume.view;

import static com.wec.resume.model.Section.SectionType;

public interface DetailsActivityView extends View {
    void showSectionDetails(String cover, SectionType sectionType);

    void performUpNavigation();
}
