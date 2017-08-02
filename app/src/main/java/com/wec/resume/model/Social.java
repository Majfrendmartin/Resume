package com.wec.resume.model;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Social {
    public enum Type {

        GITHUB("github"),
        LINKED_IN("linkedin");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private Type type;
    private String url;
}
