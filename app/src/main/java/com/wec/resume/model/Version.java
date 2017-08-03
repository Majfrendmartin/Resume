package com.wec.resume.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Version {
    private int version;
    private String url;
}
