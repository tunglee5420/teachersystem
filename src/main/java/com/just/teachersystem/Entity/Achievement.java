package com.just.teachersystem.Entity;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Achievement {

  private long id;
  private long dptId;//关联部门编号
  private long authorId;//第一作者
  private String teammate;//队友
  private String production;//成果名称
  private long kindId;//管理类别id
  private long levelId;//级别id
  private String unit;//出版/授权单位
  private Date publishTime;//发表时间
  private long patent;//是否被转让或采用（仅限于专利）是否被转让或采用（仅限于专利）
  private String certificate;//证书
  private String schoolYear;//学年
  private String year;//年度
  private Date lastTime;//最后操作时间
  private long status;//审批状态-1表示未通过  ，0 表示核审中，1表示已通过
  private String reason;//描述
  private String null2;//预留空字段
  private String null3;
  private String null4;




}
