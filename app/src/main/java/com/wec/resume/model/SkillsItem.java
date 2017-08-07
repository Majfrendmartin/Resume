package com.wec.resume.model;

/**
 * Created by Majfrendmartin on 2017-08-05.
 */

public class SkillsItem extends Section<Skill> {
    @Override
    public SectionType getType() {
        return SectionType.SKILLS;
    }
}
