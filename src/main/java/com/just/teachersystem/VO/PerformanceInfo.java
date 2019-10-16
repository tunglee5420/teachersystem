package com.just.teachersystem.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@ToString
public class PerformanceInfo implements Serializable {
    private long id;
    private String department;
    private String computeoffice;
    private String type;
    private String year;
    private String project;
    private String master;
    private long points;
    private int status;
    private Date lastTime;
}
