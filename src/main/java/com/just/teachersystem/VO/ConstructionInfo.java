package com.just.teachersystem.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ConstructionInfo implements Serializable {
  private long cid;
  private String department;
  private String projectNum;
  private String project;
  private String worknum;
  private String name;
  private String teammate;
  private String class1;
  private String class2;
  private String class3;
  private String startTime;
  private String beginToEndTime;
  private String level;
  private String sponsor;
  private String testimonial;
  private double expenditure;
  private long point;
  private String computeYear;
  private double bonus;
  private String fileNumber;
  private long isEnd;
  private String schoolyear;
  private String year;
  private long status;
  private String reason;
}
