package com.just.teachersystem.Mapper;


import	java.util.List;
import java.util.Set;

import com.just.teachersystem.Entity.Department;
import com.just.teachersystem.Entity.Kind;
import com.just.teachersystem.VO.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


/**
 * 通用mapper
 */
@Mapper
@Component
public interface CommonMapper {


    //获取用户信息
    UserInfo getInfo(String worknum);

    //更新用户信息
    int updateUserInfo(UserInfo userInfo);

    //插入用户
    int insertUserList(List<UserInfo> userlist);

    int insertUser(UserInfo userInfo);


    //获得类别
    @Select("Select class1,class2,class3 from kind")
    List<Kind> getTypeList();

    //获得级别
    @Select("select level from level")
    Set<String> getLevelSet();

    //得到部门
    @Select(" select id ,dptname from department")
    Set<Department> getDepartmentList();
}