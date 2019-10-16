package com.just.teachersystem.Mapper;

import com.just.teachersystem.VO.BonusInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BonusMapper {

    /**
     * 查询津贴汇总表
     * @param info
     * @return
     */
    List selectBonusList(BonusInfo info);

    /**
     * 更新津贴汇总表
     * @param info
     * @return
     */
    int updateBonus(BonusInfo info);

    /**
     * 插入津贴链表
     * @param list
     * @return
     */

    int insertToBonusList(List list);

    /**
     * 插入津贴记录
     * @param info
     * @return
     */
    int insertToBonus(BonusInfo info);

}
