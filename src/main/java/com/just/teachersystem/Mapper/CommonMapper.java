package com.just.teachersystem.Mapper;
import	java.util.List;

import com.just.teachersystem.VO.UserInfo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 通用mapper
 */
@Mapper
public interface CommonMapper {


    //获取用户信息
    UserInfo getInfo(String worknum);

    //更新用户信息
    int updateUserInfo(UserInfo userInfo);
    //插入用户
    int insertUserList(List<UserInfo> userlist);
    int insertUser(UserInfo userInfo);
}
