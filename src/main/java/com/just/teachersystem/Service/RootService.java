package com.just.teachersystem.Service;

import com.just.teachersystem.Entity.Kind;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.stereotype.Service;

/**
 * 超级管理员服务层
 */
@Service
public interface RootService {
    boolean addType(Kind kind);

    boolean updateUserInfo(UserInfo userInfo);
}
