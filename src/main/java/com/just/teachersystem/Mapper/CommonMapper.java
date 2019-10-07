package com.just.teachersystem.Mapper;


import	java.util.List;
import java.util.Set;

import com.just.teachersystem.Entity.Department;
import com.just.teachersystem.Entity.Kind;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


/**
 * 通用mapper
 */
@Mapper
@Component
public interface CommonMapper {

    //获得类别
    @Select("Select class1,class2,class3 from kind")
    List<Kind> getTypeList();

    //获得级别
    @Select("select level from level")
    Set<String> getLevelSet();

    //得到部门
    @Select(" select id ,dptname from department")
    Set<Department> getDepartmentList();

    @Insert("insert into kind (class1,class2,class3,computeDptId) values(#{class1},#{class2},#{class3},#{computeDptId}) ")
    boolean addType(Kind kind);

}