package com.wec.resume.model;


import android.support.annotation.Nullable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Bio {
    private String name;
    private String surname;
    private String avatar;

    @Nullable
    private String description;

    private Social[] socials;
}
