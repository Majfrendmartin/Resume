package com.wec.resume.model;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Job extends BaseItem {
    @SerializedName("current")
    private boolean isCurrent;

    @SerializedName("company")
    private String companyName;

    @SerializedName("position")
    private String positionName;

    @SerializedName("start_date")
    private Date startDate;

    @Nullable
    @SerializedName("end_date")
    private Date endDate;

    private List<String> responsibilities;

    @NonNull
    @Override
    public String getTitle() {
        return positionName;
    }
}
