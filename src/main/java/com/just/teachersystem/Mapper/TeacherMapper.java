package com.just.teachersystem.Mapper;
import	java.util.List;

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


    List selectConstructionByWorknum(String worknum);
}
