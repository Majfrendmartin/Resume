package com.wec.resume.model;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Skill extends BaseItem {
    private String name;

    @Nullable
    private String description;

    private int level;

    @NonNull
    @Override
    public String getTitle() {
        return name;
    }
}
