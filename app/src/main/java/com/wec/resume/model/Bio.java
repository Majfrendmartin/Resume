package com.wec.resume.model;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Bio {
    private String name;
    private String surname;
    private String avatar;
    private Social [] socials;
}
