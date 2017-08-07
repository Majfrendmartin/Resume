package com.wec.resume.model;

/**
 * Created by Majfrendmartin on 2017-08-05.
 */

public class JobsItem extends Section<Job> {
    @Override
    public SectionType getType() {
        return SectionType.JOBS;
    }
}
