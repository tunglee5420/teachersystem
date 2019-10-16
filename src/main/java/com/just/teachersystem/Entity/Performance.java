package com.just.teachersystem.Entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
public class Performance implements Serializable {

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
  private String null3;



}
