package com.just.teachersystem.Service;

import com.just.teachersystem.VO.AchievementInfo;
import com.just.teachersystem.VO.AwardInfo;
import com.just.teachersystem.VO.ConstructionInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 科室管理员服务层
 */
@Service
public interface OfficeAdminService {

    /**
     * 科室管理员获取建设类核审信息
     * @return
     */
    List<ConstructionInfo> getUserConstruction(ConstructionInfo construction);

    /**
     * 科室管理员获取成果类核审信息
     * @param achievementInfo
     * @return
     */
    List<AchievementInfo> getUserAchievement(AchievementInfo achievementInfo);

    /**
     * 科室管理员获取获奖类核审信息
     * @param info
     * @return
     */
   List <AwardInfo> getUserAward(AwardInfo info);
    /**
     * 科室管理员添加业绩核审信息
     * @param list
     * @return
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    boolean insertToPerformanceList(List list);

    /**
     * 插入到奖金汇总表
     * @param list
     * @return
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
     boolean insertToBonusList(List list);

}
