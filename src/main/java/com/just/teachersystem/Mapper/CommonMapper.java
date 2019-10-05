package com.just.teachersystem.Mapper;


import	java.util.List;
import java.util.Set;

import com.just.teachersystem.Entity.Department;
import com.just.teachersystem.Entity.Kind;
import com.just.teachersystem.VO.AchievementInfo;
import com.just.teachersystem.VO.ConstructionInfo;
import com.just.teachersystem.VO.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


/**
 * 通用mapper
 */
@Mapper
@Component
public interface CommonMapper {


    //根据学号获取用户信息
    @Select("select * from userInfo where worknum=#{worknum}")
    UserInfo getInfo(String worknum);

    /**
     * 根据条件查询用户信息
     * @param userInfo
     * @return
     */

    List getUserInfoList(UserInfo userInfo);


    //插入用户
    int insertUserList(List<UserInfo> userlist);

    int insertUser(UserInfo userInfo);


    //获得类别
    @Select("Select class1,class2,class3 from kind")
    List<Kind> getTypeList();

    //获得级别
    @Select("select level from level")
    Set<String> getLevelSet();

    //得到部门
    @Select(" select id ,dptname from department")
    Set<Department> getDepartmentList();

    /**
     * 更改建设类信息
     * @param construction
     * @return
     */
    int updateConstruction(ConstructionInfo construction);

    /**
     * 插入建设类信息列表
     * @param list
     * @return
     */
    int insertToConstructionList(List list);

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