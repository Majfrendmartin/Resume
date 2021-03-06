package com.wec.resume.model;


import android.support.annotation.NonNull;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class About extends BaseItem {
    private String note;

    @NonNull
    @Override
    public String getTitle() {
        return note;
    }
}
