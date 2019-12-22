package com.just.teachersystem.VO;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
@Getter
@Setter
public class AchievementInfo implements Serializable {
  private long aid;
  private String department;
  private String worknum;
  private String name;
  private String teammate;
  private String production;
  private String class1;
  private String class2;
  private String class3;
  private String level;
  private String unit;
  private String publishTime;
  private long patent;
  private String certificate;
  private String schoolYear;
  private String year;
  private long status;
  private String reason;
  private Date lastTime;

}
