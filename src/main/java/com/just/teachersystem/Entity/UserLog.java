package com.just.teachersystem.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class UserLog {
    private int id;
    private String worknum;//工号
    private String ip;//ip
    private String role;//角色
    private Date date;//日期
    private String description;//描述
    private String url;//操作细节
}
