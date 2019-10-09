package com.just.teachersystem.Service;
import	java.util.List;


import com.just.teachersystem.VO.AchievementInfo;
import com.just.teachersystem.VO.AwardInfo;
import com.just.teachersystem.VO.ConstructionInfo;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.stereotype.Service;

/**
 * 学院管理员服务层
 */
@Service
public interface CollegeAdminService {
    /**
     * 查询用户信息
     * @param info
     * @return
     */
    List getUserInfo(UserInfo info);

    /**
     * 修改用户密码
     * @param worknum
     * @param password
     * @return
     */
    int updateUserPassword(String worknum,String password,String department);

    /**
     * 修改用户电话
     * @param worknum
     * @param phone
     * @return
     */
    int uodateUserPhone(String worknum,String phone,String departemnt);

    /**
     * 获取部门建设类信息
     * @param info
     * @return
     */
    List getDptConstructions(ConstructionInfo info);

    /**
     * 获取部门成果类信息
     * @param info
     * @return
     */
    List getDptAchievements(AchievementInfo info);

    /**
     * 获取部门获奖类信息
     * @param info
     * @return
     */
    List getDptAwards(AwardInfo info);
}
