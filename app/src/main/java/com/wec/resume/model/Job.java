package com.wec.resume.model;



import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Job {
    private boolean isCurrent;
    private String companyName;
    private String positionName;
    private Date startDate;
    private Date endDate;
}
