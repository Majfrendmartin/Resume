package com.wec.resume.model;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Education extends BaseItem {
    private String degree;
    private String school;
    private String faculty;
    @SerializedName("start_year")
    private int startYear;
    @SerializedName("end_year")
    private int endYear;

    private String cover;

    private String speciality;

    @NonNull
    @Override
    public String getTitle() {
        return degree;
    }
}
