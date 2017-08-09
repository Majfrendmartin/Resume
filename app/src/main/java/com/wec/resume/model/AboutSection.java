package com.wec.resume.model;


import lombok.experimental.Accessors;

import static com.wec.resume.model.Section.SectionType.ABOUT;

@Accessors(chain = true)
public class AboutSection extends Section<About> {

    @Override
    public SectionType getType() {
        return ABOUT;
    }
}
