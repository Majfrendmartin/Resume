package com.wec.resume.model;


import android.support.annotation.NonNull;

public abstract class BaseItem {

    @NonNull
    public abstract String getTitle();

    public boolean isHeader() {
        return false;
    }
}
