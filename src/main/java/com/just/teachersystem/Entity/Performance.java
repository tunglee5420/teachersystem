package com.just.teachersystem.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@ToString
public class Performance implements Serializable {

  private long id;
  private String department;
  private String computeoffice;
  private String type;
  private String year;
  private String project;
  private String master;
  private long points;
  private int status=-2;
  private Date lastTime;
  private String null3;



}
