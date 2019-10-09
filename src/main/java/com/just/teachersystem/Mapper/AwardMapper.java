package com.just.teachersystem.Mapper;

import com.just.teachersystem.VO.AwardInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AwardMapper {


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

    /**
     * 根据条件筛选获奖类
     * @param info
     * @return
     */
    List selectAwards(AwardInfo info);

    /**
     * 插入获奖类信息列表
     * @param list
     * @return
     */
    int insertToAwardList(List list);

    /**
     * 插入获奖类信息
     * @param info
     * @return
     */
    int updateAward(AwardInfo info);


}
