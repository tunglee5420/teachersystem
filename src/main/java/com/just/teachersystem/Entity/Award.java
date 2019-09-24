package com.just.teachersystem.Entity;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Award {

  private long id;
  private long dtpId;//关联部门Id
  private long userId;//第一获奖者ID
  private String teammate;//获奖成员
  private String awardUnit;//颁奖单位
  private String content;//获奖成员
  private long kindId;//获奖类别ID
  private long level;//获奖级别ID
  private String prize;//奖项
  private double bonus;//奖金
  private String awardYear;//获奖年度
  private String certificate;//证书
  private Date awardTime;//获奖时间
  private String schoolYear;//学年
  private String year;//年度
  private long status;//状态 -1表示通过，0表示核审中，1表示通过
  private String reason;//原因
  private Date lastTime;//最后操作时间
  private String null1;//空字段
  private String null2;//
  private String null3;//


}
