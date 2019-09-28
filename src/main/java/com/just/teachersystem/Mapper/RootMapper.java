package com.just.teachersystem.Mapper;


import com.just.teachersystem.Entity.Kind;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 超级管理员Mapper
 */
@Mapper
public interface RootMapper {

    @Insert("insert into kind (class1,class2,class3,computeDptId) values(#{class1},#{class2},#{class3},#{computeDptId}) ")
    boolean addType(Kind kind);
}
