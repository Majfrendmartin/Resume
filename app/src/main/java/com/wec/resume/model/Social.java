package com.wec.resume.model;


import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Social {
    private Type type;
    private String url;

    public enum Type {
        @SerializedName("github")
        GITHUB,

        @SerializedName("linkedin")
        LINKED_IN
    }
}
