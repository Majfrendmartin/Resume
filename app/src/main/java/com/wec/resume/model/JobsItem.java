package com.wec.resume.model;

/**
 * Created by Majfrendmartin on 2017-08-05.
 */

public class JobsItem extends BaseResumeItem<Job> {
    @Override
    public ResumeItemType getType() {
        return ResumeItemType.JOBS;
    }
}
