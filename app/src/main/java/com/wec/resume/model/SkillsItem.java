package com.wec.resume.model;

/**
 * Created by Majfrendmartin on 2017-08-05.
 */

public class SkillsItem extends BaseResumeItem<Skill> {
    @Override
    public ResumeItemType getType() {
        return ResumeItemType.SKILLS;
    }
}
