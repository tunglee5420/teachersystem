package com.just.teachersystem.VO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class UserInfo {
    private int dtpId;//单位号
    private String dptname;//院部名称
    private String name;//姓名
    private String worknum;//工号
    @JsonIgnoreProperties
    private String password;//密码
    private String gender;//性别
    private Date birthday;//出生日期
    private Date enterTime;//入校时间
    private String phone;//电话
    private String techTittle;//专业技术职称
    private String eduBgd;//最高学历
    private String degree;//最高学位
    private String school;//毕业学校
    private String major;//最高学位的专业名称
    private int doubleTeacher;//是否双师型 0表示不是 1表示是
    private int background;//是否有行业背景 0表示不是 1表示是
    private int tutor;//是否博硕士导师 0表示不是 1表示是
    private int permission;//权限等级 1表示普通用户 2表示院系负责人 3表示科室负责人 4表示超级管理员

}
