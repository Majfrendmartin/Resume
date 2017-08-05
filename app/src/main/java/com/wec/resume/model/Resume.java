package com.wec.resume.model;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Resume {
    private Bio bio;
    private int version;
    private EducationItem education;
    private JobsItem jobs;
    private SkillsItem skills;
}
