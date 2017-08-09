package com.wec.resume.model;

import static com.wec.resume.model.Section.SectionType.SKILLS;

/**
 * Created by Majfrendmartin on 2017-08-05.
 */

public class SkillsSection extends Section<Skill> {
    @Override
    public SectionType getType() {
        return SKILLS;
    }
}
