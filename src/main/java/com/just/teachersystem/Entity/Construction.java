package com.just.teachersystem.Entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Construction {

  private long id;
  private long dptId;//关联作者所在部门编号
  private String projectNum;//项目编号
  private String project;//项目名称
  private long principalId;//项目负责关联id
  private String teammate;//课题组成员
  private String startYear;//立项年度
  private Date beginTime;//项目开始时间
  private Date endTime;//项目结束时间
  private String sponsor;//主办单位
  private long kindId;//关联分类Id
  private String testimonial;//证明材料
  private double expenditure;//建设经费
  private long point;//业绩分
  private String computeYear;//业绩分计算年度
  private double bonus;//奖金
  private String fileNumber;//归档文件盒编号
  private long isEnd;//是否结束
  private String schoolyear;//学年
  private String year;//年度
  private long status;//状态：-1表示为通过，0表示正在核审，1表示学院通过，2表示科室审核通过
  private Date lastTime;//最后操作时间
  private String reason;//原因
  private String null1;//
  private String null2;
  private String null3;



}
