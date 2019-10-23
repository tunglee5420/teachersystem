package com.just.teachersystem.Service.ServiceImp;
import	java.util.ArrayList;

import com.just.teachersystem.Mapper.*;
import com.just.teachersystem.Service.OfficeAdminService;
import com.just.teachersystem.VO.AchievementInfo;
import com.just.teachersystem.VO.AwardInfo;
import com.just.teachersystem.VO.ConstructionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 科室管理员服务实现层
 */
@Service
public class OfficeAdminServiceImp implements OfficeAdminService {

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
     * 科室管理员获取建设类核审信息
     * @return
     */
    public List<ConstructionInfo> getUserConstruction(ConstructionInfo constructionInfo) {
        if (constructionInfo==null)
            return null;
        List list=construction.selectConstructions(constructionInfo);
        return list;
    }


    /**
     * 科室管理员获取成果类核审信息
     * @param achievementInfo
     * @return
     */
    public List<AchievementInfo> getUserAchievement(AchievementInfo achievementInfo){
        if (achievementInfo==null)
            return null;
        List list = achievement.selectAchievements(achievementInfo);
        return list;

    }

    /**
     * 科室管理员获取获奖类核审信息
     * @param info
     * @return
     */
    public List <AwardInfo> getUserAward(AwardInfo info){
        if (info==null)
            return null;

        List list = award.selectAwards(info);
        return list;
    }

    /**
     * 插入到业绩分汇总表
     * @param list
     * @return
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public boolean insertToPerformanceList(List list){
        if(list==null) return false;
        int res=performance.insertToPerformanceList(list);
        if(res>0)
            return true;
        return false;
    }

    /**
     * 插入到奖金汇总表
     * @param list
     * @return
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public boolean insertToBonusList(List list){
        if(list==null) return false;
        int res=bonus.insertToBonusList(list);
        if(res>0)
            return true;
        return false;
    }


}
