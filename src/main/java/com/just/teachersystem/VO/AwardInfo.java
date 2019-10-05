package com.just.teachersystem.VO;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
@Setter
@Getter
public class AwardInfo implements Serializable {
  private long aid;
  private String department;
  private String worknum;
  private String name;
  private String teammate;
  private String awardUnit;
  private String content;
  private String class1;
  private String class2;
  private String class3;
  private String level;
  private String prize;
  private double bonus;
  private String awardYear;
  private String certificate;
  private Date awardTime;
  private String schoolYear;
  private String year;
  private long status;
  private String reason;

}
