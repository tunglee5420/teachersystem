package com.just.teachersystem.Entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class User implements Serializable {

  private long id;
  private long dptId; //关联部门编号
  private String name; //姓名
  private String worknum; //工号
  private String password;//密码
  private String gender;//性别
  private Date birthday;//出生年月
  private Date enterTime;//入校时间
  private String phone;//电话
  private String techTittle;//专业技术职称
  private String eduBgd;//最高学历
  private String degree;//最高学位
  private String school;//授学位单位名称
  private String major;//获最高学位的专业名称
  private long doubleTeacher;//是否双师型 0表示不是，1 表示是
  private long background;//是否具有行业背景 0表示不是，1 表示是
  private long tutor;//是否博硕士生导师 0表示不是，1 表示是
  private long permission;//权限等级 0表示普通用户，1表示学院负责人，2表示科室负责人，3表示超级管理员
  private String null1;//预留空字段
  private String null2;//预留空字段
  private String null3;//预留空字段



}
