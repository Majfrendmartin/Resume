package com.wec.resume.model;


import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Education {
    private String title;
    private String schoolName;
    private Date startDate;
    private Date endDate;
}
