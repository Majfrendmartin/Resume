package com.wec.resume.model;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Education {
    private String degree;
    private String school;
    private String faculty;
    @SerializedName("start_year")
    private int startYear;
    @SerializedName("end_year")
    private int endYear;
}
