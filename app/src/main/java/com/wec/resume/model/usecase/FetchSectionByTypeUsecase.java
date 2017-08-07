package com.wec.resume.model.usecase;


import android.support.annotation.NonNull;

import com.wec.resume.model.Section;
import com.wec.resume.model.Section.SectionType;

public interface FetchSectionByTypeUsecase extends Usecase<Section> {
    void setType(@NonNull SectionType sectionType);
}
