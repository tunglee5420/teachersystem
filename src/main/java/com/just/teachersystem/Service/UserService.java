package com.just.teachersystem.Service;

import com.just.teachersystem.VO.AchievementInfo;
import com.just.teachersystem.VO.AwardInfo;
import com.just.teachersystem.VO.ConstructionInfo;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 普通用户服务层
 */
@Service
public interface UserService {
    /**
     * 验证密码
     * @param worknum
     * @param password
     * @return
     */
    boolean check(String worknum,String password);

    /**
     * 根据工号查个人信息
     * @param worknum
     * @return
     */
    UserInfo getUserInfo(String worknum);
    /**
     * 添加建设类信息
     * @param info
     * @return
     */
    int addConstruction(ConstructionInfo info);

    /**
     * 获得用户个人建设类记录
     * @param worknum
     * @return
     */
    List getMyConstructionInfo(String worknum);

    /**
     * 添加成果类信息
     * @param info
     * @return
     */
    boolean addAchievement(AchievementInfo info);

    /**
     * 获得用户个人成果类记录
     * @param worknum
     * @return
     */
    List getMyAchievementInfo(String worknum);

    /**
     * 添加获奖类信息
     * @param info
     * @return
     */
    boolean addAward(AwardInfo info);

    /**
     * 获得用户个人获奖类记录
     * @param worknum
     * @return
     */
    List getMyAwardInfo(String worknum);


}
