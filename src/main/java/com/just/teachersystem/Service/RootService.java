package com.just.teachersystem.Service;
import	java.util.List;

import com.just.teachersystem.Entity.Kind;
import com.just.teachersystem.Entity.Performance;
import com.just.teachersystem.VO.BonusInfo;
import com.just.teachersystem.VO.PerformanceInfo;
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

    /**
     * 检索用户信息
     * @param info
     * @return
     */
    List<UserInfo> getUserInfo(UserInfo info);

    /**
     * 根据条件检索
     * @param info
     * @return
     */
    List<PerformanceInfo> getPerfromanceList(PerformanceInfo info);

    /**
     * 更新业绩分信息表
     * @param info
     * @return
     */
    boolean updatePerformanceInfo(PerformanceInfo info);

    /**
     * 添加业绩分信息表
     * @param info
     * @return
     */
    boolean addPerformanceInfo(PerformanceInfo info);

    /**
     * 根据条件检索奖金信息表
     * @param info
     * @return
     */
    List<BonusInfo> getBonusList(BonusInfo info);

    /**
     * 更新奖金信息表
     * @param info
     * @return
     */
    boolean updateBonusInfo(BonusInfo info);

    /**
     * 添加奖金信息表
     * @param info
     * @return
     */
    boolean addBonusInfo(BonusInfo info);
}
