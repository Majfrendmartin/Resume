package com.wec.resume.model;

/**
 * Created by Majfrendmartin on 2017-08-05.
 */

public class EducationItem extends BaseResumeItem<Education> {

    @Override
    public ResumeItemType getType() {
        return ResumeItemType.EDUCATION;
    }
}
