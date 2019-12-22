package com.just.teachersystem.VO;
import	java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;




@Getter
@Setter
@ToString
public class BonusInfo implements Serializable {

        private long id;
        private String department;//部门
        private String computeoffice;//业绩分科室
        private String type;//类别
        private String year;//立项年度
        private String project;//项目名称
        private String master;//项目负责人
        private double bonus;//奖金
        private int status;//1表示可用，0表示禁用
        private Date lastTime;
}
