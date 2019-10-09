package com.just.teachersystem.Mapper;

import com.just.teachersystem.VO.AchievementInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper

public interface AchievementMapper {

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
     *条件筛选查询成果类
     * @param info
     * @return
     */
    List selectAchievements(AchievementInfo info);

    /**
     * 插入成果类信息列表
     * @param list
     * @return
     */
    int insertToAchievementList(List list);

    /**
     * 更新成果类的信息
     * @param info
     * @return
     */
    int updateAchievement(AchievementInfo info);


}
