package com.just.teachersystem.Mapper;
import	java.util.List;

import com.just.teachersystem.VO.AchievementInfo;
import com.just.teachersystem.VO.AwardInfo;
import com.just.teachersystem.VO.ConstructionInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 普通用户Mapper
 */
@Mapper
@Component
public interface TeacherMapper {
    /**
     * 添加建设类成就信息
     * @param constructionInfo
     * @return
     */
    int insertToConstruction(ConstructionInfo constructionInfo);

    /**
     * 根据学号查询建设类
     * @param worknum
     * @return
     */
    List selectConstructionByWorknum(String worknum);

    /**
     * 添加成果类信息
     * @param info
     * @return
     */
    int insertToAchievement(AchievementInfo info);

    /**
     * 根据工号查询成果类
     * @param worknum
     * @return
     */
    List selectAchievementByWorknum(String worknum);

    /**
     * 添加获奖类信息
     * @param info
     * @return
     */
    int insertToAward(AwardInfo info);

    /**
     * 根据工号查询获奖类
     * @param worknum
     * @return
     */
    List selectAwardByWorknum(String worknum);


}
