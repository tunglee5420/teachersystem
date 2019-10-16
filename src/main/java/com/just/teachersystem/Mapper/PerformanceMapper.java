package com.just.teachersystem.Mapper;
import	java.util.List;

import com.just.teachersystem.VO.PerformanceInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PerformanceMapper {
    /**
     * 查询业绩汇总表
     * @param params
     * @return
     */
    List selectPerformanceList(PerformanceInfo params);

    /**
     * 更新业绩汇总表
     * @param params
     * @return
     */
    int updatePerformance(PerformanceInfo params);

    /**
     * 插入业绩链表
     * @param list
     * @return
     */
    int insertToPerformanceList(List list);

    /**
     * 插入单个业绩
     * @param performanceInfo
     * @return
     */
    int  insertToPerformance(PerformanceInfo performanceInfo);

}
