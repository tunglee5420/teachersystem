package com.just.teachersystem.Mapper;

import com.just.teachersystem.VO.UserInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 普通用户Mapper
 */
@Mapper
@Component
public interface TeacherMapper {

    /**
     * 根据学号获取用户信息
     * @param worknum
     * @return
     */
    @Select("select * from userInfo where worknum=#{worknum}")
    UserInfo getInfo(String worknum);

    /**
     * 根据条件查询用户信息
     * @param userInfo
     * @return
     */

    List getUserInfoList(UserInfo userInfo);


    /**
     * 插入用户
     * @param userInfo
     * @return
     */
    int insertUser(UserInfo userInfo);


    /**
     * 更新用户信息
     * @param userInfo
     * @return
     */
    int updateUserInfo(UserInfo userInfo);

    /**
     * 插入用户链表
     * @param userlist
     * @return
     */
    int insertUserList(List<UserInfo> userlist);

    /**
     * 删除用户的信息
     * @param worknum
     * @return
     */
    @Delete("delete from user where worknum=#{worknum}")
    int deleteByWorknum(String worknum);
}
