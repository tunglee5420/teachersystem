package com.just.teachersystem.Mapper;


import com.just.teachersystem.Entity.Kind;

import com.just.teachersystem.VO.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 超级管理员Mapper
 */
@Mapper
public interface RootMapper {

    @Insert("insert into kind (class1,class2,class3,computeDptId) values(#{class1},#{class2},#{class3},#{computeDptId}) ")
    boolean addType(Kind kind);

    //更新用户信息
    int updateUserInfo(UserInfo userInfo);

    //插入用户链表
    int insertUserList(List<UserInfo> userlist);


}
