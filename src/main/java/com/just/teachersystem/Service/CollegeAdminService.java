package com.just.teachersystem.Service;
import	java.util.List;


import com.just.teachersystem.VO.UserInfo;
import org.springframework.stereotype.Service;

/**
 * 学院管理员服务层
 */
@Service
public interface CollegeAdminService {
    /**
     * 查询用户信息
     * @param info
     * @return
     */
    List getUserInfo(UserInfo info);
}
