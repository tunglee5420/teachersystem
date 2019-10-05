package com.just.teachersystem.Service.ServiceImp;

import com.just.teachersystem.Mapper.CommonMapper;
import com.just.teachersystem.Service.CollegeAdminService;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学院管理员服务实现层
 */
@Service
public class CollegeAdminServiceImp implements CollegeAdminService {
    @Autowired
    CommonMapper common;

    /**
     * 查询用户信息
     * @param info
     * @return
     */
    public List getUserInfo(UserInfo info){
        List list=common.getUserInfoList(info);
        if(list == null||list.size()==0) return null;
        return list;
    }
}
