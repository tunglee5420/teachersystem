package com.just.teachersystem.Service.ServiceImp;

import com.just.teachersystem.Mapper.*;
import com.just.teachersystem.Service.CollegeAdminService;
import com.just.teachersystem.Utill.EncryptUtil;
import com.just.teachersystem.VO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 学院管理员服务实现层
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CollegeAdminServiceImp implements CollegeAdminService {

    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    ConstructionMapper construction;
    @Autowired
    AchievementMapper achievement;
    @Autowired
    AwardMapper award;
    @Autowired
    PerformanceMapper performance;
    @Autowired
    BonusMapper bonus;



    /**
     * 查询用户信息
     * @param info
     * @return
     */
    public List getUserInfo(UserInfo info){
        List list=teacherMapper.getUserInfoList(info);
        if(list == null||list.size()==0) return null;
        return list;
    }

    /**
     *
     * @param userInfo
     * @return
     */
    public boolean updateUserInfo(UserInfo userInfo){
        if (userInfo==null) return false;
        if(userInfo.getPassword()!=null )  userInfo.setPassword(EncryptUtil.getInstance().MD5(userInfo.getPassword()));
        int res= teacherMapper.updateUserInfo(userInfo);
        return res==1?true:false;
    }

    /**
     * 获取部门建设类信息
     * @param info
     * @return
     */
    public List getDptConstructions(ConstructionInfo info){
        if (info==null) return null;
        List list = construction.selectConstructions(info);
        return list;
    }

    /**
     * 获取部门成果类信息
     * @param info
     * @return
     */
    public List getDptAchievements(AchievementInfo info){
        if (info==null) return null;
        List list=achievement.selectAchievements(info);
        return list;
    }

    /**
     *获取部门获奖类信息
     * @param info
     * @return
     */
    public List getDptAwards(AwardInfo info){
        if (info==null) return null;
        List list=award.selectAwards(info);
        return list;
    }


    /**
     * 学院管理员核审建设类
     * @param info
     * @return
     */
    public int  ConstructionCheck(ConstructionInfo info){
        if(info==null) return 0;
        int res= construction.updateConstruction(info);
        if(res>0) return 1;
        return 0;
    }

    /**
     * 获取确认业绩名单
     * @param info
     * @return
     */
    public List confirmPerformance(PerformanceInfo info){
        if(info == null)
            return null;
        List list=performance.selectPerformanceList(info);
        if (list==null){
            return null;
        }
        return list;
    }

    /**
     * 获取确认津贴名单
     * @param info
     * @return
     */
    public List<BonusInfo> confirmBonus(BonusInfo info){
        if(info == null)
            return null;
        List list=bonus.selectBonusList(info);
        if (list==null){
            return null;
        }
        return list;
    }

}
