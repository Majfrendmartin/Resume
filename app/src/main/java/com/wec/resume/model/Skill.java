package com.wec.resume.model;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Skill {
    private String name;
    private int level;
}
