package com.just.teachersystem.Service;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.stereotype.Service;

import	java.util.Map;

/**
 * 常见通用服务层
 */
@Service
public interface CommonService {
    Map<String, Object> login(String worknum, String password);
    boolean updateUserInfo(UserInfo userInfo);
}
