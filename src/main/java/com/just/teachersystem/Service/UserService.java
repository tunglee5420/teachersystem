package com.just.teachersystem.Service;

import org.springframework.stereotype.Service;

/**
 * 普通用户服务层
 */
@Service
public interface UserService {
    boolean check(String worknum,String password);
}
