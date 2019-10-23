package com.just.teachersystem.Service;

import com.just.teachersystem.Entity.Kind;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.stereotype.Service;

/**
 * 超级管理员服务层
 */
@Service
public interface RootService {
    /**
     * 添加类别
     * @param kind
     * @return
     */
    boolean addType(Kind kind);


    /**
     * 添加级别
     * @param level
     * @return
     */
    boolean addLevel(String level);

    /**
     * 超管添加类别(仅限class3)
     * @param class3
     * @return
     */
    boolean deleteType(String class3);

    /**
     * 删除级别
     * @param level
     * @return
     */
    boolean deleteLevel(String level);

    /**
     *添加新的用户成员
     * @param user
     * @return
     */
    boolean addUser(UserInfo user);

    /**
     * 更改用户信息
     * @param userInfo
     * @return
     */
    boolean updateUserInfo(UserInfo userInfo);

    /**
     * 根据工号删除用户
     * @param worknum
     * @return
     */
    boolean deleteUser(String worknum);

}
