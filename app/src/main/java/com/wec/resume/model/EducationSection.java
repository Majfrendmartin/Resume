package com.wec.resume.model;

import static com.wec.resume.model.Section.SectionType.EDUCATION;

/**
 * Created by Majfrendmartin on 2017-08-05.
 */

public class EducationSection extends Section<Education> {

    @Override
    public SectionType getType() {
        return EDUCATION;
    }
}
