package com.wec.resume.model;

import static com.wec.resume.model.Section.SectionType.JOBS;

/**
 * Created by Majfrendmartin on 2017-08-05.
 */

public class JobsSection extends Section<Job> {
    @Override
    public SectionType getType() {
        return JOBS;
    }
}
