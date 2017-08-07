package com.wec.resume.model;

/**
 * Created by Majfrendmartin on 2017-08-05.
 */

public class EducationItem extends Section<Education> {

    @Override
    public SectionType getType() {
        return SectionType.EDUCATION;
    }
}
