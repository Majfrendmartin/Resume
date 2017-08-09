package com.wec.resume.model;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Resume {
    private Bio bio;
    private int version;
    private EducationSection education;
    private JobsSection jobs;
    private SkillsSection skills;
    private AboutSection about;
}
