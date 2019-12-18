package com.just.teachersystem.VO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//import javax.validation.constraints.PastOrPresent;
//import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;


@Getter
@Setter
@ToString

@JsonIgnoreProperties(value={"password"})
public class UserInfo implements Serializable {
    private int dtpId;//单位号
    private String dptname;//院部名称
    private String name;//姓名
//    @Size(min = 11,max = 12,message ="密码必须是6到8位")
    private String worknum;//工号

//    @Size(max = 10,min =6 ,message ="密码必须是6到8位")
    private String password;//密码
    private String gender;//性别
//    @PastOrPresent
    private Date birthday;//出生日期
    private Date enterTime;//入校时间

//    @Pattern(regexp = "/^1[3456789]\\d{9}$/" ,message = "电话号码的格式有误")
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
