package com.just.teachersystem.Mapper;

import com.just.teachersystem.VO.ConstructionInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ConstructionMapper {

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
     * 条件筛选建设类
     * @param construction
     * @return
     */
    List selectConstructions(ConstructionInfo construction);

    /**
     * 插入建设类信息列表
     * @param list
     * @return
     */
    int insertToConstructionList(List list);

    /**
     * 更改建设类信息
     * @param construction
     * @return
     */
    int updateConstruction(ConstructionInfo construction);

}
