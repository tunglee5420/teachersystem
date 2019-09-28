package com.just.teachersystem.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Kind implements Serializable {

  private long id;
  private String class1;//大类
  private String class2;//分类
  private String class3;//类别
  private long computeDptId;//计算科室
  private String null1;//空字段
  private String null2;//




}
